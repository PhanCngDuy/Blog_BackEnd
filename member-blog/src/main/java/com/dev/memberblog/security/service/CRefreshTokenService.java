package com.dev.memberblog.security.service;

public interface CRefreshTokenService {
    String getToken(String  refreshTokenString);
}
