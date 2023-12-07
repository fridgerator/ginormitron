## Services

![services](ginormitron_services.png)

## Multi-VPC infrastructure

1. ECS Fargate services running spring boot apps
2. VPC Lattice for inter service communication
3. MSK availalbe via private link (WIP)

![ginormitron](ginormitron.drawio.png)

## Local Development

1. Follow the [steps to run SigNoz with docker compose](https://signoz.io/docs/install/docker/#install-signoz-using-docker-compose)
  - Follow the steps to [remove the SigNoz sample application](https://signoz.io/docs/operate/docker-standalone/#remove-the-sample-application-from-signoz-dashboard)
2. Run SigNoz docker compose
3. Run docker compose from root directory to start kafka and mongodb
4. [Download the opentelemetry java agent](https://signoz.io/docs/instrumentation/springboot/#steps-to-auto-instrument-spring-boot-applications-for-traces) into each service directory
5. Build each service `./gradlew build`
6. Run each service using the following command `OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4317" OTEL_RESOURCE_ATTRIBUTES=service.name=<app_name> java -javaagent:/path/to/opentelemetry-javaagent.jar -jar  <myapp>.jar`
  - Replace <app_name> and <myapp> with service name and location to built jar
