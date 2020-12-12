package com.quartz.trendy;

import com.quartz.trendy.model.GainOrLoss;
import com.quartz.trendy.model.Ticker;

public interface GainOrLossCalculator {
    GainOrLoss calculate(Ticker ticker, int quantity);
}
