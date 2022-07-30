package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.validate.ValidationGroups;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
@ToString
public class ModifyPasswordForm {

    @NotBlank(message = "비밀번호는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-z0-9A-Z$`~!@$!%*#^?&-_=+]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자만 사용 가능합니다.", groups = ValidationGroups.PatternGroup.class)
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    private String passwordConfirm;
}
