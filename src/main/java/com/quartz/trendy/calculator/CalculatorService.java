package com.quartz.trendy.calculator;

import com.quartz.trendy.GainOrLossCalculator;
import com.quartz.trendy.lambert.LambertCalculator;
import com.quartz.trendy.model.GainOrLoss;
import com.quartz.trendy.model.Ticker;
import io.swagger.annotations.Api;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@Api()
@RequestMapping("/calculator")
@Validated
public class CalculatorService {

    @Autowired
    @Qualifier("lambertCalculator")
    private GainOrLossCalculator lambertCalculator;

    @RequestMapping(value = "now", method = RequestMethod.GET)
    public String ping() {
        return "Now is " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @RequestMapping(value = "lambert/{ticker}", method = RequestMethod.GET)
    public GainOrLoss calculateGainOrLossForLambert(@PathVariable("ticker") @Valid @NotNull @Size(min=1, max=4) final String tickerId,
                                                    @RequestParam("csvpath") @Valid @NotNull @Size(min = 1) final String csvFilename) {

        val ticker = new Ticker(tickerId, LocalDateTime.now());
        val csvFile = checkFile(csvFilename);

        return lambertCalculator.calculate(ticker, csvFile);
    }

    private File checkFile(final String csvFilename) {

        val file = new File(csvFilename);

        if (!file.exists() || !file.canRead()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "File not found/unreadable: " + file.getAbsolutePath());
        }

        return file;
    }
}
