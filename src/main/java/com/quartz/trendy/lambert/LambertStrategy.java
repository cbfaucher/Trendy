package com.quartz.trendy.lambert;

import com.quartz.trendy.calculator.CalculatorContext;
import com.quartz.trendy.calculator.GainOrLossCalculator;
import com.quartz.trendy.model.TickIndicator;
import com.quartz.trendy.spring.TrendyConfiguration;
import com.quartz.trendy.technical.CandlesticksHelper;
import com.quartz.trendy.technical.RSIHelper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LambertStrategy implements GainOrLossCalculator, RSIHelper, CandlesticksHelper {

    static final String ATT_RSI_OVERBOUGHT = "RSI.Overbought";
    static final String ATT_CRSI_OVERBOUGHT = "cRSI.Overbought";

    @Getter
    final private TrendyConfiguration configuration;

    @Override
    public TickIndicator isBuyPosition(CalculatorContext context) {

        val idx = context.getCurrentIndex();
        val tick = context.getCurrent();

        //  rsi
        val isOversold = isRsiOversold(tick);

        //  cRsi
        val isCRsiOversold = isCRsiOversold(tick);

        //  price in range (>= short EMA)
        val isPriceInRange = !isCandleBelow(tick, tick.getShortTermEMA(), false);

        if (isOversold && isCRsiOversold && isPriceInRange) {
            context.remove(ATT_RSI_OVERBOUGHT, ATT_CRSI_OVERBOUGHT);
            return new TickIndicator(TickIndicator.Action.Buy, context);
        } else {
            //  keep track of RSI/CRSI...
            if (isRsiOverbought(tick)) {
                context.set(ATT_RSI_OVERBOUGHT, true);
            }
            if (isCRsiOverbought(tick)) {
                context.set(ATT_CRSI_OVERBOUGHT, true);
            }

            return new TickIndicator(TickIndicator.Action.Neutral, context);
        }
    }

    @Override
    public TickIndicator isSellPosition(CalculatorContext context) {

        val idx = context.getCurrentIndex();
        val tick = context.getCurrent();

        //  todo - close <= stop loss price

        val rsiWasOverbouht = context.get(ATT_RSI_OVERBOUGHT, false);
        val crsiWasOverbouht = context.get(ATT_CRSI_OVERBOUGHT, false);

        if (rsiWasOverbouht && crsiWasOverbouht) {
            val ema9 = tick.getShortTermEMA();

            if (isRsiOverbought(tick) && isCRsiOverbought(tick) && isCandleAbove(tick, ema9, false)) {
                return new TickIndicator(TickIndicator.Action.Neutral, context);
            }

            //  RSI < 70
            if (!isRsiOverbought(tick)) {
                context.remove(ATT_RSI_OVERBOUGHT, ATT_CRSI_OVERBOUGHT);
                return new TickIndicator(TickIndicator.Action.Sell, context, "RSI is no longer overbought (%.3f)".formatted(tick.getRsi14()));
            }

            //  ...or cRSI < topline/70
            if (!isCRsiOverbought(tick)) {
                context.remove(ATT_RSI_OVERBOUGHT, ATT_CRSI_OVERBOUGHT);
                return new TickIndicator(TickIndicator.Action.Sell, context, "CRSI is no longer overbought (%.3f)".formatted(tick.getCRsi20()));
            }

            //  if close price crosses down the EMA9
            if (!isCandleAbove(tick, ema9, false)) {
                context.remove(ATT_RSI_OVERBOUGHT, ATT_CRSI_OVERBOUGHT);
                return new TickIndicator(TickIndicator.Action.Sell, context, "Close (%.02f) below EMA9 (%.02f)".formatted(tick.getClose(),
                                                                                                                          tick.getShortTermEMA()));
            }
        }

        return new TickIndicator(TickIndicator.Action.Neutral, context);
    }
}
