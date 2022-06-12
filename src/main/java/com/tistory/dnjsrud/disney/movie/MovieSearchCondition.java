package com.tistory.dnjsrud.disney.movie;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
public class MovieSearchCondition {
    private String title;
    private Long genreId;
}
