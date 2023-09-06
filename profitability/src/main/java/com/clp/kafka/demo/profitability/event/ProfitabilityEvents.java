package com.clp.kafka.demo.profitability.event;

import com.example.eventcommons.event.global.ClpEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.kafka.streams.kstream.Reducer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class ProfitabilityEvents implements Reducer {
    private List<ClpEvent> events = new ArrayList<>();

    public ProfitabilityEvents onEvent(ClpEvent event) {
        events.removeIf(e -> Objects.equals(event.getId(), e.getId()));
        events.add(event);
        return this;
    }

    @Override
    public List<Object> apply(Object value1, Object value2) {
        return List.of(value1, value2);
    }
}
