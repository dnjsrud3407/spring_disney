package com.tistory.dnjsrud.disney.poster;

import com.tistory.dnjsrud.disney.global.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

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
    private Long fileSize;

    public Poster(String originalFileName, String storedFileName, Long fileSize) {
        this.originalFileName = originalFileName;
        this.storedFileName = storedFileName;
        this.fileSize = fileSize;
    }

    //== 생성 메서드 ==//
    public static Poster createPoster(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        Long fileSize = file.getSize();

        return new Poster(originalFilename, storeFileName, fileSize);
    }

    public static String createStoreFileName(String originalFilename) {
        int post = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(post + 1);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    //== 비즈니스 로직 ==//
    public void changePoster(MultipartFile file) {
        this.originalFileName = file.getOriginalFilename();
        this.storedFileName = createStoreFileName(this.originalFileName);
        this.fileSize = file.getSize();
    }

}