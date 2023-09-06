package com.clp.kafka.demo.profitability.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class EventConfig {

    @Bean
    public NewTopic topicExample() {
        return TopicBuilder.name("CLP_ORDER_PROFITABILITY_ERROR")
                .partitions(3)
                .replicas(2)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, "1")
                .build();
    }
}
