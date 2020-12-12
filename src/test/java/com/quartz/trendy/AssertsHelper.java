package com.quartz.trendy;

import com.quartz.trendy.model.TickIndicator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public interface AssertsHelper {

    default void assertBuy(TickIndicator position) {
        assertNotNull(position);
        assertEquals(TickIndicator.Action.Buy, position.action);
    }

    default void assertNeutral(TickIndicator position) {
        assertNotNull(position);
        assertEquals(TickIndicator.Action.Neutral, position.action);
    }

    default void assertSell(TickIndicator position) {
        assertNotNull(position);
        assertEquals(TickIndicator.Action.Sell, position.action);
    }
}
