package com.hot.backend;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.headers(headers -> headers.addHeaderWriter(new StaticHeadersWriter("Access-Control-Allow-Origin", "header-value")));
		http.authorizeHttpRequests(auth -> {
			// "/hot/**"に該当するリクエストは認証をパスする。
			auth.requestMatchers("/hot/**", "/csrftoken").permitAll().
			// その他のリクエストは認証必須。
			anyRequest().authenticated();
		})
		// CORS設定
		.cors(cors -> cors.configurationSource(corsFilter()))
		//CSRFを無効化
		// .csrf(csrf -> csrf.ignoringRequestMatchers("/csrftoken"));
		.csrf(csrf -> csrf.disable());
		return http.build();
	}
	
	// CORS設定
	@Bean
	public CorsConfigurationSource corsFilter() {
		CorsConfiguration cors = new CorsConfiguration();
		// CORSリクエストを許可するドメイン設定
		cors.addAllowedOrigin("http://localhost:5173");
		// CORSリクエストで許可するヘッダー情報
		cors.setAllowedHeaders(Arrays.asList("*"));
		// CORSリクエストを許可するメソッドの設定
		cors.setAllowedMethods(Arrays.asList("GET", "POST"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// CORSリクエストを許可するURLの形式
		source.registerCorsConfiguration("/**", cors);
		
		return source;
	}
}
