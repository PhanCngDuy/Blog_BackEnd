package com.dev.memberblog.user.repository;

import com.dev.memberblog.user.model.UserBookmark;
import com.dev.memberblog.user.model.UserBookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserBookmarkRepository extends JpaRepository<UserBookmark, UserBookmarkId> {

    @Query("SELECT u FROM UserBookmark u WHERE  u.id.user.id = ?1 ORDER BY u.createAt desc")
    Set<UserBookmark> findByUserId(String userId);
}
