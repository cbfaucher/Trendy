package com.quartz.trendy.tools;

import lombok.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
@ToString
public class PriceInfo {

    public static PriceInfo createFrom(final double price, final int quantity, final double fees, final double tolerancePercent) {
        val total = price * quantity + fees;

        val breakEventPrice = total / quantity;

        val percent = (100f - tolerancePercent) / 100f;
        val stopLossPrice = price * percent;

        return new PriceInfo(breakEventPrice, stopLossPrice);
    }

    final private String breakEventPrice;

    final private String stopLossPrice;

    private PriceInfo(final double breakEventPrice, final double stopLossPrice) {
        this("%.2f".formatted(breakEventPrice), "%.2f".formatted(stopLossPrice));
    }
}
