package com.quartz.trendy;

import com.quartz.trendy.calculator.CalculatorService;
import com.quartz.trendy.csv.CsvReader;
import com.quartz.trendy.emastrategy.EmaStrategy;
import com.quartz.trendy.lambert.LambertStrategy;
import com.quartz.trendy.spring.CommonExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackageClasses = {CalculatorService.class,
                                                 LambertStrategy.class,
                                                 EmaStrategy.class,
                                                 CommonExceptionHandler.class,
                                                 CsvReader.class})
@EnableConfigurationProperties
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
