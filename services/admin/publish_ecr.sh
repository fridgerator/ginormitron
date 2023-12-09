#!/bin/bash

aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 270744187218.dkr.ecr.us-east-1.amazonaws.com

docker build -t admin .

docker tag admin:latest 270744187218.dkr.ecr.us-east-1.amazonaws.com/admin:latest

docker push 270744187218.dkr.ecr.us-east-1.amazonaws.com/admin:latest
