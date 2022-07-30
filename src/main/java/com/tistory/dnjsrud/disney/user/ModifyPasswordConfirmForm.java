package com.tistory.dnjsrud.disney.user;

import com.tistory.dnjsrud.disney.validate.ValidationGroups;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@ToString
public class ModifyPasswordConfirmForm {

    @NotBlank(message = "비밀번호는 필수입니다.", groups = ValidationGroups.NotBlankGroup.class)
    private String password;

}
