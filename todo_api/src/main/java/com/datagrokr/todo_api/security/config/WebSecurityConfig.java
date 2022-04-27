package com.datagrokr.todo_api.security.config;

import javax.servlet.http.HttpServletResponse;

import com.datagrokr.todo_api.service.user.UserServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.AllArgsConstructor;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserServiceImpl userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // http
        // .cors()
        // .and()
        // .csrf()
        // .disable()
        // .exceptionHandling()
        // .authenticationEntryPoint((request, response, ex) -> {
        // response.sendError(
        // HttpServletResponse.SC_UNAUTHORIZED,
        // ex.getMessage());
        // })
        // .and()
        // .sessionManagement()
        // .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        // .and()
        // .authorizeRequests()
        // .antMatchers("/",
        // "/favicon.ico",
        // "/**/*.png",
        // "/**/*.gif",
        // "/**/*.svg",
        // "/**/*.jpg",
        // "/**/*.html",
        // "/**/*.css",
        // "/**/*.js")
        // .permitAll()
        // .antMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
        // .antMatchers(HttpMethod.GET, "/api/checklogged").permitAll()
        // .antMatchers(HttpMethod.POST, "/api/login").permitAll()
        // .antMatchers(HttpMethod.POST, "/api/registration").permitAll()
        // .antMatchers(HttpMethod.GET, "/api/registration/confirm").permitAll()
        // .antMatchers(HttpMethod.OPTIONS).permitAll()
        // .anyRequest().authenticated()
        // .and()
        // .formLogin()
        // .loginProcessingUrl("/api/login")
        // .failureHandler(authenticationFailureHandler())
        // .and()
        // .logout()
        // .logoutUrl("/api/logout")
        // .deleteCookies("JSESSIONID")
        // .logoutSuccessHandler(logoutSuccessHandler())
        // .and()
        // .httpBasic()
        // .and()
        // .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        http.cors().and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers(HttpMethod.GET, "/api/checklogged").permitAll()
                .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/registration").permitAll()
                .antMatchers(HttpMethod.GET, "/api/registration/confirm").permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers(HttpMethod.GET, "/api/todos").authenticated()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/api/login")
                .failureHandler(authenticationFailureHandler())
                .and()
                .logout()
                .logoutUrl("/api/logout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .httpBasic();

        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    // @Bean
    // CorsConfigurationSource corsConfigurationSource() {
    // final UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // final CorsConfiguration config = new CorsConfiguration();
    // config.setAllowCredentials(true);
    // config.addAllowedOrigin("*");
    // config.addAllowedHeader("*");
    // config.addExposedHeader("Authorization");
    // config.addAllowedMethod("OPTIONS");
    // config.addAllowedMethod("HEAD");
    // config.addAllowedMethod("GET");
    // config.addAllowedMethod("PUT");
    // config.addAllowedMethod("POST");
    // config.addAllowedMethod("DELETE");
    // config.addAllowedMethod("PATCH");
    // source.registerCorsConfiguration("/**", config);
    // return source;
    // }

}