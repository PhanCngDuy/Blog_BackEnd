package com.dev.memberblog.post.dto;

import com.dev.memberblog.post.annotation.ExistPost;
import com.dev.memberblog.user.annotation.ExistUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class PostUserIdDTO {
    @ExistUser
    private String userId;
    @ExistPost
    private String postId;
}
