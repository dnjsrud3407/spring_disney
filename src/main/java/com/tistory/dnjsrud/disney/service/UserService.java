package com.tistory.dnjsrud.disney.service;

import com.tistory.dnjsrud.disney.domain.User;
import com.tistory.dnjsrud.disney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 회원가입
     * @param user
     * @return userId
     */
    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }



}
