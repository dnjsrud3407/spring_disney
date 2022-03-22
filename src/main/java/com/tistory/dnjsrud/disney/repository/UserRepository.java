package com.tistory.dnjsrud.disney.repository;

import com.tistory.dnjsrud.disney.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLoginId(String loginId);
}
