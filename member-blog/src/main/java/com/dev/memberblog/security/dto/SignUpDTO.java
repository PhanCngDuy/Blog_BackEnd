package com.dev.memberblog.security.dto;

import com.dev.memberblog.user.annotation.UniqueEmail;
import com.dev.memberblog.user.annotation.UniquePhone;
import com.dev.memberblog.user.annotation.UniqueUsername;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class SignUpDTO {
    @UniqueUsername(message="{user.username.existed}")
    @Size(min=5,max = 32,message = "{user.username.size}")
    private String username;

    @UniqueEmail(message="{user.email.existed}")
    @NotBlank(message="{user.email.notblank}")
    @Email(message="{user.email.notEmailType}")
    private String email;

    @NotBlank(message="{user.email.notblank}")
    @Size(min=8,max=100,message="{user.password.size}")
    private String password;
}
