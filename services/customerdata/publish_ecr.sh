#!/bin/bash

aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 270744187218.dkr.ecr.us-east-1.amazonaws.com

docker build -t customerdata .

docker tag customerdata:latest 270744187218.dkr.ecr.us-east-1.amazonaws.com/customerdata:latest

docker push 270744187218.dkr.ecr.us-east-1.amazonaws.com/customerdata:latest
