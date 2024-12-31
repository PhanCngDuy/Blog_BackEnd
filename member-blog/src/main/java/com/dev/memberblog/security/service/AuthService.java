package com.dev.memberblog.security.service;

import com.dev.memberblog.security.dto.LoginDTO;
import com.dev.memberblog.security.dto.SignUpDTO;
import com.dev.memberblog.user.dto.UserDetailsWithTokenDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    UserDetailsWithTokenDTO login(LoginDTO dto, HttpServletResponse response);

    boolean signup(SignUpDTO dto);

    void logout(String refreshToken);
}
