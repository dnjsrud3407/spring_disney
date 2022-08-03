package com.tistory.dnjsrud.disney.user;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<SecurityUser> findSecurityUserByLoginId(String loginId);

    Optional<ModifyNicknameForm> findNicknameById(Long userId);

    Optional<String> findLoginIdByEmail(String email);
}
