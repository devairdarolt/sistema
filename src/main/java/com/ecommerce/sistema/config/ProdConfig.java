package com.ecommerce.sistema.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ecommerce.sistema.services.DBService;

@Configuration
@Profile("prod")
public class ProdConfig {
	@Autowired
	private DBService dBService;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		
		if("create".equals(strategy)) {
			//dBService.instantiateTestDatabase();
			return true;
		}
		return false;
		
	}

}
