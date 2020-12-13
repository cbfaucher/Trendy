package com.quartz.trendy.model;

import lombok.*;

import java.util.List;

@Getter
@EqualsAndHashCode
public class GainOrLoss {


    private final int numberOfTrades;
    private final int numberOfPositiveTrades;
    private final int numberOfNegativeTrades;

    private final String gainLoss;

    private final int totalNumberOfDays;
    private final String averageNumberOfDays;

    private final List<Transaction> transactions;

    public GainOrLoss(@NonNull final List<Transaction> transactions) {
        this.transactions = transactions;

        this.numberOfTrades = transactions.size();

        double gains = 0.0D;
        int positive = 0;
        int negative = 0;

        int numberOfDays = 0;

        for (val txn : transactions) {
            val gain = txn.calculateGainOrLoss();
            gains += gain;

            if (gain >= 0D) {
                positive += 1;
            }
            if (gain < 0D) {
                negative += 1;
            }

            numberOfDays += txn.calculateNumberOfDays();
        }

        numberOfPositiveTrades = positive;
        numberOfNegativeTrades = negative;
        gainLoss = "$%.2f".formatted(gains);

        totalNumberOfDays = numberOfDays;
        averageNumberOfDays =
                numberOfTrades > 0
                ? "%.1f".formatted(1F * totalNumberOfDays / numberOfTrades)
                : "%0.0";
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public String getPercentageTransactions() {
        return numberOfTrades > 0
               ? "%%%.0f".formatted(100.0F * numberOfPositiveTrades / numberOfTrades)
               : "%0.0";
    }
}
