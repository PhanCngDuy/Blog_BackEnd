package com.dev.memberblog.user.model;

import com.dev.memberblog.comment.model.Comment;
import com.dev.memberblog.common.model.BaseEntity;
import com.dev.memberblog.notification.model.Notification;
import com.dev.memberblog.post.model.Post;
import com.dev.memberblog.post.model.PostLikes;
import com.dev.memberblog.role.model.Role;
import com.dev.memberblog.security.model.RefreshToken;
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
@Table(name="user")
public class User extends BaseEntity {
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name="user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new LinkedHashSet<Role>();

    @Column(name = "username",unique = true,nullable = false,length=100)
    private String username;

    @JsonIgnore
    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "display_name",nullable = false)
    private String displayName;

    @Column(name = "email",nullable = false,unique = true,length = 255)
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "phone")
    private String phone;

    @Column(name = "bio")
    private String bio;

    @Column(name = "status")
    private UserStatus status;

    @Column(name = "likes")
    private int likes;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<RefreshToken> refreshTokens = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Post> posts = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<Comment> comments = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private Set<PostLikes> postLikes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "id.user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserBookmark> bookmarks = new LinkedHashSet<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> receiverNotifications = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Notification> senderNotifications = new LinkedHashSet<>();

    public void removeLikes(PostLikes like){
        postLikes.remove(like);
    }
    public void addLikes(PostLikes like){
        postLikes.add(like);
        like.setUser(this);
    }
    public void addRole(Role role){
        roles.add(role);
        role.getUsers().add(this);
    }
    public void removeRole(Role role){
        roles.remove(role);
        role.getUsers().remove(this);
    }
    public void addPost(Post post){
        posts.add(post);
        post.setUser(this);
    }

    public void increaseLike() {
        ++likes;
    }
    public void decreaseLike() {
        --likes;
    }

}
