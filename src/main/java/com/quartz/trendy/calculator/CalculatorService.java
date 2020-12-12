package com.quartz.trendy.calculator;

import com.quartz.trendy.csv.ColumnDictionary;
import com.quartz.trendy.csv.CsvReader;
import com.quartz.trendy.model.GainOrLoss;
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

    @RequestMapping(value = "now", method = RequestMethod.GET)
    public String ping() {
        return "Now is " + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    @RequestMapping(value = "lambert/{ticker}/quantity/{quantity}", method = RequestMethod.GET)
    public GainOrLoss calculateGainOrLossForLambert(@PathVariable("ticker") @Valid @NotNull @Size(min=1, max=4) final String tickerId,
                                                    @PathVariable("quantity") @Valid @Min(1) final int quantity,
                                                    @RequestParam("csvpath") @Valid @NotNull @Size(min = 1) final String csvFilename,
                                                    @RequestParam(value = "timeAsUtc", defaultValue = "true") final boolean timeAsUtc) throws IOException {

        val csvFile = checkFile(csvFilename);
        val csvReader = new CsvReader(new ColumnDictionary(timeAsUtc));
        val ticker = csvReader.loadTradingViewCsv(tickerId, csvFile);

        return lambertCalculator.calculate(ticker, quantity);
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
