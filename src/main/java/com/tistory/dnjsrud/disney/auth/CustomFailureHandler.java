package com.tistory.dnjsrud.disney.auth;

import com.tistory.dnjsrud.disney.exception.OAuth2AuthenticationProcessingException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String exceptionMsg;

        // 비밀번호가 틀린 경우 || 아이디가 틀린 경우
        if (exception instanceof BadCredentialsException
                || exception instanceof InternalAuthenticationServiceException) {
            exceptionMsg = "invalid";
        }
        // Oauth 로그인시 해당 이메일이 일반 회원가입으로 DB에 email이 저장되어 있는 경우
        else if(exception instanceof OAuth2AuthenticationProcessingException) {
            exceptionMsg = "oauth2Duplicate";
        }
        else {
            exceptionMsg = "requestAdm";
        }

        setDefaultFailureUrl("/user/loginForm?exceptionMsg=" + exceptionMsg);
        super.onAuthenticationFailure(request, response, exception);
    }
}
