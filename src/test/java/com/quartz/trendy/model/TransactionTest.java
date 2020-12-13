package com.quartz.trendy.model;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @Test
    @SneakyThrows
    public void testTxnSameDay() {
        val txn = new Transaction(200,
                                  LocalDateTime.of(2020, 11, 10, 8, 0, 0), 11.00D,
                                  LocalDateTime.of(2020, 11, 10, 8, 0, 0), 10.75D,
                                  "");
        assertEquals(-50D, txn.calculateGainOrLoss());
        assertEquals(0, txn.calculateNumberOfDays());
    }

    @Test
    @SneakyThrows
    public void testTxnDifferentDay() {
        val txn = new Transaction(100,
                                  LocalDateTime.of(2020, 11, 15, 8, 0, 0), 9.50D,
                                  LocalDateTime.of(2020, 11, 22, 8, 0, 0), 10.25D,
                                  "");
        assertEquals(75D, txn.calculateGainOrLoss());
        assertEquals(7, txn.calculateNumberOfDays());
    }

}