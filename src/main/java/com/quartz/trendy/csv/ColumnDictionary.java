package com.quartz.trendy.csv;

import com.quartz.trendy.spring.TrendyConfiguration;
import lombok.NonNull;
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

    public ColumnDictionary(@NonNull final TrendyConfiguration configuration) {
        this(configuration.getCsv().isDateTimeInUtc());
    }

    public ColumnDictionary(final boolean timestampAsUtc) {
        this.timestampAsUtc = timestampAsUtc;

        put("TIME", (s, t) -> t.withTimestamp(parseDateTime(s)));

        put("OPEN", (s, t) -> t.withOpen(Double.parseDouble(s)));
        put("HIGH", (s, t) -> t.withHigh(Double.parseDouble(s)));
        put("LOW", (s, t) -> t.withLow(Double.parseDouble(s)));
        put("CLOSE", (s, t) -> t.withClose(Double.parseDouble(s)));

        put("VOLUME", (s, t) -> t.withVolume(Long.parseLong(s)));
        put("VOLUME MA", (s, t) -> t.withAverageVolume(ifNumber(s, v -> Math.round(Double.parseDouble(v)))));

        put("EMA9", (s, t) -> t.withEma9(ifNumber(s, Double::parseDouble)));
        put("EMA50", (s, t) -> t.withEma50(ifNumber(s, Double::parseDouble)));

        put("VWAP", (s, t) -> t.withVwap(ifNumber(s, Double::parseDouble)));
        put("RSI", (s, t) -> t.withRsi14(ifNumber(s, Double::parseDouble)));

        put("LOWBAND", (s, t) -> t.withCRsi20LowBand(ifNumber(s, Double::parseDouble)));
        put("CRSI", (s, t) -> t.withCRsi20(ifNumber(s, Double::parseDouble)));
        put("HIGHBAND", (s, t) -> t.withCRsi20HighBand(ifNumber(s, Double::parseDouble)));

        put("HISTOGRAM", (s, t) -> t);  //ignore for now

        put("MACD", (s, t) -> t.withMacd(ifNumber(s, Double::parseDouble)));
        put("SIGNAL", (s, t) -> t.withMacdSignal(ifNumber(s, Double::parseDouble)));
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
