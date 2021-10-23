package com.ecommerce.sistema.config;

import java.text.ParseException;

import com.ecommerce.sistema.services.DBService;
import com.ecommerce.sistema.services.EmailService;
import com.ecommerce.sistema.services.SmtpEmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevConfig {
	@Autowired
	private DBService dBService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		if("create".equals(strategy)) {
			dBService.instantiateTestDatabase();
			return true;
		}
		return false;
		
	}
	
	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
}
