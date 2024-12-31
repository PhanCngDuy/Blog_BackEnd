package com.dev.memberblog.user.dto;

import com.dev.memberblog.role.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@SuperBuilder
public class UserDetailsAndRolesDTO {
    private UserDetailsDTO userDetails;
    private List<Role> roles;
}
