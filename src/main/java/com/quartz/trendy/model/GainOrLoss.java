package com.quartz.trendy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.With;

import java.util.List;

@RequiredArgsConstructor
@Getter
@With
@EqualsAndHashCode
public class GainOrLoss {

    private final List<Transaction> transactions;
}
