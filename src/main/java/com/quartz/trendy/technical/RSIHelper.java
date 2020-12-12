package com.quartz.trendy.technical;

import com.quartz.trendy.model.Tick;
import com.quartz.trendy.spring.TrendyConfiguration;

import java.util.Optional;

public interface RSIHelper {

    TrendyConfiguration getConfiguration();

    default double getRsiOversoldLimit() {
        return getConfiguration().getRsi().getOversoldLimit();
    }

    default double getRsiOverboughtLimit() {
        return getConfiguration().getRsi().getOverboughtLimit();
    }

    default boolean isRsiOversold(final Tick tick) {
        return Optional.ofNullable(tick.getRsi14())
                       .map(s -> s <= getRsiOversoldLimit())
                       .orElse(false);
    }

    default boolean isRsiOverbought(final Tick tick) {
        return Optional.ofNullable(tick.getRsi14())
                       .map(s -> s >= getRsiOverboughtLimit())
                       .orElse(false);
    }

    default boolean isCRsiOversold(final Tick tick) {
        return Optional.ofNullable(tick.getCRsi20())
                       .map(s -> s <= getRsiOversoldLimit()
                                 || s <= tick.getCRsi20LowBand())
                       .orElse(false);
    }

    default boolean isCRsiOverbought(final Tick tick) {
        return Optional.ofNullable(tick.getCRsi20())
                       .map(s -> s >= getRsiOverboughtLimit()
                                 || s >= tick.getCRsi20HighBand())
                       .orElse(false);
    }

}
