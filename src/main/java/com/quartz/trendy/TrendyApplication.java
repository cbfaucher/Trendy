package com.quartz.trendy;

import com.quartz.trendy.calculator.CalculatorService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackageClasses = CalculatorService.class)
@EnableConfigurationProperties
@EnableSwagger2
public class TrendyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrendyApplication.class, args);
	}

}
