package com.dev.memberblog.post.model;

import com.dev.memberblog.common.model.BaseEntity;
import com.dev.memberblog.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name="post_like")
public class PostLikes {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id",columnDefinition = "VARCHAR(36)")
    private String id;

    @JsonIgnore
    @CreatedDate
    @Column(name="created_at")
    protected LocalDateTime createAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
