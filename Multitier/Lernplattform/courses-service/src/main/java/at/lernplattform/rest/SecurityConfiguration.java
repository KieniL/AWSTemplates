package at.lernplattform.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;

import at.lernplattform.infrastructure.TokenService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private TokenService tokenService;

	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		/*
		// @formatter:off
		http.csrf().disable()
			.cors().and()
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/users/login").permitAll()
			.antMatchers(HttpMethod.GET, "/courses").permitAll()
			.antMatchers(HttpMethod.GET, "/courses/{\\d+}").permitAll()
			.antMatchers(HttpMethod.GET, "/courses/{\\d+}/tasks").permitAll()
			.antMatchers(HttpMethod.GET, "/courses/{\\d+}/tasks/{\\d+}").permitAll()
			.antMatchers(HttpMethod.PUT, "/courses/{\\d+}/tasks/{\\d+}/feedback").permitAll()
			.antMatchers(HttpMethod.GET, "/database/getschemas").permitAll()
			.antMatchers("/**").authenticated();
		// @formatter:on*/
		http.addFilterBefore(new JwtAuthorizationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
	}
	/*
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(ImmutableList.of("*"));
        configuration.setAllowedMethods(ImmutableList.of("HEAD",
                "GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

}
