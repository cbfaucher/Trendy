package com.quartz.trendy.lambert;

import com.quartz.trendy.AssertsHelper;
import com.quartz.trendy.calculator.CalculatorContext;
import com.quartz.trendy.model.Tick;
import com.quartz.trendy.model.Ticker;
import com.quartz.trendy.spring.TrendyConfiguration;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class LambertStrategyTest implements AssertsHelper {

    private final TrendyConfiguration configuration = new TrendyConfiguration();
    private final LambertStrategy calculator = new LambertStrategy(configuration);

    @Test
    @SneakyThrows
    public void testIsBuyPosition_rsiOversold() {
        val ticker = new Ticker("TEST");

        val context = new CalculatorContext(ticker);

        val tick = new Tick().withOpen(9D)
                             .withClose(10D)
                             .withShortTermEMA(9.5D)
                             .withRsi14(29D)
                             .withCRsi20LowBand(35)
                             .withCRsi20(34D);

        assertBuy(calculator.isBuyPosition(context.withCurrent(tick, 3)));

        assertNeutral(calculator.isBuyPosition(context.withCurrent(tick.withRsi14(69D), 3)));
        assertNeutral(calculator.isBuyPosition(context.withCurrent(tick.withRsi14(36D), 3)));
        assertNeutral(calculator.isBuyPosition(context.withCurrent(tick.withShortTermEMA(10.01D), 3)));

        assertNull(context.get(LambertStrategy.ATT_RSI_OVERBOUGHT));
        assertNull(context.get(LambertStrategy.ATT_CRSI_OVERBOUGHT));
    }

    @Test
    @SneakyThrows
    public void testIsSellPosition_preconditionMet_rsiIsSell() {
        val ticker = new Ticker("TEST");

        val context = new CalculatorContext(ticker);
        context.set(LambertStrategy.ATT_RSI_OVERBOUGHT, true)
               .set(LambertStrategy.ATT_CRSI_OVERBOUGHT, true);

        val tick = new Tick().withOpen(9.75D)
                             .withClose(10D)
                             .withShortTermEMA(9.5D)
                             .withRsi14(71D)
                             .withCRsi20HighBand(55)
                             .withCRsi20(56D);

        //  still in oversold comfortably
        assertNeutral(calculator.isSellPosition(context.withCurrent(tick, 3)));

        assertSell(calculator.isSellPosition(context.withCurrent(tick.withRsi14(69D), 3)));

        assertNull((Boolean)context.get(LambertStrategy.ATT_RSI_OVERBOUGHT));
        assertNull((Boolean)context.get(LambertStrategy.ATT_CRSI_OVERBOUGHT));
    }

    @Test
    @SneakyThrows
    public void testIsSellPosition_preconditionMet_crsiIsSell() {
        val ticker = new Ticker("TEST");

        val context = new CalculatorContext(ticker);
        context.set(LambertStrategy.ATT_RSI_OVERBOUGHT, true)
               .set(LambertStrategy.ATT_CRSI_OVERBOUGHT, true);

        val tick = new Tick().withOpen(9.75D)
                             .withClose(10D)
                             .withShortTermEMA(9.5D)
                             .withRsi14(71D)
                             .withCRsi20HighBand(55)
                             .withCRsi20(56D);

        //  still in oversold comfortably
        assertNeutral(calculator.isSellPosition(context.withCurrent(tick, 3)));

        assertSell(calculator.isSellPosition(context.withCurrent(tick.withCRsi20(54D), 3)));

        assertNull((Boolean)context.get(LambertStrategy.ATT_RSI_OVERBOUGHT));
        assertNull((Boolean)context.get(LambertStrategy.ATT_CRSI_OVERBOUGHT));
    }

    @Test
    @SneakyThrows
    public void testIsSellPosition_preconditionMet_closeBelowEma() {
        val ticker = new Ticker("TEST");

        val context = new CalculatorContext(ticker);
        context.set(LambertStrategy.ATT_RSI_OVERBOUGHT, true)
               .set(LambertStrategy.ATT_CRSI_OVERBOUGHT, true);

        val tick = new Tick().withOpen(9.75D)
                             .withClose(10D)
                             .withShortTermEMA(9.5D)
                             .withRsi14(71D)
                             .withCRsi20HighBand(55)
                             .withCRsi20(56D);

        //  still in oversold comfortably
        assertNeutral(calculator.isSellPosition(context.withCurrent(tick, 3)));

        assertSell(calculator.isSellPosition(context.withCurrent(tick.withClose(9.45D), 3)));

        assertNull((Boolean)context.get(LambertStrategy.ATT_RSI_OVERBOUGHT));
        assertNull((Boolean)context.get(LambertStrategy.ATT_CRSI_OVERBOUGHT));
    }

    @Test
    @SneakyThrows
    public void testIsSellPosition_preconditionNotMet() {
        val ticker = new Ticker("TEST");

        val context = new CalculatorContext(ticker);

        assertNull((Boolean)context.get(LambertStrategy.ATT_RSI_OVERBOUGHT));
        assertNull((Boolean)context.get(LambertStrategy.ATT_CRSI_OVERBOUGHT));

        val tick = new Tick().withOpen(9.75D)
                             .withClose(10D)
                             .withShortTermEMA(9.5D)
                             .withRsi14(50D)
                             .withCRsi20HighBand(55)
                             .withCRsi20(30D);

        assertNeutral(calculator.isSellPosition(context.withCurrent(tick, 3)));
    }
}