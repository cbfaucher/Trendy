package com.quartz.trendy.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@With
@SuperBuilder
@ToString
public class Tick {
    private LocalDateTime timestamp = null;

    private double open = 0;
    private double high = 0;
    private double low = 0;
    private double close = 0;

    private long volume = 0;
    private Long averageVolume = null;

    private Double shortTermEMA = null;
    private Double longTermEMA = null;

    private Double vwap = null;

    private Double rsi14 = null;

    @Builder.Default
    private double cRsi20LowBand = 0.0D;
    private Double cRsi20 = null;

    @Builder.Default
    private double cRsi20HighBand = 100.0D;

    private Double macd = null;
    private Double macdSignal = null;

    //  todo: validation
}
