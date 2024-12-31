package com.dev.memberblog.comment.dto;

import com.dev.memberblog.user.dto.UserDTO;
import com.dev.memberblog.user.dto.UserDetailsDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class CommentDto {
    private String id;
    private LocalDateTime createAt;
    private String text;
    private UserDetailsDTO user;
    private List<CommentDto> childComments;
}
