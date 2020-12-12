package com.quartz.trendy.lambert;

import com.quartz.trendy.calculator.GainOrLossCalculator;
import com.quartz.trendy.model.Tick;
import com.quartz.trendy.model.TickIndicator;
import com.quartz.trendy.model.Ticker;
import com.quartz.trendy.spring.TrendyConfiguration;
import com.quartz.trendy.technical.CandlesticksHelper;
import com.quartz.trendy.technical.RSIHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("lambertCalculator")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LambertCalculator implements GainOrLossCalculator, RSIHelper, CandlesticksHelper {

    @Getter
    final private TrendyConfiguration configuration;

    @Override
    public TickIndicator isBuyPosition(final Ticker ticker,
                                       final Tick tick,
                                       final int idx) {

        //  rsi
        val isOversold = isRsiOversold(tick);

        //  cRsi
        val isCRsiOversold = isCRsiOversold(tick);

        //  price in range (>= short EMA)
        val isPriceInRange = !isCandleBelow(tick, tick.getEma9(), false);

        return new TickIndicator(isOversold && isCRsiOversold && isPriceInRange
                                 ? TickIndicator.Action.Buy
                                 : TickIndicator.Action.Neutral,
                                 tick,
                                 idx);
    }

    @Override
    public TickIndicator isSellPosition(final Ticker ticker,
                                        final Tick tick,
                                        final int idx) {
        val ema9 = tick.getEma9();

        if (isRsiOverbought(tick) && isCRsiOverbought(tick) && isCandleAbove(tick, ema9, false)) {
            return new TickIndicator(TickIndicator.Action.Neutral, tick, idx);
        }

        //  RSI < 70
        if (!isRsiOverbought(tick)) {
            return new TickIndicator(TickIndicator.Action.Sell, tick, idx, "RSI is no longer overbought (%.3f)".formatted(tick.getRsi14()));
        }

        //  ...or cRSI < topline/70
        if (!isCRsiOverbought(tick)) {
            return new TickIndicator(TickIndicator.Action.Sell, tick, idx, "CRSI is no longer overbought (%.3f)".formatted(tick.getCRsi20()));
        }

        //  if close price crosses down the EMA9
        if (!isCandleAbove(tick, ema9, false)) {
            return new TickIndicator(TickIndicator.Action.Sell, tick, idx, "Close (%.02f) below EMA9 (%.02f)".formatted(tick.getClose(),
                                                                                                                        tick.getEma9()));
        }

        //  todo - close < 95% buy.close

        return new TickIndicator(TickIndicator.Action.Neutral, tick, idx);
    }
}
