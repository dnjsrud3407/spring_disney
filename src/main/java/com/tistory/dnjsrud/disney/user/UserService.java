package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.global.EmailSender;
import com.tistory.dnjsrud.disney.global.RandomPassword;
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

import javax.mail.MessagingException;
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

    private final RandomPassword randomPassword;
    private final EmailSender emailSender;

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
        User user = User.createUser(form.getLoginId(), encodedPassword, form.getNickname(), form.getEmail(), null);
        userRepository.save(user);
        return user.getId();
    }

    public boolean validateDuplicateMember(UserJoinForm form) {
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
        return false;
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
     * 회원 닉네임 찾기
     * @param userId
     * @return
     */
    public ModifyNicknameForm findNickname(Long userId){
        return userRepository.findNicknameById(userId).orElse(null);
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

    /**
     * 회원 아이디 찾기
     * @param email
     * @return
     */
    public String findUserLoginId(String email) {
        Optional<String> loginId = userRepository.findLoginIdByEmail(email);
        if (loginId.isEmpty()) {
            throw new IllegalStateException("이메일이 일치하지 않습니다.");
        }
        return loginId.get();
    }

    /**
     * 비밀번호 찾기 시 로그인 아이디, 이메일 확인
     * @param loginId
     * @param email
     * @return
     */
    public long findUserByLoginIdAndEmail(String loginId, String email) {
        Optional<User> findUser = userRepository.findByLoginIdAndEmail(loginId, email);
        if(findUser.isPresent()) {
            return findUser.get().getId();
        }
        return -1;
    }

    /**
     * random 패스워드를 생성하여 이메일 전송 후 회원 비밀번호를 변경한다
     * @param userId
     * @param email
     */
    @Transactional
    public void sendRandomPasswordEmail(Long userId, String email) throws MessagingException {
        String password = randomPassword.makePassword(10);

        // 임시 비밀번호 메일 보내기
        String to = email;
        String title = "Disney | 임시 비밀번호 이메일 입니다.";
        emailSender.send(to, title, "user/email", password);

        // 회원 비밀번호 변경
        changePassword(userId, password);
    }

    /**
     * 회원 비밀번호 변경
     * @param userId
     * @param password
     */
    @Transactional
    public void changePassword(Long userId, String password) {
        User user = userRepository.findById(userId).orElse(null);
        String encodedPassword = passwordEncoder.encode(password);
        if (user != null) {
            user.changePassword(encodedPassword);
        }
    }

    /**
     * 회원 닉네임 변경
     * @param userId
     * @param modifyNicknameForm
     */
    @Transactional
    public void modifyNickname(Long userId, ModifyNicknameForm modifyNicknameForm) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Optional<User> findUser = userRepository.findByNickname(modifyNicknameForm.getNickname());
            if(findUser.isPresent()) {
                throw new IllegalStateException(ms.getMessage("user.nicknameDuplicate", null, null));
            }
            user.changeNickname(modifyNicknameForm.getNickname());
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

    /**
     * 비밀번호 변경 시 비밀번호 확인
     * @param confirmPassword   회원 id
     * @param userPassword      DB에 저장된 pw
     */
    public void confirmPassword(String confirmPassword, String userPassword) {
        if(!passwordEncoder.matches(confirmPassword, userPassword)) {
            throw new IllegalArgumentException(ms.getMessage("user.confirmPassword", null, null));
        }
    }

    /**
     * 회원 비활성화 처리
     * @param userId
     */
    @Transactional
    public void disableUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null) {
            user.changeEnabled(false);
        }
    }
}
