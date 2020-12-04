package com.quartz.trendy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@With
@EqualsAndHashCode
public class Transaction {

    private final int quantity;

    private final LocalDateTime boughtDt;
    private final double boughtAt;

    private final LocalDateTime soldDt;
    private final double soldAt;
}
