package com.dev.memberblog.security;

import com.dev.memberblog.security.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CRefreshTokenRepository extends JpaRepository<RefreshToken,String> {
    Optional<RefreshToken> findByRefreshToken(String refreshTokenString);
}
