package com.example.eventcommons.event.serde;

import com.example.eventcommons.event.global.ClpEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

public class DefaultEventSerde implements Serde<ClpEvent> {

    @Override
    public Serializer<ClpEvent> serializer() {
        return new DefaultEventSerializer();
    }

    @Override
    public Deserializer<ClpEvent> deserializer() {
        return new DefaultEventDeserializer();
    }
}
