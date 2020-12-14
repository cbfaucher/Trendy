package com.quartz.trendy.tools;

import io.swagger.annotations.Api;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api
@RequestMapping("/tools")
@Validated
public class ToolsController {

    @RequestMapping(value = "prices/{price}/{quantity}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PriceInfo getPriceInfo(@PathVariable("price") final double price,
                                  @PathVariable("quantity") final int quantity,
                                  @RequestParam(value = "fees", defaultValue = "50") final double fees,
                                  @RequestParam(value = "tolerance", defaultValue = "3") final double tolerancePercent) {
        return PriceInfo.createFrom(price, quantity, fees, tolerancePercent);
    }
}
