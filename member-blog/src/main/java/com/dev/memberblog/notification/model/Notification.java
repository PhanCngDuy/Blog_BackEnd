package com.dev.memberblog.notification.model;

import com.dev.memberblog.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;


@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id",columnDefinition = "VARCHAR(36)")
    private String id;

    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createAt;

    @Column(name="content",nullable = false)
    private String content;

    @Column(name="body",nullable = false)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "receiver",nullable = false)
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.MERGE)
    @JoinColumn(name = "sender",referencedColumnName = "id")
    private User sender;

    @Enumerated(EnumType.STRING)
    @Column(name="type")
    private NotificationType type;

    @Column(name="path",nullable = false)
    private String path;

    @Column(name="is_read",columnDefinition = "TINYINT(1)")
    private boolean isRead;
}
