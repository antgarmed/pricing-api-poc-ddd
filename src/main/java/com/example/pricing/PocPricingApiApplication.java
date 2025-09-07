package com.example.pricing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.example.pricing.shared.application.UseCase;

@SpringBootApplication
@ComponentScan(includeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = UseCase.class)
})
public class PocPricingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocPricingApiApplication.class, args);
	}

}
