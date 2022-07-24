package com.tistory.dnjsrud.disney.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ToString
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinForm {

    @NotBlank(message = "아이디는 필수입니다.")
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "아이디는 5~20자 영문 소문자, 숫자만 사용 가능합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^[a-z0-9A-Z$`~!@$!%*#^?&-_=+]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자만 사용 가능합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String passwordConfirm;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{2,15}$", message = "닉네임은 2~15자 한글, 영문 대 소문자, 숫자만 사용 가능합니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다.")
    @Email
    private String email;

    public UserJoinForm(String loginId, String password, String nickname, String email) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
    }
}
