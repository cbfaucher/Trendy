package com.quartz.trendy;

import com.quartz.trendy.model.GainOrLoss;
import com.quartz.trendy.model.Ticker;

import java.io.File;

public interface GainOrLossCalculator {
    GainOrLoss calculate(Ticker ticker, File csvFile);
}
