package com.quartz.trendy.emastrategy;

import com.quartz.trendy.AssertsHelper;
import com.quartz.trendy.calculator.CalculatorContext;
import com.quartz.trendy.model.Tick;
import com.quartz.trendy.model.Ticker;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

class EmaStrategyTest implements AssertsHelper {

    private final EmaStrategy calculator = new EmaStrategy();

    @Test
    @SneakyThrows
    public void testIsBuyPosition() {

        val ticker = createTicker();

        val context = new CalculatorContext(ticker);

        assertNeutral(calculator.isBuyPosition(context.withCurrent(ticker.getTicks().get(0), 0)));
        assertBuy(calculator.isBuyPosition(context.withCurrent(ticker.getTicks().get(1), 1)));
        assertNeutral(calculator.isBuyPosition(context.withCurrent(ticker.getTicks().get(2), 2)));
        assertNeutral(calculator.isBuyPosition(context.withCurrent(ticker.getTicks().get(3), 3)));
        assertNeutral(calculator.isBuyPosition(context.withCurrent(ticker.getTicks().get(4), 4)));
    }

    @Test
    @SneakyThrows
    public void testIsSellPosition() {
        val ticker = createTicker();

        val context = new CalculatorContext(ticker);

        assertNeutral(calculator.isSellPosition(context.withCurrent(ticker.getTicks().get(0), 0)));
        assertNeutral(calculator.isSellPosition(context.withCurrent(ticker.getTicks().get(1), 1)));
        assertNeutral(calculator.isSellPosition(context.withCurrent(ticker.getTicks().get(2), 2)));
        assertSell(calculator.isSellPosition(context.withCurrent(ticker.getTicks().get(3), 3)));
        assertNeutral(calculator.isSellPosition(context.withCurrent(ticker.getTicks().get(4), 4)));
    }


    private Ticker createTicker() {
        val ticker = new Ticker("AAA");

        ticker.add(new Tick().withShortTermEMA(1.98D).withLongTermEMA(2D));
        ticker.add(new Tick().withShortTermEMA(2.01D).withLongTermEMA(2D));
        ticker.add(new Tick().withShortTermEMA(2.5D).withLongTermEMA(2D));
        ticker.add(new Tick().withShortTermEMA(1.99D).withLongTermEMA(2D));
        ticker.add(new Tick().withShortTermEMA(1.95D).withLongTermEMA(2D));

        return ticker;
    }

}