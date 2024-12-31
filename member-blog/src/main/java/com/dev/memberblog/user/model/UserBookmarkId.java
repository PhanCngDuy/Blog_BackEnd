package com.dev.memberblog.user.model;

import com.dev.memberblog.post.model.Post;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class UserBookmarkId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "post_id")
    private Post post;
}
