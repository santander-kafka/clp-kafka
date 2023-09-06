package com.clp.kafka.demo.productstandard.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.RecordTooLargeException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.listener.AfterRollbackProcessor;
import org.springframework.kafka.listener.DefaultAfterRollbackProcessor;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableKafka
@Slf4j
public class KafkaConfiguration {

//    @Bean
//    AfterRollbackProcessor<Object, Object> arp() {
//        DefaultAfterRollbackProcessor darp = new DefaultAfterRollbackProcessor<>((rec, ex) -> {
//            log.info("Exception  during event processing key: {}, value {}, topic {}", rec.key(), rec.value(), rec.topic(), ex);
//        }, new FixedBackOff(3000L, 3));
//
//        darp.addNotRetryableExceptions(Exception.class);
//
//        return darp;
//    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        return new DefaultErrorHandler((record, exception) -> {
            // SEND MESSAGE
            log.info("Exception during event processing key: {}, value {}, topic {}", record.key(), record.value(), record.topic(), exception);
        }, new FixedBackOff(10000, 2));
    }
}