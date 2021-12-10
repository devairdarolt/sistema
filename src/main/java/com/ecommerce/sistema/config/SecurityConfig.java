package com.ecommerce.sistema.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ecommerce.sistema.security.JWTAutenticationFilter;
import com.ecommerce.sistema.security.JWTAuthorizationFilter;
import com.ecommerce.sistema.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JWTUtil jwtutil;
	// pode fazer a injeção pela interface pois existe apenas uma Impl que
	// implementa esse serviço
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private Environment env;

	private static final String[] PUBLIC_MATCHERS = { "/h2-console/**" };

	private static final String[] PUBLIC_MATCHERS_GET = { "/produtos/**", "/categorias/**" };

	private static final String[] PUBLIC_MATCHERS_POST = { "/clientes/**"};

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
				.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() // Permite acesso público para os métodos do vetor Post
				.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()// Permite acesso público apenas para GET
				.antMatchers(PUBLIC_MATCHERS).permitAll() // Permite acesso público para todos os métodos
				.anyRequest().authenticated(); // Requisita autenticação para qualquer requisição

		// Filtro de autenticação
		http.addFilter(new JWTAutenticationFilter(authenticationManager(), jwtutil));

		// Filtro de autorização
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtutil, userDetailsService));

		// Define a política de sessão como STATELESS para que o sistema não recupere
		// sessão de usuário
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
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

	// Bean para injetar BCryptPasswordEncoder para password
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
