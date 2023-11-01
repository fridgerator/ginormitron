import { AwsLogDriver, Cluster, ContainerImage, FargateService, FargateTaskDefinition } from "aws-cdk-lib/aws-ecs";
import { ApplicationLoadBalancer, ApplicationProtocol } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { Effect, PolicyStatement, Role, ServicePrincipal } from "aws-cdk-lib/aws-iam";
import { Construct } from "constructs";

export interface EcsServiceProps {
    cluster: Cluster
    registry: string
    serviceName: string
    publicAlb?: boolean
}

export class EcsService extends Construct {
    latticeLoadBalancer: ApplicationLoadBalancer

    constructor(scope: Construct, id: string, props: EcsServiceProps) {
        super(scope, id)

        const executionRole = new Role(this, "execution-role", {
            roleName: `${props.serviceName}-execution-role`,
            assumedBy: new ServicePrincipal("ecs-tasks.amazonaws.com")
        })
        executionRole.addToPrincipalPolicy(
            new PolicyStatement({
                effect: Effect.ALLOW,
                actions: [
                    "ecr:BatchGetImage",
                    "ecr:GetDownloadUrlForLayer",
                    "ecr:GetAuthorizationToken",
                    "secretsmanager:*"
                ],
                resources: ["*"]
            })
        )

        const taskDefinition = new FargateTaskDefinition(this, `${props.serviceName}-taskdef`, {
            memoryLimitMiB: 2048,
            executionRole
        });

        const appContainer = taskDefinition.addContainer(props.serviceName, {
            image: ContainerImage.fromRegistry(props.registry),
            memoryLimitMiB: 2048,
            logging: new AwsLogDriver({ streamPrefix: props.serviceName }),
            environment: {
                SPRING_PROFILES_ACTIVE: "aws"
            }
        })

        appContainer.addPortMappings({
            containerPort: 8080
        })

        const service = new FargateService(this, `${props.serviceName}-service`, {
            cluster: props.cluster,
            taskDefinition
        })

        if (props.publicAlb) {
            const publicAlb = new ApplicationLoadBalancer(this, "PublicLoadBalancer", {
                vpc: props.cluster.vpc,
                internetFacing: true
            })
            const publicListener = publicAlb.addListener('PublicListner', {
                protocol: ApplicationProtocol.HTTP
            })
            const publicTargetGroup = publicListener.addTargets('ECS', {
                port: 80,
                healthCheck: {
                    path: '/actuator/health'
                }
            })
            publicTargetGroup.addTarget(service)
        }

        // inernal ALB for lattice
        this.latticeLoadBalancer = new ApplicationLoadBalancer(this, "PrivateLoadBalancer", {
            vpc: props.cluster.vpc,
            internetFacing: false
        })
        const privateListener = this.latticeLoadBalancer.addListener('PublicListner', {
            protocol: ApplicationProtocol.HTTP,
            port: 80
        })
        const privateTargetGroup = privateListener.addTargets('ECS', {
            port: 80,
            healthCheck: {
                path: '/actuator/health'
            }
        })
        privateTargetGroup.addTarget(service)
    }
}
