#!/usr/bin/env bash

if [ ! -f "opentelemetry-javaagent.jar" ]; then
    echo Downloading opentelemetry java agent
    wget --quiet https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar
fi

OTEL_EXPORTER_OTLP_ENDPOINT="http://localhost:4317"
OTEL_RESOURCE_ATTRIBUTES=service.name=transactiondata
./gradlew bootrun
