import { CfnOutput, RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
import { InstanceType, Vpc } from "aws-cdk-lib/aws-ec2";
import { AwsLogDriver, Cluster, ContainerImage, FargateService, FargateTaskDefinition, Protocol } from "aws-cdk-lib/aws-ecs";
import { ApplicationLoadBalancedFargateService } from "aws-cdk-lib/aws-ecs-patterns";
import { ApplicationLoadBalancer } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { Effect, PolicyStatement, Role, ServicePrincipal } from "aws-cdk-lib/aws-iam";
import { Construct } from "constructs";

export class CustomerDataStack extends Stack {
    props: StackProps

    constructor(scope: Construct, id: string, props: StackProps) {
        super(scope, id, props);
        this.props = props;
        this.build();
    }

    build() {
        const vpc = new Vpc(this, "customer-data-vpc", {
            maxAzs: 2,
            vpcName: "customer-data-vpc"
        })

        const cluster = new Cluster(this, "customer-data-cluster", {
            vpc
        })

        const executionRole = new Role(this, "execution-role", {
            roleName: "customer-data-execution-role",
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

        // cluster.addCapacity("DefaultAutoScalingGroupCapacity", {
        //     instanceType: new InstanceType("t2.large")
        // })

        // const taskDefinition = new FargateTaskDefinition(this, "customer-data-taskdef", {
        //     memoryLimitMiB: 2048,
        //     executionRole
        // });

        // const appContainer = taskDefinition.addContainer("customer-data", {
        //     image: ContainerImage.fromRegistry("270744187218.dkr.ecr.us-east-1.amazonaws.com/customerdata:latest"),
        //     memoryLimitMiB: 2048,
        //     logging: new AwsLogDriver({ streamPrefix: 'customer-data' })
        // })

        // appContainer.addPortMappings({
        //     containerPort: 8080
        // })

        // const service = new FargateService(this, "customer-data-service", {
        //     cluster,
        //     taskDefinition
        // })

        // https://github.com/aws/aws-cdk/blob/main/packages/aws-cdk-lib/aws-ecs-patterns/lib/fargate/application-load-balanced-fargate-service.ts
        //
        const service = new ApplicationLoadBalancedFargateService(this, "customer-data-service", {
            cluster,
            memoryLimitMiB: 2048,
            cpu: 256,
            taskImageOptions: {
                image: ContainerImage.fromRegistry("270744187218.dkr.ecr.us-east-1.amazonaws.com/customerdata:latest"),
                containerPort: 8080,
                executionRole
            }
        })

        service.targetGroup.configureHealthCheck({
            path: '/actuator/health'
        })
    }
}
