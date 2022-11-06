package com.tistory.dnjsrud.disney.user;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoDto {
    private String nickname;
    private String email;
    private Long reviewCount;
}
