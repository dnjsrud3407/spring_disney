package com.tistory.dnjsrud.disney.domain;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Genre {

    @Id @GeneratedValue
    @Column(name = "genre_id")
    private Long id;

    @NotNull @Column(unique = true)
    private String genreName;

    @NotNull
    private boolean isVisible;

    public Genre(String genreName, boolean isVisible) {
        this.genreName = genreName;
        this.isVisible = isVisible;
    }

    //== 비즈니스 로직 ==//
    public void changeVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
}
