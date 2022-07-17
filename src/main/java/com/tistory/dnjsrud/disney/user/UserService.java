package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final MessageSource ms;

    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     * @param form
     * @return userId
     */
    @Transactional
    public Long join(UserJoinForm form) {
        // 회원 중복 체크
        validateDuplicateMember(form);

        // Password 인코더 후 저장
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        User user = User.createUser(form.getLoginId(), encodedPassword, form.getNickname(), form.getEmail());
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(UserJoinForm form) {
        // 동시성 문제가 발생할 수 있다
        Optional<User> findUser = userRepository.findByLoginId(form.getLoginId());
        if (findUser.isPresent()) {
            throw new IllegalStateException(ms.getMessage("user.loginIdDuplicate", null, null));
        }

        findUser = userRepository.findByNickname(form.getNickname());
        if (findUser.isPresent()) {
            throw new IllegalStateException(ms.getMessage("user.nicknameDuplicate", null, null));
        }

        findUser = userRepository.findByEmail(form.getEmail());
        if (findUser.isPresent()) {
            throw new IllegalStateException(ms.getMessage("user.emailDuplicate", null, null));
        }
    }

    // 회원 전체 조회
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    // 회원 정보 조회
    public User findUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    /**
     * UserInfoDto 조회
     * @param userId
     * @return UserInfoDto (nickname, email, reviewCount)
     */
    public UserInfoDto findUserInfoDto(Long userId) {
        User findUser = findUser(userId);
        if(findUser != null) {
            UserInfoDto userInfo = new UserInfoDto(findUser.getNickname(), findUser.getEmail(), reviewRepository.countByUserId(userId));
            return userInfo;
        }
        throw new IllegalStateException("존재하지 않는 회원입니다.");
    }

    // 회원 아이디 찾기
    public String findUserId(String email) {
        Optional<String> loginId = userRepository.findLoginIdByEmail(email);
        if (loginId.isEmpty()) {
            throw new IllegalStateException("이메일이 일치하지 않습니다.");
        }
        return loginId.get();
    }

    /**
     * 회원 비밀번호 변경
     * @param userId
     * @param password
     */
    @Transactional
    public void changePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            user.changePassword(password);
        }
    }

    /**
     * 회원 닉네임 변경
     * @param userId
     * @param nickname
     */
    @Transactional
    public void changeNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Optional<User> findUser = userRepository.findByNickname(nickname);
            if(findUser.isPresent()) {
                throw new IllegalStateException("이미 존재하는 닉네임입니다.");
            }
            user.changeNickname(nickname);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SecurityUser user = userRepository.findSecurityUserByLoginId(username).orElse(null);
        if(user == null) {
            throw new InternalAuthenticationServiceException("사용자 정보가 없습니다.");
        }

        // return 하면 Session((Authentication)) 내부에 저장된다
        return user;
    }

}
