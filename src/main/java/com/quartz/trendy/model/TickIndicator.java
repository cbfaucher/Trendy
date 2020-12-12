package com.quartz.trendy.model;

import lombok.*;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
@With
public class TickIndicator {

    public enum Action { Buy, Neutral, Sell }

    public final Action action;

    public final Tick tick;

    public final int index;

    public final String reason;

    public TickIndicator(Action action, Tick tick, int index) {
        this.action = action;
        this.tick = tick;
        this.index = index;
        this.reason = null;
    }
}
