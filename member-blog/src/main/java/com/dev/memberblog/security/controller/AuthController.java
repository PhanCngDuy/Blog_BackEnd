package com.dev.memberblog.security.controller;

import com.dev.memberblog.common.helper.ResponseHelper;
import com.dev.memberblog.security.dto.AdminLoginDTO;
import com.dev.memberblog.security.dto.LoginDTO;
import com.dev.memberblog.security.dto.SignUpDTO;
import com.dev.memberblog.security.service.AuthService;
import com.dev.memberblog.security.service.CRefreshTokenService;
import com.dev.memberblog.user.dto.UserDetailsWithTokenDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private CRefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public Object login(@Valid @RequestBody LoginDTO dto,
                        BindingResult bindingResult,
                        HttpServletResponse response) {
        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        UserDetailsWithTokenDTO userDetailsResponse = service.login(dto,response);

        if(userDetailsResponse == null)
            return ResponseHelper.getErrorResponse("Username or password is not correct.", HttpStatus.UNAUTHORIZED);


        return ResponseHelper.getResponse(userDetailsResponse,HttpStatus.OK);
    }
    @PostMapping("/login/admin")
    public Object adminLogin(@Valid @RequestBody AdminLoginDTO dto,
                        BindingResult bindingResult,
                        HttpServletResponse response) {
        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);
        UserDetailsWithTokenDTO userDetailsResponse = service.login(LoginDTO.builder().username(dto.getUsername()).password(dto.getPassword()).build(),response);

        if(userDetailsResponse == null)
            return ResponseHelper.getErrorResponse("Username or password is not correct.", HttpStatus.UNAUTHORIZED);

        return ResponseHelper.getResponse(userDetailsResponse,HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public Object signUp(@Valid @RequestBody SignUpDTO dto, BindingResult bindingResult ) {
        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult, HttpStatus.BAD_REQUEST);

        boolean result = service.signup(dto);
        if(!result)
            return ResponseHelper.getErrorResponse("Sign up failure!",HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse("Sign up successfully.", HttpStatus.OK);
    }

    @GetMapping("/get-new-token")
    public Object refreshToken(@CookieValue(name="refreshToken",required = false) String refreshToken,
                               HttpServletResponse response){
        String token = refreshTokenService.getToken(refreshToken);

        if(token == null){
            Cookie cookie = new Cookie("refreshToken",null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);

            return ResponseHelper.getErrorResponse("Your session has expired.Please login again.",HttpStatus.UNAUTHORIZED);
        }
        return ResponseHelper.getResponse(token, HttpStatus.OK);
    }
    @PostMapping("/logout")
    public Object logout(@CookieValue(name="refreshToken",required = false) String refreshTokenCookie,
                         HttpServletResponse response,
                         HttpServletRequest request){
        String refreshToken = refreshTokenCookie;

        Cookie cookie = new Cookie("refreshToken",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        service.logout(refreshToken);

        return ResponseHelper.getResponse("Logout successfully!!",HttpStatus.OK);
    }
}
