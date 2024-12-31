package com.dev.memberblog.post.repository;

import com.dev.memberblog.post.model.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikes,String> {
}
