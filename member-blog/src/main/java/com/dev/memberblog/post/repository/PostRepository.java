package com.dev.memberblog.post.repository;

import com.dev.memberblog.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    @Query("SELECT p FROM Post p")
    Page<Post> findAllPaging(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:title% ORDER BY LOCATE(:title, p.title)")
    Page<Post> findByTitleContains(Pageable pageable,String title);

    Optional<Post> findByTitle(String title);
}
