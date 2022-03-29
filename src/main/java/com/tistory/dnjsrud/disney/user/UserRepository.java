package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    @Query("select u.loginId from User u where u.email = :email")
    Optional<String> findLoginIdByEmail(@Param("email") String email);
}
