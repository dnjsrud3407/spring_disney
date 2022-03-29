package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.user.User;
import com.tistory.dnjsrud.disney.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입
     * @param user
     * @return userId
     */
    @Transactional
    public Long join(User user) {
        // Validation 검증하는 부분은 강의 스프링 MVC 2편에 나와있다
        // 회원 중복 체크
        validateDuplicateMember(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMember(User user) {
        // 동시성 문제가 발생할 수 있다
        List<User> findUsers = userRepository.findByLoginId(user.getLoginId());
        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
    }

    // 회원 전체 조회
    public List<User> findUsers() {
        return userRepository.findAll();
    }

    // 회원 정보 조회
    public User findUser(Long id) {
        return userRepository.findById(id).orElse(null);
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
}
