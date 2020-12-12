package com.quartz.trendy.technical;

import com.quartz.trendy.model.Tick;
import com.quartz.trendy.spring.TrendyConfiguration;
import lombok.Getter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RSIHelperTest implements RSIHelper {

    @Getter
    private final TrendyConfiguration configuration = new TrendyConfiguration();

    @BeforeEach
    void setUp() {
        configuration.getRsi().setOversoldLimit(30);
        configuration.getRsi().setOverboughtLimit(70);
    }

    @Test
    @SneakyThrows
    public void testIsOverSold() {

        assertFalse(isRsiOversold(new Tick().withRsi14(31D)));
        assertFalse(isRsiOversold(new Tick().withRsi14(71D)));
        assertTrue(isRsiOversold(new Tick().withRsi14(30D)));
        assertTrue(isRsiOversold(new Tick().withRsi14(29D)));
        assertTrue(isRsiOversold(new Tick().withRsi14(0D)));

        assertFalse(isCRsiOversold(new Tick().withCRsi20(31D)));
        assertFalse(isCRsiOversold(new Tick().withCRsi20(71D)));
        assertTrue(isCRsiOversold(new Tick().withCRsi20(30D)));
        assertTrue(isCRsiOversold(new Tick().withCRsi20(29D)));
        assertTrue(isCRsiOversold(new Tick().withCRsi20(0D)));

        //  CRSI with respect to lower band
        assertFalse(isCRsiOversold(new Tick().withCRsi20(41D).withCRsi20LowBand(40D)));
        assertTrue(isCRsiOversold(new Tick().withCRsi20(40D).withCRsi20LowBand(40D)));
        assertTrue(isCRsiOversold(new Tick().withCRsi20(39D).withCRsi20LowBand(40D)));
    }

    @Test
    @SneakyThrows
    public void testIsOverBought() {
        assertFalse(isRsiOverbought(new Tick().withRsi14(29D)));
        assertFalse(isRsiOverbought(new Tick().withRsi14(30D)));
        assertFalse(isRsiOverbought(new Tick().withRsi14(69D)));
        assertTrue(isRsiOverbought(new Tick().withRsi14(70D)));
        assertTrue(isRsiOverbought(new Tick().withRsi14(71D)));
        assertTrue(isRsiOverbought(new Tick().withRsi14(90D)));

        assertFalse(isCRsiOverbought(new Tick().withCRsi20(29D)));
        assertFalse(isCRsiOverbought(new Tick().withCRsi20(30D)));
        assertFalse(isCRsiOverbought(new Tick().withCRsi20(69D)));
        assertTrue(isCRsiOverbought(new Tick().withCRsi20(70D)));
        assertTrue(isCRsiOverbought(new Tick().withCRsi20(71D)));
        assertTrue(isCRsiOverbought(new Tick().withCRsi20(90D)));

        //  CRSI with respect to upper band
        assertFalse(isCRsiOverbought(new Tick().withCRsi20(59D).withCRsi20HighBand(60D)));
        assertTrue(isCRsiOverbought(new Tick().withCRsi20(60D).withCRsi20HighBand(60D)));
        assertTrue(isCRsiOverbought(new Tick().withCRsi20(61D).withCRsi20HighBand(60D)));
    }
}