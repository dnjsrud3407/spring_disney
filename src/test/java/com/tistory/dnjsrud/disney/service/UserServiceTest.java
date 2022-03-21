package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.User;
import com.tistory.dnjsrud.disney.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback(value = false)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 회원가입() throws Exception {
        // given
        User user = new User("dnjsrud3407", "1234", "wk", "dnjsrud3407@naver.com");

        // when
        Long joinUserId = userService.join(user);
        User findUser = userRepository.findById(joinUserId).get();

        // then
        Assertions.assertThat(user.getLoginId()).isEqualTo(findUser.getLoginId());
    }
}