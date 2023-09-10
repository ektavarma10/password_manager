package com.project.passwordmanager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.passwordmanager.filter.JWTAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

import static com.project.passwordmanager.helper.PasswordManagerHelper.PBKDF_2_WITH_HMAC_SHA_256;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootConfiguration
@EnableAutoConfiguration()
@ComponentScan(basePackages = "com.project.passwordmanager")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories
public class PasswordManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordManagerApplication.class, args);
	}


	@Bean
	public FilterRegistrationBean<JWTAuthFilter> authenticationFilter(JWTVerifier jwtVerifier) {
		FilterRegistrationBean<JWTAuthFilter> filter = new FilterRegistrationBean<>();

		filter.setFilter(new JWTAuthFilter(jwtVerifier));
		filter.setUrlPatterns(Collections.singletonList("/password/preview"));

		return filter;
	}

	@Value("${jwt-secret}")
	private String jwtSecret;

	@Bean
	public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(jwtSecret);
	}

	@Bean
	public SecretKeyFactory secretKeyFactory() throws NoSuchAlgorithmException {
		return SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_256);
	}

	@Bean
	public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm)
				.build();
	}
}
