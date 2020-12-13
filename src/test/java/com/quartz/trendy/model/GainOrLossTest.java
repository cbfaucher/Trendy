package com.quartz.trendy.model;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GainOrLossTest {

    @Test
    @SneakyThrows
    public void test_noTxns() {
        val gainOrLoss = new GainOrLoss(Collections.emptyList());

        assertEquals(0, gainOrLoss.getNumberOfTrades());
        assertEquals(0, gainOrLoss.getNumberOfPositiveTrades());
        assertEquals(0, gainOrLoss.getNumberOfNegativeTrades());
        assertEquals("%0.0", gainOrLoss.getPercentageTransactions());

        assertEquals("$0.00", gainOrLoss.getGainLoss());

        assertEquals(0, gainOrLoss.getTotalNumberOfDays());
        assertEquals("%0.0", gainOrLoss.getAverageNumberOfDays());
    }

    @Test
    @SneakyThrows
    public void test_withTxns() {
        val gainOrLoss = new GainOrLoss(Arrays.asList(
                new Transaction(100,
                                LocalDateTime.of(2020, 11, 5, 8, 0, 0), 10.00D,
                                LocalDateTime.of(2020, 11, 8, 8, 0, 0), 10.50D,
                                ""),
                new Transaction(100,
                                LocalDateTime.of(2020, 11, 10, 8, 0, 0), 11.00D,
                                LocalDateTime.of(2020, 11, 10, 8, 0, 0), 10.75D,
                                ""),
                new Transaction(100,
                                LocalDateTime.of(2020, 11, 15, 8, 0, 0), 9.50D,
                                LocalDateTime.of(2020, 11, 22, 8, 0, 0), 10.25D,
                                "")));

        assertEquals(3, gainOrLoss.getNumberOfTrades());
        assertEquals(2, gainOrLoss.getNumberOfPositiveTrades());
        assertEquals(1, gainOrLoss.getNumberOfNegativeTrades());
        assertEquals("%67", gainOrLoss.getPercentageTransactions());

        assertEquals("$100.00", gainOrLoss.getGainLoss());

        assertEquals(10, gainOrLoss.getTotalNumberOfDays());
        assertEquals("3.3", gainOrLoss.getAverageNumberOfDays());
    }
}
