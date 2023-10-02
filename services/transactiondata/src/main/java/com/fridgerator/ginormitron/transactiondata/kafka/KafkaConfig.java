package com.fridgerator.ginormitron.transactiondata.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin.NewTopics;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.fridgerator.ginormitron.transactiondata.entities.Customer;
import com.fridgerator.ginormitron.transactiondata.entities.Retailer;
import com.fridgerator.ginormitron.transactiondata.entities.Transaction;

@Configuration
public class KafkaConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    String consumerGroup;

    @Value("${kafka-topics.names.transactions}")
    private String transactionsTopic;

    @Value("${kafka-topics.replicaCount}")
    int replicaCount;

    @Value("${kafka-topics.partitionCount}")
    int partitionCount;

    @Bean
    public NewTopics topics() {
        return new NewTopics(
            TopicBuilder.name(transactionsTopic)
                .partitions(partitionCount)
                .replicas(replicaCount)
                .build()
        );
    }

    @Bean
    public ProducerFactory<String, Transaction> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Transaction> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.fridgerator.ginormitron.customerdata.model,com.fridgerator.ginormitron.retailerdata.model");
        props.put(JsonDeserializer.TYPE_MAPPINGS, "com.fridgerator.ginormitron.customerdata.model.Customer:com.fridgerator.ginormitron.transactiondata.entities.Customer, com.fridgerator.ginormitron.retailerdata.model.Retailer:com.fridgerator.ginormitron.transactiondata.entities.Retailer");
        // props.put(JsonDeserializer.TYPE_MAPPINGS, "com.fridgerator.ginormitron.retailerdata.model.Retailer:com.fridgerator.ginormitron.transactiondata.entities.Retailer");

        return props;
    }

    @Bean
    public ConsumerFactory<String, Customer> customerConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Customer> customerKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Customer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(customerConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Retailer> retailerConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Retailer> retailerKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Retailer> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(retailerConsumerFactory());
        return factory;
    }
}
