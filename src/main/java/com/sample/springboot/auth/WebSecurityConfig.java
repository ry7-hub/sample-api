package com.sample.springboot.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sample.springboot.support.JWTAuthenticationFilter;
import com.sample.springboot.support.JWTAuthorizationFilter;

import static com.sample.springboot.support.SecurityConstants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .configurationSource(this.corsConfigurationSource())
                .and().authorizeRequests()
                // とりあえず動けば。。。
                .antMatchers("/public", SIGNUP_URL, LOGIN_URL, "/api/users",
                		"/api/attendance/list", "/api/attendance/regist", "/api/attendance/find", "/api/attendance//update/status", "/api/attendance/update/batch").permitAll()
                .anyRequest().authenticated()
                .and().logout()
                .and().csrf().disable()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), bCryptPasswordEncoder()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        ;
    }


    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // frontのlocalhost:3000からのCORSを有効化（localhost:3000はnpm run dev）
    private CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        // jwt用のhttpヘッダーを登録
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addAllowedOrigin("http://172.30.0.10:3000");// AWS用
//        corsConfiguration.addAllowedOrigin("http://localhost:3000");
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", corsConfiguration);

        return corsSource;
    }
	
}
