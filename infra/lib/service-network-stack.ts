import { CfnOutput, Stack, StackProps } from "aws-cdk-lib";
import { Vpc } from "aws-cdk-lib/aws-ec2";
import { StringParameter } from "aws-cdk-lib/aws-ssm";
import { CfnServiceNetwork } from "aws-cdk-lib/aws-vpclattice";
import { Construct } from "constructs";

export class ServiceNetworkStack extends Stack {
    props: StackProps

    constructor(scope: Construct, id: string, props: StackProps) {
        super(scope, id, props);
        this.props = props;
        this.build();
    }

    build () {
        const sn = new CfnServiceNetwork(this, 'service-network', {
            authType: 'NONE',
            name: 'main-service-network'
        })

        new StringParameter(this, 'ServiceNetworkArn', {
            stringValue: sn.attrArn,
            parameterName: 'service-network-arn'
        })
    }
}
