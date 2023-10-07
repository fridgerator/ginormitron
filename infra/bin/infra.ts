#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { MskStack } from '../lib/msk-stack';
import { CustomerDataStack } from '../lib/customer-data-stack';
import { RetailerDataStack } from '../lib/retailer-data-stack';

const app = new cdk.App();
const props = {
  env: {
    region: process.env.AWS_REGION,
    account: process.env.AWS_ACCOUNT,
  },
};

// new MskStack(app, "MskStack", props);
new CustomerDataStack(app, "CustomerDataStack", props);
new RetailerDataStack(app, "RetailerDataStack", props);
