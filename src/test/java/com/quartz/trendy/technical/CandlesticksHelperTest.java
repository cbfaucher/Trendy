package com.quartz.trendy.technical;

import com.quartz.trendy.model.Tick;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CandlesticksHelperTest implements CandlesticksHelper {

    @Test
    @SneakyThrows
    public void testIsCandleAround() {
        val tick = new Tick().withOpen(10.01D)
                              .withHigh(12D)
                              .withLow(9.50)
                              .withClose(11D);
        assertFalse(isCandleAround(tick, 10D, false));
        assertTrue(isCandleAround(tick, 10.50, false));
        assertTrue(isCandleAround(tick, 10D, true));

        //  candle completely above
        assertFalse(isCandleAround(tick, 9D, false));
        assertFalse(isCandleAround(tick, 9D, true));

        //  candle completely below
        assertFalse(isCandleAround(tick, 13D, false));
        assertFalse(isCandleAround(tick, 13D, true));
    }

    @Test
    @SneakyThrows
    public void testIsCandleAbove() {
        val tick = new Tick().withOpen(10.00D)
                             .withHigh(12D)
                             .withLow(9D)
                             .withClose(11D);

        assertTrue(isCandleAbove(tick, 9.9D, false));
        assertFalse(isCandleAbove(tick, 9.5D, true));
    }

    @Test
    @SneakyThrows
    public void testIsCandleBelow() {
        val tick = new Tick().withOpen(10.00D)
                             .withHigh(12D)
                             .withLow(9D)
                             .withClose(11D);

        assertTrue(isCandleBelow(tick, 11.1D, false));
        assertFalse(isCandleAbove(tick, 11.1D, true));
    }


}