package com.quartz.trendy.csv;

import com.quartz.trendy.model.Tick;

@FunctionalInterface
public interface ValueParser {

    Tick parse(final String value, final Tick from);
}
