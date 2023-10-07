#!/bin/bash

aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 270744187218.dkr.ecr.us-east-1.amazonaws.com

docker build -t retailerdata .

docker tag retailerdata:latest 270744187218.dkr.ecr.us-east-1.amazonaws.com/retailerdata:latest

docker push 270744187218.dkr.ecr.us-east-1.amazonaws.com/retailerdata:latest
