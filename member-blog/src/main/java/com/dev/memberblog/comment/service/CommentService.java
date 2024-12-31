package com.dev.memberblog.comment.service;

import com.dev.memberblog.comment.dto.CommentDto;
import com.dev.memberblog.comment.dto.CommentReqDTO;

import java.util.List;

public interface CommentService {
    List<CommentDto> commentPost(CommentReqDTO dto);
    List<CommentDto> findAllCommentByPostId(String postId);

    boolean deleteCommentById(String commentId);
}
