package com.tistory.dnjsrud.disney.user;

import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.Optional;

import static com.tistory.dnjsrud.disney.user.QUser.user;

public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<SecurityUser> findSecurityUserByLoginId(String loginId) {
        return Optional.ofNullable(
                queryFactory
                .select(new QSecurityUser(user.id, user.loginId, user.password, user.role))
                .from(user)
                .where(user.loginId.eq(loginId), user.isEnabled.eq(true))
                .fetchOne()
        );
    }

    @Override
    public Optional<ModifyNicknameForm> findNicknameById(Long userId) {
        return Optional.ofNullable(
                queryFactory
                .select(new QModifyNicknameForm(user.nickname))
                .from(user)
                .where(user.id.eq(userId))
                .fetchOne()
        );
    }

    @Override
    public Optional<String> findLoginIdByEmail(String email) {
        return Optional.ofNullable(
                queryFactory
                .select(user.loginId)
                .from(user)
                .where(user.email.eq(email), user.isEnabled.eq(true))
                .fetchOne()
        );
    }
}
