package com.quartz.trendy.csv;

import com.google.common.annotations.VisibleForTesting;
import com.quartz.trendy.model.Tick;
import com.quartz.trendy.model.Ticker;
import com.quartz.trendy.spring.TrendyConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Dictionary;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class CsvReader implements ColumnDictionary {

    private final Pattern wrappedValue = Pattern.compile("^\"(.*)\"$");

    private final TrendyConfiguration configuration;

    public Ticker loadTradingViewCsv(final String tickerId, final File file) throws FileNotFoundException, IOException {
        try (val reader = new BufferedReader(new FileReader(file))) {
            return loadTradingViewCsv(tickerId, reader);
        }
    }

    public Ticker loadTradingViewCsv(final String tickerId, final BufferedReader reader) throws FileNotFoundException, IOException {

        val ticker = new Ticker(tickerId);

        val valueReaders = recognizeCsvFormat(reader);

        String line;
        while ((line = reader.readLine()) != null) {
            val values = splitValues(line);

            if (values.size() != valueReaders.size()) {
                throw new InvalidCsvFormatException("# values different of # columns");
            }

            Tick tick = new Tick();
            for (int i = 0; i < values.size(); i++) {
                tick = valueReaders.get(i).parse(values.get(i), tick);
            }

            ticker.add(tick);
        }

        return ticker;
    }

    @VisibleForTesting
    List<ValueParser> recognizeCsvFormat(BufferedReader reader) throws IOException {

        return splitValues(reader.readLine()).stream()
                .map(s -> {
                    val parser = columnDictionary.get(s.toUpperCase());
                    if (parser == null) {
                        throw new InvalidCsvFormatException(String.format("Column '%s' is unknown.", s));
                    }
                    return parser;
                }).collect(Collectors.toList());
    }

    @VisibleForTesting
    List<String> splitValues(final String line) {

        return Stream.of(line.split("" + configuration.getCsv().getValueSeparator()))
                .map(s -> {
                    val matcher = wrappedValue.matcher(s);
                    return matcher.matches() ? matcher.group(1) : s;
                })
                .collect(Collectors.toList());
    }
}
