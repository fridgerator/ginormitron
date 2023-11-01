import { Vpc } from "aws-cdk-lib/aws-ec2";
import { Cluster } from "aws-cdk-lib/aws-ecs";
import { ApplicationLoadBalancer } from "aws-cdk-lib/aws-elasticloadbalancingv2";
import { LogGroup } from "aws-cdk-lib/aws-logs";
import { StringParameter } from "aws-cdk-lib/aws-ssm";
import { CfnAccessLogSubscription, CfnAuthPolicy, CfnListener, CfnService, CfnServiceNetworkServiceAssociation, CfnServiceNetworkVpcAssociation, CfnTargetGroup } from "aws-cdk-lib/aws-vpclattice";
import { Construct } from "constructs";

export interface LatticeServiceProps {
    vpc: Vpc
    loadBalancer: ApplicationLoadBalancer
    serviceName: string
}

export class LatticeService extends Construct {
    domainName: string

    constructor(scope: Construct, id: string, props: LatticeServiceProps) {
        super(scope, id);

        const vpclTargetGroup = new CfnTargetGroup(this, "VpcLTargetGroup", {
            type: 'ALB',
            name: `${props.serviceName}-target-group`,
            config: {
                vpcIdentifier: props.vpc.vpcId,
                port: 80,
                protocol: 'HTTP',
                protocolVersion: 'HTTP1'
            },
            targets: [{
                id: props.loadBalancer.loadBalancerArn,
                port: 80
            }]
        })

        const vpclService = new CfnService(this, 'VpcLService', {
            authType: 'AWS_IAM',
            name: `${props.serviceName}-service`
        })

        new CfnAuthPolicy(this, 'VpcLServiceAuthPolicy', {
            policy: {
                "Statement": {
                    "Effect": "Allow",
                    "Principal": "*",
                    "Resource": "*",
                    "Action": "*"
                }
            },
            resourceIdentifier: vpclService.attrArn
        })

        const vplServiceLogGroup = new LogGroup(this, "VpcLServiceLG")

        new CfnAccessLogSubscription(this, "LogSub", {
            destinationArn: vplServiceLogGroup.logGroupArn,
            resourceIdentifier: vpclService.attrArn
        })

        new CfnListener(this, "Listener", {
            protocol: 'HTTP',
            port: 80,
            name: `${props.serviceName}-listener`,
            serviceIdentifier: vpclService.attrArn,
            defaultAction: {
                forward: {
                    targetGroups: [{
                        targetGroupIdentifier: vpclTargetGroup.attrId,
                        weight: 1
                    }]
                }
            }
        })

        const serviceNetworkIdentifier = StringParameter.fromStringParameterName(this, 'service-network-arn', 'service-network-arn').stringValue

        const serviceAssociation = new CfnServiceNetworkServiceAssociation(this, 'association', {
            serviceIdentifier: vpclService.attrArn,
            serviceNetworkIdentifier
        })

        new CfnServiceNetworkVpcAssociation(this, 'vpc-association', {
            serviceNetworkIdentifier: vpclService.attrArn,
            vpcIdentifier: props.vpc.vpcId
        })

        this.domainName = serviceAssociation.attrDnsEntryDomainName
    }
}
