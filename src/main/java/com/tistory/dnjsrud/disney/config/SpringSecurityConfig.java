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
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
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
    private final AuthenticationEntryPoint customAuthenticationEntryPoint;

    private final DefaultOAuth2UserService customOauth2UserService;

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
                    .antMatchers("/admin/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            .and()
                .exceptionHandling()
                    .accessDeniedHandler(customAccessDeniedHandler) // 접근 권한이 없는 요청이 올 때
                    .authenticationEntryPoint(customAuthenticationEntryPoint) // 로그인하지 않고 접근하려는 경우
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
                .invalidateHttpSession(true)
            .and() // Google 로그인 설정
                .oauth2Login()
                    .loginPage("/user/loginForm") // Google 로그인이 완료 -> 엑세스 토큰, 사용자 프로필 정보를 얻어옴
                    .userInfoEndpoint()
                        .userService(customOauth2UserService)
                    .and()
                    .failureHandler(customFailureHandler);
    }

    private String[] urlPermitAll() {
        String[] urls = new String[] {
                "/", "/disney", "/user/loginForm", "/user/loginErr", "/user/login", "/user/join", "/user/loginErr",
                "/user/findLoginId", "/user/findLoginIdResult", "/user/findPassword", "/user/findPasswordResult",
                "/movie/**"
        };
        return urls;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
