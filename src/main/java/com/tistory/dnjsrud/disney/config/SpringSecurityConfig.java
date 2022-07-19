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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationSuccessHandler customSuccessHandler;
    private final AuthenticationFailureHandler customFailureHandler;
    private final AccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/", "/resources/**", "/css/**", "/js/**", "/img/**", "/poster/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .and() // 페이지 권한 설정
                .authorizeRequests()
                    .antMatchers(urlPermitAll()).permitAll()
                    .antMatchers("/user/myPage", "/review/**").hasAnyRole("USER", "ADMIN")
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            .and()
                .exceptionHandling() // 접근 권한이 없는 요청이 올 때
                    .accessDeniedHandler(customAccessDeniedHandler)
            .and() // 로그인 설정
                .formLogin()
                    .loginPage("/user/loginForm")
                    .loginProcessingUrl("/user/login")  // security가 로그인해주기 때문에 Controller에 구현 X
                    .usernameParameter("loginId")
                    .successHandler(customSuccessHandler) // 로그인 성공
                    .failureHandler(customFailureHandler) // 로그인 실패
                    .permitAll()
            .and() // 로그아웃 설정
                .logout()
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/disney")
                .invalidateHttpSession(true);
    }

    private String[] urlPermitAll() {
        String[] urls = new String[] {
                "/", "/disney", "/user/loginForm", "/user/login", "/user/join", "/user/loginErr",
                "/movie/**"
        };
        return urls;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
