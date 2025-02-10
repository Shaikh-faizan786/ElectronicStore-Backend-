package com.electronics.store.config;
import com.electronics.store.security.JwtAuthenticationEntryPoint;
import com.electronics.store.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;

    @Autowired
    private JwtAuthenticationFilter filter;

    private final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger-resources/**",
//            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {

//        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());

        // globally ab isko orsConfigurer.disable ko anable karnege

        security.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
//                corsConfiguration.addAllowedOrigin("http://localhost:4200");
                // iske andar hum methods ko bhi allow kar sakte hai
                // multiple url bhi allow kar sakte hai
                corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                corsConfiguration.setAllowedMethods(List.of("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(List.of("*"));
                corsConfiguration.setMaxAge(4000L);
                return corsConfiguration;
            }
        }));

        security.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

        security.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.GET,"/users").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/products").hasRole("NORMAL")
                        .requestMatchers(HttpMethod.GET,"/products").hasAnyRole("ADMIN","NORMAL")
                        .requestMatchers(HttpMethod.POST,"/users").authenticated()
                        .requestMatchers(HttpMethod.PUT,"/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/generate-token","/auth/login-with-google","/auth/regenerate-token").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html","/swagger-ui/index.html").permitAll()
//                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .anyRequest().permitAll()
                );

//        security.httpBasic(http -> Customizer.withDefaults());

        // exception ke liye
        security.exceptionHandling(ex-> ex.authenticationEntryPoint(entryPoint));
        // session policy  ke liye
        security.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // check kaega username and passowrd sahi hai ya nhi
        security.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
}
