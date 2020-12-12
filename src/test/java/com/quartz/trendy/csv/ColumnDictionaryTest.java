package com.quartz.trendy.csv;

import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ColumnDictionaryTest {

    private final ColumnDictionary dictionary = new ColumnDictionary(true);

    @Test
    @SneakyThrows
    public void testListAllZoneIds() {
        val availableZoneIds = ZoneId.getAvailableZoneIds();
        System.out.println("ZoneIDs:\n" + availableZoneIds
                                                .stream()
                                                .sorted()
                                                .collect(Collectors.joining("\n")));

        assertTrue(availableZoneIds.contains("UTC"));
    }

    @Test
    @SneakyThrows
    public void testparseDateTime_isUtc() {
        val utc = "2020-12-10T15:36:30Z";
        val expected = LocalDateTime.of(2020, 12, 10,
                                        10 /*-5 hours*/, 36, 30);
        val actual = dictionary.parseDateTime(utc);
        assertEquals(expected, actual);
    }

    @Test
    @SneakyThrows
    public void testParseDateTime_notUtc() {
        val dic = dictionary.withTimestampAsUtc(false);

        val utc = "2020-12-10T15:36:30Z";
        val expected = LocalDateTime.of(2020, 12, 10,
                                        15 /*same*/, 36, 30);
        val actual = dic.parseDateTime(utc);
        assertEquals(expected, actual);
    }

}