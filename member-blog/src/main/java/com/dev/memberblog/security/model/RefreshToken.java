package com.dev.memberblog.security.model;

import com.dev.memberblog.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id",columnDefinition = "VARCHAR(36)")
    private String id;
    @Column(name="refresh_token",nullable = false,unique = true)
    private String refreshToken;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
