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
    private String genre;

    @NotNull
    private boolean visible;

    public Genre(String genre, boolean visible) {
        this.genre = genre;
        this.visible = visible;
    }
}
