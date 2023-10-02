#!/usr/bin/env node
import 'source-map-support/register';
import * as cdk from 'aws-cdk-lib';
import { MskStack } from '../lib/msk-stack';

const app = new cdk.App();
const props = {
  env: {
    region: process.env.AWS_REGION,
    account: process.env.AWS_ACCOUNT,
  },
};

new MskStack(app, "MskStack", props);
