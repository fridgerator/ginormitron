import { CfnOutput, RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
import { InstanceType, Vpc } from "aws-cdk-lib/aws-ec2";
import { AwsLogDriver, Cluster, ContainerImage, FargateService, FargateTaskDefinition, Protocol } from "aws-cdk-lib/aws-ecs";
import { ApplicationLoadBalancedFargateService } from "aws-cdk-lib/aws-ecs-patterns";
import { Effect, PolicyStatement, Role, ServicePrincipal } from "aws-cdk-lib/aws-iam";
import { Construct } from "constructs";

export class RetailerDataStack extends Stack {
    props: StackProps

    constructor(scope: Construct, id: string, props: StackProps) {
        super(scope, id, props);
        this.props = props;
        this.build();
    }

    build() {
        const vpc = new Vpc(this, "retailer-data-vpc", {
            maxAzs: 2,
            vpcName: "retailer-data-vpc"
        })

        const cluster = new Cluster(this, "retailer-data-cluster", {
            vpc
        })

        const executionRole = new Role(this, "execution-role", {
            roleName: "retailer-data-execution-role",
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

        cluster.addCapacity("DefaultAutoScalingGroupCapacity", {
            instanceType: new InstanceType("t2.large")
        })

        const taskDefinition = new FargateTaskDefinition(this, "retailer-data-taskdef", {
            memoryLimitMiB: 2048,
            executionRole
        });

        const appContainer = taskDefinition.addContainer("retailer-data", {
            image: ContainerImage.fromRegistry("270744187218.dkr.ecr.us-east-1.amazonaws.com/retailerdata:latest"),
            memoryLimitMiB: 2048,
            logging: new AwsLogDriver({ streamPrefix: 'retailer-data' }),
            environment: {
                SPRING_PROFILES_ACTIVE: "aws"
            }
        })

        appContainer.addPortMappings({
            containerPort: 8080
        })

        const service = new FargateService(this, "retailer-data-service", {
            cluster,
            taskDefinition
        })

        // https://github.com/aws/aws-cdk/blob/main/packages/aws-cdk-lib/aws-ecs-patterns/lib/fargate/application-load-balanced-fargate-service.ts
        //
        // const service = new ApplicationLoadBalancedFargateService(this, "retailer-data-service", {
        //     cluster,
        //     memoryLimitMiB: 2048,
        //     cpu: 512,
        //     taskImageOptions: {
        //         image: ContainerImage.fromRegistry("270744187218.dkr.ecr.us-east-1.amazonaws.com/retailerdata:latest"),
        //         containerPort: 8080,
        //         executionRole
        //     }
        // })
    }
}
