#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { MskStack } from '../lib/msk-stack';
import { CustomerDataStack } from '../lib/customer-data-stack';
import { RetailerDataStack } from '../lib/retailer-data-stack';
import { ServiceNetworkStack } from '../lib/service-network-stack'

const app = new cdk.App();
const props = {
  env: {
    region: process.env.AWS_REGION,
    account: process.env.AWS_ACCOUNT,
  },
};

const serviceNetworkStack = new ServiceNetworkStack(app, "ServiceNetworkStack", props);
// new MskStack(app, "MskStack", props);
const customerDataStack = new CustomerDataStack(app, "CustomerDataStack", props);
customerDataStack.addDependency(serviceNetworkStack)

const retailerDataStack = new RetailerDataStack(app, "RetailerDataStack", {
  ...props,
  customerDataUrl: customerDataStack.domainName
});
retailerDataStack.addDependency(serviceNetworkStack)
retailerDataStack.addDependency(customerDataStack)
