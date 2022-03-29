package com.tistory.dnjsrud.disney.user;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class UserJoinForm {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String password;

    @NotEmpty
    private String passwordConfirm;

    @NotEmpty
    private String nickname;

    @NotEmpty
    private String email;

    public UserJoinForm(String loginId, String password, String nickname, String email) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }
}
