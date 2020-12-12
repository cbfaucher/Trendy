package com.quartz.trendy.calculator;

import com.quartz.trendy.model.Tick;
import com.quartz.trendy.model.TickIndicator;
import com.quartz.trendy.model.Ticker;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GainOrLossCalculatorTest implements GainOrLossCalculator {

    private final Set<Integer> buyPositions = new HashSet<>();
    private final Set<Integer> sellPositions = new HashSet<>();

    @Override
    public TickIndicator isBuyPosition(Ticker ticker, Tick tick, int idx) {
        return new TickIndicator(buyPositions.contains(idx) ? TickIndicator.Action.Buy : TickIndicator.Action.Neutral,
                                 tick,
                                 idx);
    }

    @Override
    public TickIndicator isSellPosition(Ticker ticker, Tick tick, int idx) {
        return new TickIndicator(sellPositions.contains(idx) ? TickIndicator.Action.Sell : TickIndicator.Action.Neutral,
                                 tick,
                                 idx);
    }

    @Test
    @SneakyThrows
    public void testCalculate() {
        val ticker = createTicker(20);

        //  expect 4 & 10 as first txn
        buyPositions.add(3);
        buyPositions.add(4);
        buyPositions.add(6);

        sellPositions.add(10);
        sellPositions.add(11);

        //  expect 15 & 17
        sellPositions.add(14);

        buyPositions.add(15);
        sellPositions.add(17);

        //  buy without sell -> incomplet
        buyPositions.add(19);

        //  when
        val gains = calculate(ticker, 100);

        //  then
        assertEquals(2, gains.getTransactions().size());

        //  1st txn
        assertEquals(5D /*idx +1*/, gains.getTransactions().get(0).getBoughtAt());
        assertEquals(11D, gains.getTransactions().get(0).getSoldAt());

        //  2nd txn
        assertEquals(16D, gains.getTransactions().get(1).getBoughtAt());
        assertEquals(18D, gains.getTransactions().get(1).getSoldAt());
    }

    private Ticker createTicker(int nbTicks) {
        val ticker = new Ticker("AAA");

        for (int i = 1; i <= nbTicks; i++) {
            ticker.add(new Tick().withClose(i)
                                 .withTimestamp(LocalDateTime.now()));
        }

        return ticker;
    }

}