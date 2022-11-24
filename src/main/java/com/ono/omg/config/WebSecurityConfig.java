package com.ono.omg.config;

import com.ono.omg.security.jwt.AuthenticationEntryPointException;
import com.ono.omg.security.jwt.JwtAuthFilter;
import com.ono.omg.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity //시큐리티 활성화
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final AuthenticationEntryPointException authenticationEntryPointException;

    //password를 암호화 하지않으면 spring security가 접근을 허가하지 않는다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        //h2-console 사용에 대한 허용 (CSRF, FrameOptions 무시)
        return (web) -> web.ignoring()
                .antMatchers("/h2-console/**",
                                        "/v2/api-docs",
                                        "/v3/api-docs",
                                        "/configuration/ui",
                                        "/swagger-resources/**",
                                        "/swagger-ui/**",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/swagger/**",
                                        "/js/**",
                                        "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOriginPatterns(List.of("*"));
            cors.setAllowedMethods(List.of("*"));
            cors.setAllowedHeaders(List.of("*"));
            cors.addExposedHeader("Authorization");
            cors.addExposedHeader("Refresh_Token");
            cors.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", cors);

            return cors;
        });

        http.csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPointException);

        http.formLogin().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/accounts/signup").permitAll()
                .antMatchers( "/accounts/login").permitAll()
                .antMatchers("/admin/login").permitAll()
                .antMatchers("/omg").permitAll()
                .antMatchers("/api/omg").permitAll()
                .antMatchers("/products/detail/{productId}").permitAll()
                .antMatchers("/api/products/detail/{productId}").permitAll()
                .antMatchers("/admin/management").permitAll()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/endPoint1").permitAll()
                .antMatchers("/endPoint2").permitAll()

//                .antMatchers("/").permitAll()
//                .antMatchers("/**").permitAll()
//                .antMatchers("/admin/management").hasRole("ADMIN")

//                .antMatchers(HttpMethod.GET, "/product/**").permitAll()
//                .antMatchers(HttpMethod.GET, "/product/{productId}/comment/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
