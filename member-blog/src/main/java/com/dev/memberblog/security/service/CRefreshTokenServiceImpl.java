package com.dev.memberblog.security.service;

import com.dev.memberblog.role.model.Role;
import com.dev.memberblog.security.CRefreshTokenRepository;
import com.dev.memberblog.security.jwt.JwtHelper;
import com.dev.memberblog.security.model.RefreshToken;
import com.dev.memberblog.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
public class CRefreshTokenServiceImpl implements CRefreshTokenService {
    @Autowired
    private CRefreshTokenRepository repository;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public String getToken(String refreshTokenString) {
        Optional<RefreshToken> refreshTokenOpt = repository.findByRefreshToken(refreshTokenString);

        if(refreshTokenOpt.isEmpty())
            return null;

        RefreshToken refreshToken = refreshTokenOpt.get();

        if(!jwtHelper.validationJwt(refreshToken.getRefreshToken())){
            repository.delete(refreshToken);
            return null;
        }
        User user = refreshToken.getUser();
        String token = null;
        Set<Role> roles = user.getRoles();

        if(roles != null){
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            roles.stream().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getCode())));
            token = jwtHelper.generateTokenWithUsernameAndRoles(user.getUsername(),authorities);
        }
        return token;
    }

    public void delete(String refreshToken) {
        Optional<RefreshToken> refreshTokenOpt = repository.findByRefreshToken(refreshToken);
        if(refreshTokenOpt.isEmpty())
            return;
        try{
            repository.delete(refreshTokenOpt.get());
        }catch(Exception e){
            return;
        }
    }
}
