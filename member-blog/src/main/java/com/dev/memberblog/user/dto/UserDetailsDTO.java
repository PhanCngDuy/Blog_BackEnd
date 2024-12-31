package com.dev.memberblog.user.dto;

import com.dev.memberblog.user.annotation.UniqueEmail;
import com.dev.memberblog.user.annotation.UniqueUsername;
import com.dev.memberblog.user.model.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class UserDetailsDTO {
    private String id;
    @NotBlank(message="{user.username.notBlank}")
    @UniqueUsername(message="{user.username.existed}")
    private String username;

    @NotBlank
    private String displayName;

    @Email(message = "{user.email.notEmailType}")
    @UniqueEmail(message="{user.email.existed}")
    private String email;

    private String avatar;
    private String bio;
    private int likes;

}
