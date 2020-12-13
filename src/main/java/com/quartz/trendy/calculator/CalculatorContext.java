package com.quartz.trendy.calculator;

import com.quartz.trendy.model.Tick;
import com.quartz.trendy.model.Ticker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.HashMap;
import java.util.Map;

/**
 * Information about current tick being examined
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class CalculatorContext {

    private final Map<String, Object> attributes = new HashMap<>();

    private final Ticker ticker;

    private Tick lastBuy;
    private int lastBuyIndex = -1;

    private Tick current;
    private int currentIndex;

    public CalculatorContext withLastBuy(final Tick tick, final int idx) {
        lastBuy = tick;
        lastBuyIndex = idx;
        return this;
    }

    void clearLastBuy() {
        withLastBuy(null, -1);
    }

    public CalculatorContext withCurrent(final Tick tick, final int idx) {
        current = tick;
        currentIndex = idx;
        return this;
    }

    public CalculatorContext set(final String name, final Object value) {
        attributes.put(name, value);

        return this;
    }

    public <T> T get(final String name) {
        return (T) attributes.get(name);
    }

    public <T> T get(final String name, final T defaultValue) {
        val v = get(name);
        return v != null ? (T) attributes.get(name) : defaultValue;
    }

    public CalculatorContext remove(final String ... names) {
        for (val name: names) {
            attributes.remove(name);
        }

        return this;
    }
}
