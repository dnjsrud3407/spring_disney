package com.tistory.dnjsrud.disney.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if(savedRequest != null) {
            String redirectUrl = savedRequest.getRedirectUrl();
            response.sendRedirect(redirectUrl);
        } else {
            response.sendRedirect("/disney");
        }

    }
}
