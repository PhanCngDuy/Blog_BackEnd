package com.dev.memberblog.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class UserDetailsUpdateDTO {
    @NotBlank
    private String displayName;
    private String avatar;
    private String bio;

    @NotBlank
    private String email;
}
