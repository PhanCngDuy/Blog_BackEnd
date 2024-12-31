package com.dev.memberblog.role.service;

import com.dev.memberblog.role.dto.RoleDTO;
import com.dev.memberblog.role.model.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
    RoleDTO save(RoleDTO role);
    boolean removeRole(String roleId);
    RoleDTO updateRole(String roleId, RoleDTO dto);
}
