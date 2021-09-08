package com.ecommerce.sistema.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;

	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" };

	private static final String[] PUBLIC_MATCHERS_GET = { "/produtos/**", "/categorias/**" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// Necessário para poder acessar database /h2-console/** do profile de test
		if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}

		// Aplica o cors definido pelo @Bean CorsConfigurationSource e desabilita
		// proteção csrf
		http.cors().and().csrf().disable();

		// Permite acesso aos end-points PUBLIC_MATCHERS[] e requisita autenticação para
		// qualquer outro end-point
		http.authorizeRequests()//
				.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()// Permite acesso público apenas para GET
				.antMatchers(PUBLIC_MATCHERS).permitAll() // Permite acesso público para todos os métodos
				.anyRequest().authenticated(); // Requisita autenticação para qualquer requisição 

		// Define a política de sessão como STATELESS para que o sistema não recupere
		// sessão de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/**
	 * Permite acesso aos end-points por múltiplas fontes Ex.: postman, front-end,
	 * apps, SoapUI
	 * 
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}
