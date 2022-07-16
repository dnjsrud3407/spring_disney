package com.tistory.dnjsrud.disney.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurity extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler customSuccessHandler;
    private final AuthenticationFailureHandler customFailureHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/resources/**", "/css/**", "/js/**", "/img/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .and() // 페이지 권한 설정
                .authorizeRequests()
                .antMatchers(urlPermitAll()).permitAll()
                .antMatchers("/user/myPage").hasRole("USER")
                .anyRequest().authenticated()
            .and() // 로그인 설정
                .formLogin()
                .loginPage("/user/login")
                .usernameParameter("loginId")
                .successHandler(customSuccessHandler)
                .failureHandler(customFailureHandler)
                .permitAll()
            .and() // 로그아웃 설정
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true);
    }

    private String[] urlPermitAll() {
        String[] urls = new String[] {
                "/", "/disney", "/user/login", "/user/join"
        };
        return urls;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
