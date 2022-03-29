package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLoginId(String loginId);
}
