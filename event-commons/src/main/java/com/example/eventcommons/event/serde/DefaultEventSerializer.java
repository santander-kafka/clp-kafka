package com.example.eventcommons.event.serde;

import com.example.eventcommons.event.global.ClpEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

public class DefaultEventSerializer implements Serializer<ClpEvent> {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Override
    public byte[] serialize(String topic, ClpEvent data) {
        return toBytes(data);
    }

    @Override
    public byte[] serialize(String topic, Headers headers, ClpEvent data) {
        return toBytes(data);
    }

    @SneakyThrows
    public static byte[] toBytes(Object event) {
        return MAPPER.writeValueAsBytes(event);
    }
}
