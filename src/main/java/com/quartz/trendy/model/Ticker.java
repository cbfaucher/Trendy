package com.quartz.trendy.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@With
public class Ticker {

    private final String ticker;

    private final LocalDateTime importedDateTime;

    private final List<Tick> ticks = new ArrayList<>();

    public void add(final Tick tick) {
        ticks.add(tick);
    }
}
