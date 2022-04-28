package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    /**
     * 회원가입
     * @param form
     * @return userId
     */
    @Transactional
    public Long join(UserJoinForm form) {
        // Validation 검증하는 부분은 강의 스프링 MVC 2편에 나와있다 - Controller 에서 작성
        // 회원 중복 체크
        validateDuplicateMember(form);
        User user = User.createUser(form.getLoginId(), form.getPassword(), form.getNickname(), form.getEmail());
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(UserJoinForm form) {
        // 동시성 문제가 발생할 수 있다
        Optional<User> findUser = userRepository.findByLoginId(form.getLoginId());
        if (findUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        findUser = userRepository.findByEmail(form.getNickname());
        if (findUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }

        findUser = userRepository.findByEmail(form.getEmail());
        if (findUser.isPresent()) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
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
}
