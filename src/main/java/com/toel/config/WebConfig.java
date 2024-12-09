package com.toel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import vn.payos.PayOS;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Value("${PAYOS_CLIENT_ID}")
	private String clientId;

	@Value("${PAYOS_API_KEY}")
	private String apiKey;

	@Value("${PAYOS_CHECKSUM_KEY}")
	private String checksumKey;

	@Bean
	public PayOS payOS() {
		return new PayOS(clientId, apiKey, checksumKey);
	}

	@Override
	public void addCorsMappings(@NonNull CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*").exposedHeaders("*")
				.allowCredentials(false).maxAge(3600); // Max age of the CORS pre-flight request
	}
}
