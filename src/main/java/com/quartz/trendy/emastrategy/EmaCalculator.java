package com.quartz.trendy.emastrategy;

import com.quartz.trendy.calculator.GainOrLossCalculator;
import com.quartz.trendy.model.Tick;
import com.quartz.trendy.model.TickIndicator;
import com.quartz.trendy.model.Ticker;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class EmaCalculator implements GainOrLossCalculator {

    @Override
    public TickIndicator isBuyPosition(Ticker ticker, Tick tick, int idx) {

        if (idx == 0) {
            return new TickIndicator(TickIndicator.Action.Neutral, tick, idx);
        }

        if (tick.getShortTermEMA() < tick.getLongTermEMA()) {
            return new TickIndicator(TickIndicator.Action.Neutral, tick, idx);
        }

        //  at this point, we know short EMA >= long EMA
        //  check if previous tick was Short EMA < Long EMA
        val previous = ticker.getTicks().get(idx - 1);
        val isBelow = previous.getShortTermEMA() < previous.getLongTermEMA();

        return new TickIndicator(isBelow ? TickIndicator.Action.Buy : TickIndicator.Action.Neutral,
                                 tick, idx);
    }

    @Override
    public TickIndicator isSellPosition(Ticker ticker, Tick tick, int idx) {

        if (idx == 0) {
            return new TickIndicator(TickIndicator.Action.Neutral, tick, idx);
        }

        if (tick.getShortTermEMA() > tick.getLongTermEMA()) {
            return new TickIndicator(TickIndicator.Action.Neutral, tick, idx);
        }

        //  at this point, we know short EMA <= long EMA
        //  check if previous tick's short EMA was > long EMA
        val previous = ticker.getTicks().get(idx - 1);
        val isBelow = previous.getShortTermEMA() > previous.getLongTermEMA();

        return new TickIndicator(isBelow ? TickIndicator.Action.Sell : TickIndicator.Action.Neutral,
                                 tick, idx);
    }
}
