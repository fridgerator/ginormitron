## VPC Lattice

- ~~create another ec2 load balancing target group to the same ip as the already created one~~
- ~~create an internal alb to that target group~~
- ~~create vpc lattice tg, register target to internal alb~~
- ~~create a vpc lattice service~~
  - create a log group for logging
  - define routing
    - http:80 - default action "customer-tg" weight 1
- service network
  - service association to vpc lattice service
    - this is where the domain name is - customer-service-09428636bc23cc426.7d67968.vpc-lattice-svcs.us-east-1.on.aws
  - add vpc association, vpc where web app runs
  - auth "none" for now
  - log group

- from retailer stack output dns name of http://retail-retai-jisd0cevi2sk-694043456.us-east-1.elb.amazonaws.com/api/v1/customers-created-count