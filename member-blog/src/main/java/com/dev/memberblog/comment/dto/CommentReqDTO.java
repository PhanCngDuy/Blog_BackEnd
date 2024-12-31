package com.dev.memberblog.comment.dto;

import com.dev.memberblog.post.annotation.ExistPost;
import com.dev.memberblog.user.annotation.ExistUser;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class CommentReqDTO {
    @ExistUser
    private String userId;
    @ExistPost
    private String postId;
    private String text;
    private String parentCommentId;
}
