package com.quartz.trendy.lambert;

import com.quartz.trendy.GainOrLossCalculator;
import com.quartz.trendy.model.*;
import com.quartz.trendy.spring.TrendyConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component("lambertCalculator")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LambertCalculator implements GainOrLossCalculator {

    final private TrendyConfiguration configuration;

    //  todo: this can be pushed up
    @Override
    public GainOrLoss calculate(Ticker ticker, int quantity) {

        val indicators = computeIndicators(ticker);
        val transactions = computeTransactions(quantity, indicators);

        return new GainOrLoss(transactions);
    }

    private List<TickIndicator> computeIndicators(final Ticker ticker) {

        val positions = new ArrayList<TickIndicator>();
        for (int i = 0; i < ticker.getTicks().size(); i++) {

            val tick = ticker.getTicks().get(i);
            TickIndicator pos = isBuyPosition(ticker, tick, i);
            if (pos.action != TickIndicator.Action.Buy) {
                pos = isSellPosition(ticker, tick, i);
            }

            positions.add(pos);
        }
        return positions;
    }

    private TickIndicator isBuyPosition(final Ticker ticker,
                                        final Tick tick,
                                        final int idx) {

        //  rsi
        val isOversold = isRsiOversold(tick);

        //  cRsi
        val isCRsiOversold = isCRsiOversold(tick);

        //  price in range
        val isPriceInRange = isPriceAround(tick, tick.getEma9()) || isPriceAbove(tick, tick.getEma9());

        return new TickIndicator(isOversold && isCRsiOversold && isPriceInRange
                                 ? TickIndicator.Action.Buy
                                 : TickIndicator.Action.Neutral,
                                 tick,
                                 idx);
    }

    private List<Transaction> computeTransactions(final int quantity, final List<TickIndicator> indicators) {

        val txns = new ArrayList<Transaction>();

        int idx = 0;
        while (idx < indicators.size()) {

            //  find next buy
            while (idx < indicators.size() && indicators.get(idx).action != TickIndicator.Action.Buy) {
                idx += 1;
            }

            //  go to last consecutive 'buy'
            while (idx < indicators.size() && indicators.get(idx).action == TickIndicator.Action.Buy) {
                idx += 1;
            }
            idx -= 1;   //one too far

            val buyAt = indicators.get(idx).tick;

            //  find next 'sell'
            while (idx < indicators.size() && indicators.get(idx).action != TickIndicator.Action.Sell) {
                idx += 1;
            }

            if (idx < indicators.size() && indicators.get(idx).action == TickIndicator.Action.Sell) {
                txns.add(new Transaction(quantity,
                                         buyAt,
                                         indicators.get(idx)));

                idx += 1;
            }
        }

        return txns;
    }

    private TickIndicator isSellPosition(final Ticker ticker,
                                         final Tick tick,
                                         final int idx) {
        if (isRsiOverbought(tick) && isCRsiOverbought(tick)) {
            return new TickIndicator(TickIndicator.Action.Sell, tick, idx, String.format("RSI >= %s / cRSI Overbought",
                                                                                         configuration.getRsi().getOverbought()));
        }

        val ema9 = tick.getEma9();
        if (ema9 == null) {
            return new TickIndicator(TickIndicator.Action.Neutral, tick, idx);
        }

        if (tick.getClose() < ema9) {
            return new TickIndicator(TickIndicator.Action.Sell, tick, idx, String.format("Close (%.02f) below EMA9 (%.02f)",
                                                                                         tick.getClose(),
                                                                                         tick.getEma9()));
        }

        //  todo - close < 95% buy.close

        return new TickIndicator(TickIndicator.Action.Neutral, tick, idx);
    }

    private boolean isRsiOversold(final Tick tick) {
        return Optional.ofNullable(tick.getRsi14())
                       .map(s -> s <= (double) configuration.getRsi().getOversold())
                       .orElse(false);
    }

    private boolean isRsiOverbought(final Tick tick) {
        return Optional.ofNullable(tick.getRsi14())
                       .map(s -> s >= (double) configuration.getRsi().getOverbought())
                       .orElse(false);
    }

    private boolean isCRsiOversold(final Tick tick) {
        return Optional.ofNullable(tick.getCRsi20())
                       .map(s -> s < (double) configuration.getRsi().getOversold()
                                 || s <= tick.getCRsi20LowBand())
                       .orElse(false);
    }

    private boolean isCRsiOverbought(final Tick tick) {
        return Optional.ofNullable(tick.getCRsi20())
                       .map(s -> s >= (double) configuration.getRsi().getOverbought()
                                 || s >= tick.getCRsi20HighBand())
                       .orElse(false);
    }

    private boolean isPriceAround(final Tick tick, final Double ema9) {
        return Optional.ofNullable(ema9)
                       .map(v -> tick.getHigh() >= v && tick.getLow() <= v)
                       .orElse(false);
    }

    private boolean isPriceAbove(final Tick tick, final Double ema9) {
        return Optional.ofNullable(ema9)
                       .map(v -> tick.getClose() >= v)
                       .orElse(false);
    }

}
