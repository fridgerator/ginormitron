import { Stack, StackProps} from "aws-cdk-lib";
import { Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster } from "aws-cdk-lib/aws-ecs";
import { Construct } from "constructs";
import { LatticeService } from "./shared/lattice-service";
import { EcsService } from "./shared/ecs-service";

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

        const retailerEcsService = new EcsService(this, 'retailer-ecs-service', {
            cluster,
            registry: '270744187218.dkr.ecr.us-east-1.amazonaws.com/retailerdata:latest',
            serviceName: 'retailer-data',
            publicAlb: true,
        })

        new LatticeService(this, 'retailer-lattice-service', {
            vpc,
            loadBalancer: retailerEcsService.latticeLoadBalancer,
            serviceName: 'retailer-data'
        })

        // https://github.com/aws/aws-cdk/blob/main/packages/aws-cdk-lib/aws-ecs-patterns/lib/fargate/application-load-balanced-fargate-service.ts
        //
        // const service = new ApplicationLoadBalancedFargateService(this, "retailer-data-service", {
        //     cluster,
        //     memoryLimitMiB: 2048,
        //     cpu: 256,
        //     taskImageOptions: {
        //         image: ContainerImage.fromRegistry("270744187218.dkr.ecr.us-east-1.amazonaws.com/retailerdata:latest"),
        //         containerPort: 8080,
        //         executionRole,
        //         environment: {
        //             SPRING_PROFILES_ACTIVE: "aws"
        //         }
        //     }
        // })

        // service.targetGroup.configureHealthCheck({
        //     path: '/actuator/health'
        // })
    }
}
