package com.quartz.trendy.calculator;

import com.quartz.trendy.csv.ColumnDictionary;
import com.quartz.trendy.csv.CsvReader;
import com.quartz.trendy.model.GainOrLoss;
import com.quartz.trendy.model.Ticker;
import io.swagger.annotations.Api;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.IOException;
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

    @Autowired
    @Qualifier("emaCalculator")
    private GainOrLossCalculator emaCrossingCalculator;

    @RequestMapping(value = "now", method = RequestMethod.GET)
    public String ping() {
        return "Now is " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @RequestMapping(value = "lambert/{ticker}/quantity/{quantity}", method = RequestMethod.GET)
    public GainOrLoss calculateGainOrLossForLambert(@PathVariable("ticker") @Valid @NotNull @Size(min = 1, max = 4) final String tickerId,
                                                    @PathVariable("quantity") @Valid @Min(1) final int quantity,
                                                    @RequestParam("csvpath") @Valid @NotNull @Size(min = 1) final String csvFilename,
                                                    @RequestParam(value = "timeAsUtc", defaultValue = "true") final boolean timeAsUtc) throws IOException {

        val ticker = loadTicker(tickerId, csvFilename, new ColumnDictionary(timeAsUtc).withHighOpenCloseLow()
                                                                                      .withRSI()
                                                                                      .withCRSI());

        return lambertCalculator.calculate(ticker, quantity);
    }

    @RequestMapping(value = "ema/{ticker}/quantity/{quantity}", method = RequestMethod.GET)
    public GainOrLoss calculateGainOrLossByEmaCrossing(@PathVariable("ticker") @Valid @NotNull @Size(min = 1, max = 4) final String tickerId,
                                                       @PathVariable("quantity") @Valid @Min(1) final int quantity,
                                                       @RequestParam("csvpath") @Valid @NotNull @Size(min = 1) final String csvFilename,
                                                       @RequestParam(value = "timeAsUtc", defaultValue = "true") final boolean timeAsUtc) throws IOException {

        val ticker = loadTicker(tickerId, csvFilename, new ColumnDictionary(timeAsUtc).withHighOpenCloseLow()
                                                                                      .withShortTermEma("EMA9", "EMA10")
                                                                                      .withLongTermEma("EMA", "EMA50", "EMA100", "EMA200"));

        return emaCrossingCalculator.calculate(ticker, quantity);

    }

    private Ticker loadTicker(String tickerId, String csvFilename, ColumnDictionary columnDictionary) throws IOException {
        val csvFile = checkFile(csvFilename);
        val csvReader = new CsvReader(columnDictionary, CsvReader.NotFoundAction.Ignore);
        return csvReader.loadTradingViewCsv(tickerId, csvFile);
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
