package com.quartz.trendy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@With
@EqualsAndHashCode
public class Transaction {

    private final int quantity;

    private final LocalDateTime boughtDt;
    private final double boughtAt;

    private final LocalDateTime soldDt;
    private final double soldAt;

    private final String sellReason;

    public Transaction(final int quantity, final Tick buyAt, final TickIndicator sellAt) {
        this.quantity = quantity;

        this.boughtDt = buyAt.getTimestamp();
        this.boughtAt = buyAt.getClose();

        this.soldDt = sellAt.tick.getTimestamp();
        this.soldAt = sellAt.tick.getClose();
        this.sellReason = sellAt.reason;
    }
}
