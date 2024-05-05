package br.com.jaya.exchangerates.converter;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

import static org.hibernate.type.descriptor.java.JdbcDateJavaType.DATE_FORMAT;
import static org.mapstruct.ap.internal.util.JodaTimeConstants.DATE_TIME_FORMAT;

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
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> {
			builder.simpleDateFormat(DATE_TIME_FORMAT);
			builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT)));
			builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
		};
	}
}
