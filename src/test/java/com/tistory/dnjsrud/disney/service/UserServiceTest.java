package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.user.User;
import com.tistory.dnjsrud.disney.user.UserRepository;
import com.tistory.dnjsrud.disney.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
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

        // then
        User findUser = userService.findUser(joinUserId);
        assertEquals(user, findUser);
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        // given
        User user = new User("dnjsrud3407", "1234", "wk", "dnjsrud3407@naver.com");
        User user2 = new User("dnjsrud3407", "12345", "wk2", "dnjsrud34072@naver.com");

        // when
        userService.join(user);
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            userService.join(user2);
        });

        // then

    }


}