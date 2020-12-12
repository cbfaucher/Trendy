package com.quartz.trendy.technical;

import com.quartz.trendy.model.Tick;
import lombok.val;

import java.util.Optional;

public interface CandlesticksHelper {

    default boolean isCandleAround(final Tick tick, final Double value, final boolean useHighLow) {

        val highValue = useHighLow ? tick.getHigh() : Math.max(tick.getOpen(), tick.getClose());
        val lowValue = useHighLow ? tick.getLow() : Math.min(tick.getOpen(), tick.getClose());

        return Optional.ofNullable(value)
                       .map(v -> highValue >= v && lowValue <= v)
                       .orElse(false);
    }

    default boolean isCandleAbove(final Tick tick, final Double value, final boolean useHighLow) {

        val lowValue = useHighLow ? tick.getLow() : Math.min(tick.getOpen(), tick.getClose());

        return Optional.ofNullable(value)
                       .map(v -> lowValue > v)
                       .orElse(false);
    }

    default boolean isCandleBelow(final Tick tick, final Double value, final boolean useHighLow) {

        val highValue = useHighLow ? tick.getHigh() : Math.max(tick.getOpen(), tick.getClose());

        return Optional.ofNullable(value)
                       .map(v -> highValue < v)
                       .orElse(false);
    }
}
