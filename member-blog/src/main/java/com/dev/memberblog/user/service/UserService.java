package com.dev.memberblog.user.service;
import com.dev.memberblog.common.dto.PagingDTO;
import com.dev.memberblog.user.dto.UserDetailsAndRolesDTO;
import com.dev.memberblog.user.dto.UserDetailsDTO;
import com.dev.memberblog.user.dto.UserDetailsUpdateDTO;

public interface UserService {

    PagingDTO findAll(int page, int limit, String orderBy);

    UserDetailsDTO getUserDetail(String userId);
    UserDetailsDTO getUserDetailsByUsername(String username);

    UserDetailsDTO updateUserDetails(String userId, UserDetailsUpdateDTO dto);

    UserDetailsAndRolesDTO updateUser(String userId, UserDetailsAndRolesDTO dto);
}
