package com.quartz.trendy.lambert;

import com.quartz.trendy.model.Tick;
import com.quartz.trendy.model.TickIndicator;
import com.quartz.trendy.model.Ticker;
import com.quartz.trendy.spring.TrendyConfiguration;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LambertCalculatorTest {

    private final TrendyConfiguration configuration = new TrendyConfiguration();
    private final LambertCalculator calculator = new LambertCalculator(configuration);

    @Test
    @SneakyThrows
    public void testIsBuyPosition_rsiNotOversold() {
        val ticker = new Ticker("TEST");
        val tick = new Tick().withOpen(9D)
                             .withClose(10D)
                             .withEma9(9.5D)
                             .withRsi14(29D)
                             .withCRsi20LowBand(35)
                             .withCRsi20(34D);

        assertBuy(calculator.isBuyPosition(ticker, tick, 3));

        assertNeutral(calculator.isBuyPosition(ticker, tick.withRsi14(69D), 3));
        assertNeutral(calculator.isBuyPosition(ticker, tick.withRsi14(36D), 3));
        assertNeutral(calculator.isBuyPosition(ticker, tick.withEma9(10.01D), 3));
    }

    @Test
    @SneakyThrows
    public void testIsBuyPosition_crsiNotOversold() {
        val ticker = new Ticker("TEST");
        val tick = new Tick().withOpen(9.75D)
                             .withClose(10D)
                             .withEma9(9.5D)
                             .withRsi14(71D)
                             .withCRsi20HighBand(55)
                             .withCRsi20(56D);

        assertNeutral(calculator.isSellPosition(ticker, tick, 3));

        assertSell(calculator.isSellPosition(ticker, tick.withRsi14(69D), 3));
        assertSell(calculator.isSellPosition(ticker, tick.withCRsi20(54D), 3));
        assertSell(calculator.isSellPosition(ticker, tick.withClose(9.45D), 3));
    }

    private void assertBuy(TickIndicator position) {
        assertNotNull(position);
        assertEquals(TickIndicator.Action.Buy, position.action);
    }

    private void assertNeutral(TickIndicator position) {
        assertNotNull(position);
        assertEquals(TickIndicator.Action.Neutral, position.action);
    }

    private void assertSell(TickIndicator position) {
        assertNotNull(position);
        assertEquals(TickIndicator.Action.Sell, position.action);
    }
}