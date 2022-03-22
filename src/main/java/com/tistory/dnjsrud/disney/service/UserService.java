package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.User;
import com.tistory.dnjsrud.disney.repository.UserRepository;
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

    /**
     * 회원정보 조회
     * @param id
     * @return User
     */
    public User findUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

}
