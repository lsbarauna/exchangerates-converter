package br.com.jaya.exchangerates.converter;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, UserDetailsServiceAutoConfiguration.class})
@EnableFeignClients("br.com.jaya.exchangerates.converter")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@Configuration
public class ExchangeRatesApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExchangeRatesApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Exchange Rates API").description("\n" +
						"Rest API that performs the conversion between two currencies\n" +
						"using updated conversion rates from an external service."))
				.externalDocs(new ExternalDocumentation()
						.description("GitHub Repository")
						.url("https://github.com/your/repository"));
	}
}
