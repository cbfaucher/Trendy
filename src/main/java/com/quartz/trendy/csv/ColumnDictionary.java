package com.quartz.trendy.csv;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public interface ColumnDictionary {

    Map<String, ValueParser> columnDictionary = new HashMap<>() {
        {
            put("TIME", (s, t) -> t.withTimestamp(LocalDateTime.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))));

            put("OPEN", (s, t) -> t.withOpen(Double.parseDouble(s)));
            put("HIGH", (s, t) -> t.withHigh(Double.parseDouble(s)));
            put("LOW", (s, t) -> t.withLow(Double.parseDouble(s)));
            put("CLOSE", (s, t) -> t.withClose(Double.parseDouble(s)));

            put("VOLUME", (s, t) -> t.withVolume(Long.parseLong(s)));
            put("VOLUME MA", (s, t) -> t.withAverageVolume(Long.parseLong(s)));

            put("EMA9", (s, t) -> t.withEma9(Double.parseDouble(s)));
            put("EMA50", (s, t) -> t.withEma50(Double.parseDouble(s)));

            put("VWAP", (s, t) -> t.withVwap(Double.parseDouble(s)));
            put("RSI", (s, t) -> t.withRsi14(Double.parseDouble(s)));

            put("LOWBAND", (s, t) -> t.withCRsi20LowBand(Double.parseDouble(s)));
            put("CRSI", (s, t) -> t.withCRsi20(Double.parseDouble(s)));
            put("HIGHBAND", (s, t) -> t.withCRsi20HighBand(Double.parseDouble(s)));

            put("HISTOGRAM", (s, t) -> t);  //ignore for now

            put("MACD", (s, t) -> t.withMacd(Double.parseDouble(s)));
            put("SIGNAL", (s, t) -> t.withMacdSignal(Double.parseDouble(s)));
        }
    };
}
