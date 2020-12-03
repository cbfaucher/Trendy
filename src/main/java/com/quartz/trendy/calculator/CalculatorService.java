package com.quartz.trendy.calculator;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Api()
@RequestMapping("/calculator")
public class CalculatorService {

    @RequestMapping(value = "now", method = RequestMethod.GET)
    public String ping() {
        return "Now is " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }
}
