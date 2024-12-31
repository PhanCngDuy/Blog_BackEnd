package com.dev.memberblog.role.model;

import com.dev.memberblog.common.model.BaseEntity;
import com.dev.memberblog.user.model.User;
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
@Entity
@NoArgsConstructor
@Table(name = "role")
public class Role extends BaseEntity {
    @Column(name="code",nullable = false,unique = true)
    private String code;

    @Column(name="description")
    private String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    private Set<User> users = new LinkedHashSet<User>();
}
