package com.dev.memberblog.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserDetailsWithTokenDTO {
    private UserDetailsDTO userDetail;
    private String token;
}
