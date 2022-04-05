package com.tistory.dnjsrud.disney.poster;

import com.tistory.dnjsrud.disney.global.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Poster extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "poster_id")
    private Long id;

    @NotNull
    private String originalFileName;

    @NotNull
    private String storedFileName;

    @NotNull
    private String fileFullPath;

    @NotNull
    private Long fileSize;

    public Poster(String originalFileName, String storedFileName, String fileFullPath, Long fileSize) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileFullPath = fileFullPath;
        this.fileSize = fileSize;
    }

}