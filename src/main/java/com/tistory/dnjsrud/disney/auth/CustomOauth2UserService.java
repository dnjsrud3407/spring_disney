package com.tistory.dnjsrud.disney.auth;

import com.tistory.dnjsrud.disney.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * 구글 로그인 후
 * Disney 회원가입 처리
 */
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final MessageSource ms;

    /**
     * 구글로부터 회원프로필 정보를 받아옴
     * 1. sub - 구글 아이디 primary value,
     * 2. email
     * @param userRequest
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 강제로 회원가입
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google
        String sub = oAuth2User.getAttribute("sub");
        String loginId = provider + "_" + sub;  // google_115642999616091662090
        String nickname = loginId;
        String password = "";
        String email = oAuth2User.getAttribute("email");

        UserJoinForm form = new UserJoinForm(loginId, password, nickname, email);

        // 일반 회원가입으로 DB에 email이 저장되어 있는 경우
        User emailDuplicated = userRepository.findByEmailAndProviderIsNull(form.getEmail()).orElse(null);
        if(emailDuplicated != null) {
            throw new IllegalStateException(ms.getMessage("user.emailDuplicate", null, null));
        }

        // 구글 로그인 중복 확인
        User user = userRepository.findByLoginId(form.getLoginId()).orElse(null);

        // 구글 최초 로그인인 경우
        if(user == null) {
            user = User.createUser(form.getLoginId(), password, form.getNickname(), form.getEmail(), provider);
            userRepository.save(user);
        }

        SecurityUser securityUser = new SecurityUser(user, oAuth2User.getAttributes());

        return securityUser;
    }
}
