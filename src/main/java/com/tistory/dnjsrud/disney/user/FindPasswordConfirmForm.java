package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.validate.ValidationGroups;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class FindPasswordConfirmForm {

    @NotBlank(message = "아이디는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    private String loginId;

    @NotBlank(message = "이메일은 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Email(groups = ValidationGroups.PatternGroup.class)
    private String email;

}
