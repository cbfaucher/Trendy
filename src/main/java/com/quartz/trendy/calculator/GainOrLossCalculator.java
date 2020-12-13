package com.quartz.trendy.calculator;

import com.quartz.trendy.model.*;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

/**
 * Main logic for transaction calculations.  Actual what is BUY, what is SELL is provided by subclass, but
 * processing remains the same.
 */
public interface GainOrLossCalculator {

    //  todo: this can be pushed up
    default GainOrLoss calculate(Ticker ticker, int quantity) {

        val indicators = computeIndicators(ticker);
        val transactions = computeLongTransactions(quantity, indicators);

        return new GainOrLoss(transactions);
    }

    default List<TickIndicator> computeIndicators(final Ticker ticker) {

        val positions = new ArrayList<TickIndicator>();

        val context = new CalculatorContext(ticker);

        for (int i = 0; i < ticker.getTicks().size(); i++) {

            val tick = ticker.getTicks().get(i);

            context.withCurrent(tick, i);

            TickIndicator pos = isBuyPosition(context);

            switch (pos.action) {
                case Buy -> context.withLastBuy(pos.tick, i);
                default -> {
                    pos = isSellPosition(context);
                    if (pos.action == TickIndicator.Action.Sell) {
                        context.clearLastBuy();
                    }
                }
            }

            positions.add(pos);
        }
        return positions;
    }

    default List<Transaction> computeLongTransactions(final int quantity, final List<TickIndicator> indicators) {

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

    TickIndicator isBuyPosition(final CalculatorContext context);

    TickIndicator isSellPosition(final CalculatorContext context);
}
