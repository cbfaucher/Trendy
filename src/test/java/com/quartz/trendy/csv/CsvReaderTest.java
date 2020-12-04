package com.quartz.trendy.csv;

import com.quartz.trendy.model.Tick;
import com.quartz.trendy.spring.TrendyConfiguration;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CsvReaderTest implements ColumnDictionary {

    private final TrendyConfiguration configuration = new TrendyConfiguration();
    private final CsvReader reader = new CsvReader(configuration);

    @Test
    public void splitValues_simple() {
        val line = "CLOSE;EMA;VOLUME";

        val actual = reader.splitValues(line);
        assertEquals(3, actual.size());
        assertEquals(Arrays.asList("CLOSE", "EMA", "VOLUME"), actual);
    }

    @Test
    public void splitValues_wrapper() {
        val line = "\"CLOSE\";EMA;\"VOLUME\"";

        val actual = reader.splitValues(line);
        assertEquals(3, actual.size());
        assertEquals(Arrays.asList("CLOSE", "EMA", "VOLUME"), actual);
    }

    @Test
    @SneakyThrows
    public void recognizeCsvFormat_valid() {
        val line = "open;HiGh;low;\"CLOSE\";EMA9;\"VOLUME\"";

        @Cleanup
        val in = new BufferedReader(new StringReader(line));

        val readers = reader.recognizeCsvFormat(in);
        assertEquals(6, readers.size());

        assertSame(columnDictionary.get("OPEN"), readers.get(0));
        assertSame(columnDictionary.get("HIGH"), readers.get(1));
        assertSame(columnDictionary.get("LOW"), readers.get(2));
        assertSame(columnDictionary.get("CLOSE"), readers.get(3));
        assertSame(columnDictionary.get("EMA9"), readers.get(4));
        assertSame(columnDictionary.get("VOLUME"), readers.get(5));
    }

    @Test
    @SneakyThrows
    public void recognizeCsvFormat_unknownColumn() {
        val line = "open;BLERG;\"VOLUME\"";

        @Cleanup
        val in = new BufferedReader(new StringReader(line));

        try {
            reader.recognizeCsvFormat(in);
        } catch (InvalidCsvFormatException e) {
            //ok
        }
    }

    @Test
    @SneakyThrows
    public void loadTradingViewCsv_success() {
        val src = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/csv/sample.csv")));

        val ticker = reader.loadTradingViewCsv("ABC", src);

        assertEquals("ABC", ticker.getTicker());
        assertNotNull(ticker.getImportedDateTime());

        assertEquals(3, ticker.getTicks().size());

        /*  2020-10-14T14:00:00Z;6.355;6.44;212000
            2020-10-14T15:00:00Z;6.45;6.60;205700
            2020-10-14T16:00:00Z;7.01;6.75;153400 */

        val expected = Arrays.asList(
                Tick.builder().timestamp(LocalDateTime.of(2020, 10, 14, 14, 0, 0))
                        .open(6.355D)
                        .close(6.44D)
                        .volume(212000L)
                        .build(),
                Tick.builder().timestamp(LocalDateTime.of(2020, 10, 14, 15, 0, 0))
                        .open(6.45D)
                        .close(6.60D)
                        .volume(205700L)
                        .build(),
                Tick.builder().timestamp(LocalDateTime.of(2020, 10, 14, 16, 0, 0))
                        .open(7.01D)
                        .close(6.75D)
                        .volume(153400L)
                        .build());
        assertEquals(expected, ticker.getTicks());
    }
}