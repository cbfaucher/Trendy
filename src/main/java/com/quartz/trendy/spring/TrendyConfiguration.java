package com.quartz.trendy.spring;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "trendy")
@NoArgsConstructor
@Getter
@Setter
public class TrendyConfiguration {

    private final CsvConfiguration csv = new CsvConfiguration();

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class CsvConfiguration {
        private char valueSeparator = ';';
    }
}
