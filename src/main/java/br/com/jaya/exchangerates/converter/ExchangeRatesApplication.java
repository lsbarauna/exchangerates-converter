package br.com.jaya.exchangerates.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication()
@EnableFeignClients("br.com.jaya.exchangerates.converter")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class ExchangeRatesApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExchangeRatesApplication.class, args);
	}
}
