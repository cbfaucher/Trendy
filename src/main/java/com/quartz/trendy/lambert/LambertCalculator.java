package com.quartz.trendy.lambert;

import com.quartz.trendy.GainOrLossCalculator;
import com.quartz.trendy.model.GainOrLoss;
import com.quartz.trendy.model.Ticker;
import org.springframework.stereotype.Component;

import java.io.File;

@Component("lambertCalculator")
public class LambertCalculator implements GainOrLossCalculator {

    @Override
    public GainOrLoss calculate(Ticker ticker, File csvFile) {
        return null;
    }
}
