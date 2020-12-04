package com.quartz.trendy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@With
public class Tick {
    final private LocalDateTime timestamp;

    final private double open;
    final private double high;
    final private double low;
    final private double close;

    final private double shortEma;
    final private double midEma;
    final private double longEma;

    final private double vwap;

    final private double rsi14;

    final private double cRsi20;
}
