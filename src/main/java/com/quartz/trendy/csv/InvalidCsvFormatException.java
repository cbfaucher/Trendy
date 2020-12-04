package com.quartz.trendy.csv;

public class InvalidCsvFormatException extends RuntimeException {
    public InvalidCsvFormatException(String msg) {
        super(msg);
    }
}
