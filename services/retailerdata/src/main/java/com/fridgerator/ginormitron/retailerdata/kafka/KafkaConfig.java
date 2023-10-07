package com.fridgerator.ginormitron.retailerdata.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.fridgerator.ginormitron.retailerdata.model.Retailer;

// @Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka-topics.names.retailers}")
    private String retailersTopic;

    @Value("${kafka-topics.replicaCount}")
    int replicaCount;

    @Value("${kafka-topics.partitionCount}")
    int partitionCount;

    @Bean
    public NewTopics topics() {
        System.out.println("retailers topic : " + retailersTopic);
        return new NewTopics(
            TopicBuilder.name(retailersTopic)
                .partitions(partitionCount)
                .replicas(replicaCount)
                .build()
        );
    }

    @Bean
    public ProducerFactory<String, Retailer> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Retailer> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
