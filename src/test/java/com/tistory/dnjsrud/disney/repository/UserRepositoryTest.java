package com.tistory.dnjsrud.disney.repository;

import com.tistory.dnjsrud.disney.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    UserRepository userRepository;

    @Test
    @Rollback(value = false)
    public void userInsert() throws Exception {
        // given
        User userA = new User("dnjsrud3407", "1234", "dnjsrud", "dnjsrud3407@naver.com");

        // when
        userRepository.save(userA);

        // then
        User findUser = userRepository.findById(userA.getId()).get();
        assertThat(userA).isEqualTo(findUser);
    }
}