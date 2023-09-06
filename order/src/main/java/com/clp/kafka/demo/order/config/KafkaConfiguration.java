package com.clp.kafka.demo.order.config;


import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaConfiguration {
    @Autowired
    private final KafkaProperties kafkaProperties;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return new KafkaAdmin(kafkaAdminProperties());
    }

    @Bean
    public AdminClient adminClient() {
        return AdminClient.create(kafkaAdminProperties());
    }

    @Bean
    public Map<String, Object> kafkaAdminProperties() {
        return Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers()
        );
    }
}