package com.tistory.dnjsrud.disney.user;

import com.querydsl.core.annotations.QueryProjection;
import com.tistory.dnjsrud.disney.validate.ValidationGroups;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@ToString
public class ModifyNicknameForm {

    @NotBlank(message = "닉네임은 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9]{2,15}$", message = "닉네임은 2~15자 한글, 영문 대 소문자, 숫자만 사용 가능합니다.", groups = ValidationGroups.PatternGroup.class)
    private String nickname;

    @QueryProjection
    public ModifyNicknameForm(String nickname) {
        this.nickname = nickname;
    }
}
