package com.quartz.trendy.tools;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriceInfoTest {

    @Test
    @SneakyThrows
    public void testCreateFrom() {

        val actual = PriceInfo.createFrom(20.46D, 200, 50D, 1.0D);

        assertEquals("20.71", actual.getBreakEventPrice());
        assertEquals("20.26", actual.getStopLossPrice());
    }

}