package com.quartz.trendy.csv;

import lombok.With;
import lombok.val;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.function.Function;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@With
public class ColumnDictionary extends HashMap<String, ValueParser> {

    private final boolean timestampAsUtc;

    public ColumnDictionary(final boolean timestampAsUtc) {
        this.timestampAsUtc = timestampAsUtc;

        put("TIME", (s, t) -> t.withTimestamp(parseDateTime(s)));
    }

    public ColumnDictionary withHighOpenCloseLow() {
        put("OPEN", (s, t) -> t.withOpen(Double.parseDouble(s)));
        put("HIGH", (s, t) -> t.withHigh(Double.parseDouble(s)));
        put("LOW", (s, t) -> t.withLow(Double.parseDouble(s)));
        put("CLOSE", (s, t) -> t.withClose(Double.parseDouble(s)));

        return this;
    }

    public ColumnDictionary withShortTermEma(String ... names) {
        if (names.length == 0) {
            throw new IllegalArgumentException("No short-term EMA names provided");
        }

        for(val name: names) {
            put(name.toUpperCase(), (s, t) -> t.withShortTermEMA(ifNumber(s, Double::parseDouble)));
        }

        return this;
    }

    public ColumnDictionary withLongTermEma(String ... names) {
        if (names.length == 0) {
            throw new IllegalArgumentException("No long-term EMA names provided");
        }

        for(val name: names) {
            put(name.toUpperCase(), (s, t) -> t.withLongTermEMA(ifNumber(s, Double::parseDouble)));
        }

        return this;
    }

    public ColumnDictionary withVolume() {
        put("VOLUME", (s, t) -> t.withVolume(Long.parseLong(s)));
        put("VOLUME MA", (s, t) -> t.withAverageVolume(ifNumber(s, v -> Math.round(Double.parseDouble(v)))));

        return this;
    }

    public ColumnDictionary withRSI() {
        put("RSI", (s, t) -> t.withRsi14(ifNumber(s, Double::parseDouble)));

        return this;
    }

    public ColumnDictionary withCRSI() {
        put("LOWBAND", (s, t) -> t.withCRsi20LowBand(ifNumber(s, Double::parseDouble)));
        put("CRSI", (s, t) -> t.withCRsi20(ifNumber(s, Double::parseDouble)));
        put("HIGHBAND", (s, t) -> t.withCRsi20HighBand(ifNumber(s, Double::parseDouble)));

        return this;
    }

    public ColumnDictionary withVWAP() {
        put("VWAP", (s, t) -> t.withVwap(ifNumber(s, Double::parseDouble)));

        return this;
    }

    public ColumnDictionary withMACD() {
        put("MACD", (s, t) -> t.withMacd(ifNumber(s, Double::parseDouble)));
        put("SIGNAL", (s, t) -> t.withMacdSignal(ifNumber(s, Double::parseDouble)));

        return this;
    }

    protected LocalDateTime parseDateTime(String s) {
        val dt = LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));

        if (timestampAsUtc) {
            // convert to local date time
            return dt.atZone(ZoneId.of("UTC"))
                     .withZoneSameInstant(ZoneId.systemDefault())
                     .toLocalDateTime();
        } else {
            return dt;
        }
    }

    protected <T> T ifNumber(final String v, Function<String, T> fct) {
        if (isNotBlank(v) && !"nan".equalsIgnoreCase(v)) {
            return fct.apply(v);
        } else {
            return null;
        }
    }
}
