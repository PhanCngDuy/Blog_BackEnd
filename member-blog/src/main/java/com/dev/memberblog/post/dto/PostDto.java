package com.dev.memberblog.post.dto;

import com.dev.memberblog.comment.dto.CommentDto;
import com.dev.memberblog.comment.model.Comment;
import com.dev.memberblog.post.model.PostLikes;
import com.dev.memberblog.user.dto.UserDTO;
import com.dev.memberblog.user.dto.UserDetailsDTO;
import com.dev.memberblog.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class PostDto {
    private String id;

    private LocalDateTime createAt;

    private String title;

    private String content;

    private String coverImage;

    private UserDetailsDTO user;

    private int view;

    private int comments;

    private List<String> userBookmarks;
    private List<String> likesId;
}
