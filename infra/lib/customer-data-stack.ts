import { CfnOutput, RemovalPolicy, Stack, StackProps } from "aws-cdk-lib";
import { InstanceType, Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster, ContainerImage, FargateService, FargateTaskDefinition } from "aws-cdk-lib/aws-ecs";
import { ApplicationLoadBalancedFargateService } from "aws-cdk-lib/aws-ecs-patterns";
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

        // cluster.addCapacity("DefaultAutoScalingGroupCapacity", {
        //     instanceType: new InstanceType("t2.large")
        // })

        // const taskDefinition = new FargateTaskDefinition(this, "customer-data-taskdef", {
        //     memoryLimitMiB: 2048
        // });

        // taskDefinition.addContainer("customer-data", {
        //     image: ContainerImage.fromRegistry("270744187218.dkr.ecr.us-east-1.amazonaws.com/customerdata:latest"),
        //     memoryLimitMiB: 2048
        // })

        // const service = new FargateService(this, "customer-data-service", {
        //     cluster,
        //     taskDefinition
        // })

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

        const service = new ApplicationLoadBalancedFargateService(this, "customer-data-service", {
            cluster,
            memoryLimitMiB: 2048,
            cpu: 512,
            taskImageOptions: {
                image: ContainerImage.fromRegistry("270744187218.dkr.ecr.us-east-1.amazonaws.com/customerdata:latest"),
                containerPort: 8080,
                executionRole
            }
        })

        // service.taskDefinition.addToTaskRolePolicy(new PolicyStatement({
        //     effect: Effect.ALLOW,
        //     actions: [
        //         "ecr:BatchGetImage",
        //         "ecr:GetDownloadUrlForLayer",
        //         "ecr:GetAuthorizationToken",
        //         "secretsmanager:*"
        //     ],
        //     resources: ["*"]
        // }))
    }
}
