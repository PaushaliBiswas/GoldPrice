package com.goldprice.calculator.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("default")
public class CorsConfig implements WebMvcConfigurer{

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/gold-price/**")
		.allowedMethods(HttpMethod.GET.toString(), HttpMethod.POST.toString(), HttpMethod.OPTIONS.toString())
		.exposedHeaders(HttpHeaders.CONTENT_DISPOSITION);
	}
}
