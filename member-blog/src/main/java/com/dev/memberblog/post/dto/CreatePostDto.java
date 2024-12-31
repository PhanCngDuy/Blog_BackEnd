package com.dev.memberblog.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class CreatePostDto {
    @NotBlank(message = "The title is required.")
    @Size(min = 30,message = "The title of the article must be at least 30 characters.")
    private String title;

    @NotBlank(message = "The content is required.")
    @Size(min = 50,message = "The content of the article must be at least 30 characters.")
    private String content;

    @NotBlank(message = "The cover image is required.")
    private String coverImage;
}
