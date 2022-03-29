package com.tistory.dnjsrud.disney.user;

import com.sun.istack.NotNull;
import com.tistory.dnjsrud.disney.review.Review;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotNull
    private String loginId;

    @NotNull
    private String password;

    @NotNull @Column(unique = true)
    private String nickname;

    @NotNull @Column(unique = true)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    private User(String loginId, String password, String nickname, String email, Role role) {
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.role = role;
    }

    //== 생성 메서드 ==//
    public static User createUser(String loginId, String password, String nickname, String email) {
        User user = new User(loginId, password, nickname, email, Role.USER);
        return user;
    }

    //== 비즈니스 로직 ==//
    public void changePassword(String password) {
        this.password = password;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

}
