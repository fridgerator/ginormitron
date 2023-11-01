import { Stack, StackProps } from "aws-cdk-lib";
import { Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster } from "aws-cdk-lib/aws-ecs";
import { Construct } from "constructs";
import { EcsService } from "./shared/ecs-service";
import { LatticeService } from "./shared/lattice-service";

export class CustomerDataStack extends Stack {
    props: StackProps
    domainName: string

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

        const customerEcsService = new EcsService(this, 'customer-ecs-service', {
            cluster,
            registry: '270744187218.dkr.ecr.us-east-1.amazonaws.com/customerdata:latest',
            serviceName: 'customer-data',
            publicAlb: false
        })

        const latticeService = new LatticeService(this, 'customer-lattice-service', {
            vpc,
            loadBalancer: customerEcsService.latticeLoadBalancer,
            serviceName: 'customer-data'
        })

        this.domainName = latticeService.domainName

        // https://github.com/aws/aws-cdk/blob/main/packages/aws-cdk-lib/aws-ecs-patterns/lib/fargate/application-load-balanced-fargate-service.ts
        //
        // const service = new ApplicationLoadBalancedFargateService(this, "customer-data-service", {
        //     cluster,
        //     memoryLimitMiB: 2048,
        //     cpu: 256,
        //     taskImageOptions: {
        //         image: ContainerImage.fromRegistry("270744187218.dkr.ecr.us-east-1.amazonaws.com/customerdata:latest"),
        //         containerPort: 8080,
        //         executionRole
        //     }
        // })

        // service.targetGroup.configureHealthCheck({
        //     path: '/actuator/health'
        // })
    }
}
