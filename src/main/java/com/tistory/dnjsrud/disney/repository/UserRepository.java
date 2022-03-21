package com.tistory.dnjsrud.disney.repository;

import com.tistory.dnjsrud.disney.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
