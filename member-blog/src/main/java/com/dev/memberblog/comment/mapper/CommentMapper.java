package com.dev.memberblog.comment.mapper;

import com.dev.memberblog.comment.dto.CommentDto;
import com.dev.memberblog.comment.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);
    CommentDto toDto(Comment comment);
    Comment toEntity(CommentDto dto);
}
