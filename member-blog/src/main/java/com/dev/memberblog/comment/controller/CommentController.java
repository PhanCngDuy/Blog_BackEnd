package com.dev.memberblog.comment.controller;

import com.dev.memberblog.comment.dto.CommentDto;
import com.dev.memberblog.comment.dto.CommentReqDTO;
import com.dev.memberblog.comment.service.CommentService;
import com.dev.memberblog.common.helper.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    @Autowired
    private CommentService service;

    @PostMapping
    public Object comment(@Valid @RequestBody CommentReqDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult,HttpStatus.BAD_REQUEST);

        List<CommentDto> newDto = service.commentPost(dto);
        return ResponseHelper.getResponse(newDto, HttpStatus.OK);
    }
    @GetMapping
    public Object getCommentByPostId(@RequestParam(name = "postId") String postId){

        return ResponseHelper.getResponse(service.findAllCommentByPostId(postId),HttpStatus.OK);
    }

    @PostMapping("/delete/{commentId}")
    public Object deleteComment(@PathVariable String commentId){
        boolean result = service.deleteCommentById(commentId);
        if(!result){
            return  ResponseHelper.getErrorResponse("Delete failed",HttpStatus.BAD_REQUEST);
        }
        return ResponseHelper.getResponse("Deleted",HttpStatus.OK);
    }
}
