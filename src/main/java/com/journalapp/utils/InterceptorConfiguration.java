package com.journalapp.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.journalapp.interceptor.SignUpInterceptor;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SignUpInterceptor()).addPathPatterns("/signup/**");
	}
}
