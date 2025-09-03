package com.example.prices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.example.prices.shared.application.UseCase;

@SpringBootApplication
@ComponentScan(includeFilters = {
		@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = UseCase.class)
})
public class PocPricesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PocPricesApiApplication.class, args);
	}

}
