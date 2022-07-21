package com.tistory.dnjsrud.disney.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private MessageSource ms;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (authException instanceof InsufficientAuthenticationException) {
            // 일반 회원가입으로 DB에 email이 저장되어 있는 경우
            request.setAttribute("msg", "이미 회원가입으로 가입된 사용자입니다. 아이디/비밀번호 찾기를 진행해주세요.");
        } else {
            request.setAttribute("msg", ms.getMessage("requestAdm", null, null));
        }
        request.getRequestDispatcher("/user/loginForm").forward(request, response);
    }
}
