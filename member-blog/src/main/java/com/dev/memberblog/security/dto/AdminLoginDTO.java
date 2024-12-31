package com.dev.memberblog.security.dto;

import com.dev.memberblog.security.annotation.AdminUsernameChecked;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class AdminLoginDTO {
    @Size(min=6,max = 32,message = "{user.username.size}")
    @NotBlank
    @AdminUsernameChecked
    private String username;

    @Size(min=6,max = 32,message = "{user.password.size}")
    @NotBlank
    private String password;
}
