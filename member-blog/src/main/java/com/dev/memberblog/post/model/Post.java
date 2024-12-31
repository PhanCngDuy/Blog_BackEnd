package com.dev.memberblog.post.model;

import com.dev.memberblog.comment.model.Comment;
import com.dev.memberblog.common.model.BaseEntity;
import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.model.UserBookmark;
import com.dev.memberblog.user.model.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name="post")
public class Post extends BaseEntity {
    @Column(name = "title",nullable = false,length=100)
    private String title;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "cover_image",columnDefinition = "LONGTEXT")
    private String coverImage;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="user_id",nullable = false)
    private User user;


    @JoinColumn(name="view")
    private int view;

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<PostLikes> likes = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "id.post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBookmark> bookmarks = new LinkedHashSet<>();

    public void removeLikes(PostLikes like){
        likes.remove(like);
    }
    public void addLikes(PostLikes like){
        likes.add(like);
        like.setPost(this);
    }
}
