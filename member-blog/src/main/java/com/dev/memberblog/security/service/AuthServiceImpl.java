package com.dev.memberblog.security.service;

import com.dev.memberblog.role.model.Role;
import com.dev.memberblog.role.repository.RoleRepository;
import com.dev.memberblog.security.CRefreshTokenRepository;
import com.dev.memberblog.security.dto.LoginDTO;
import com.dev.memberblog.security.dto.SignUpDTO;
import com.dev.memberblog.security.jwt.JwtHelper;
import com.dev.memberblog.security.model.RefreshToken;
import com.dev.memberblog.user.dto.UserDetailsDTO;
import com.dev.memberblog.user.dto.UserDetailsWithTokenDTO;
import com.dev.memberblog.user.mapper.UserMapper;
import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.model.UserStatus;
import com.dev.memberblog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CRefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetailsWithTokenDTO login(LoginDTO dto, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
        Authentication auth;
        try{
            auth = authenticationManager.authenticate(authentication);
        }catch (Exception e){
            return null;
        }

        if (auth == null)
            return null;

        Collection<SimpleGrantedAuthority> authorities = (Collection<SimpleGrantedAuthority>) auth.getAuthorities();

        String token = jwtHelper.generateTokenWithUsernameAndRoles(dto.getUsername(), authorities);

        if (token == null)
            return null;

        Optional<User> userOpt = userRepository.findByUsername(dto.getUsername());

        if(userOpt.isEmpty())
            return null;
        User user = userOpt.get();
        UserDetailsDTO userDetails =  UserMapper.INSTANCE.toUserDetailsDTO(user);

        //Set Refresh token for client
        String refreshTokenString = jwtHelper.generateRefreshTokenWithUsernameAndRoles(userDetails.getUsername(),authorities);
        RefreshToken refreshTokenEntity = RefreshToken.builder().refreshToken(refreshTokenString).user(user).build();
        refreshTokenRepository.save(refreshTokenEntity);

        ResponseCookie cookie = ResponseCookie.from("refreshToken",refreshTokenString)
                .httpOnly(true)
                .secure(true)
                .maxAge(30 * 24 * 60 * 60 )
                .sameSite("NONE")
                .path("/")
                .domain("localhost")
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return UserDetailsWithTokenDTO.builder().userDetail(userDetails).token(token).build();
    }
    @Override
    public boolean signup(SignUpDTO dto) {
        Optional<Role> defaultRole = roleRepository.findByCode("ROLE_USER");
        Set<Role> roles = new LinkedHashSet<Role>();
        roles.add(defaultRole.get());
        User newUser = User.builder()
                .displayName(dto.getUsername())
                .username(dto.getUsername())
                .phone(null)
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .status(UserStatus.ACTIVE)
                .roles(roles)
                .build();
        try {
            userRepository.save(newUser);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void logout(String refreshToken) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByRefreshToken(refreshToken);
        if(refreshTokenOpt.isEmpty())
            return;
        try{
            refreshTokenRepository.delete(refreshTokenOpt.get());
        }catch(Exception e){

        }
    }
}
