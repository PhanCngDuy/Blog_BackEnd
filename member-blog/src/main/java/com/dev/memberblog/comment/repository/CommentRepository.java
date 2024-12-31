package com.dev.memberblog.comment.repository;

import com.dev.memberblog.comment.model.Comment;
import com.dev.memberblog.post.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,String> {
    List<Comment> findAllByPostAndParentCommentIsNullOrderByCreateAtDesc(Post post);
}
