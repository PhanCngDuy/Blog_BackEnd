package com.dev.memberblog.user.controller;

import com.dev.memberblog.common.helper.ResponseHelper;
import com.dev.memberblog.user.dto.UserDetailsAndRolesDTO;
import com.dev.memberblog.user.dto.UserDetailsDTO;
import com.dev.memberblog.user.dto.UserDetailsUpdateDTO;
import com.dev.memberblog.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserService service;

    @GetMapping
    public Object findAllUser(@RequestParam(name="limit",required = false,defaultValue = "10") Integer limit,
                              @RequestParam(name="page",required = false,defaultValue = "1") Integer page,
                              @RequestParam(name="order-by",required = false,defaultValue = "createAt:desc") String orderBy) {
        return ResponseHelper.getResponse(service.findAll(page,limit,orderBy), HttpStatus.OK);
    }

    @GetMapping("/detail/{user-id}")
    public Object getUserDetail(@PathVariable(name="user-id") String userId) {
        UserDetailsDTO dto = service.getUserDetail(userId);

        return ResponseHelper.getResponse(dto, HttpStatus.OK);
    }

    @PutMapping("/update/{user-id}")
    public Object updateUserDetails(@PathVariable(name="user-id") String userId,
                                    @Valid @RequestBody UserDetailsUpdateDTO dto,
                                    BindingResult bindingResult) {

        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult,HttpStatus.BAD_REQUEST);

        UserDetailsDTO newDetails = service.updateUserDetails(userId,dto);
        if(newDetails == null)
            return ResponseHelper.getErrorResponse("Something wrong.", HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(newDetails, HttpStatus.OK);
    }



}
