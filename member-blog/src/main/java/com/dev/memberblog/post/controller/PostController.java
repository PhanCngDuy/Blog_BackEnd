package com.dev.memberblog.post.controller;

import com.dev.memberblog.common.helper.ResponseHelper;
import com.dev.memberblog.post.dto.CreatePostDto;
import com.dev.memberblog.post.dto.PostDto;
import com.dev.memberblog.post.dto.PostUserIdDTO;
import com.dev.memberblog.post.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {
    @Autowired
    private PostService service;

    @GetMapping
    public Object getPost(@RequestParam(name="title") String title){
        PostDto post = service.getByTitle(title);
        if(post == null)
            return ResponseHelper.getErrorResponse("Not Found",HttpStatus.NOT_FOUND);

        return ResponseHelper.getResponse(post,HttpStatus.OK);
    }

    @GetMapping("/latest")
    public Object latestPost(
                              @RequestParam(name="limit",required = false,defaultValue = "10") Integer limit,
                              @RequestParam(name="page",required = false,defaultValue = "1") Integer page,
                              @RequestParam(name="order-by",required = false,defaultValue = "createAt") String orderBy){

        return ResponseHelper.getResponse(service.latestPost(page,limit,orderBy),HttpStatus.OK);
    }
    @GetMapping("/popular")
    public Object popularPost(
            @RequestParam(name="limit",required = false,defaultValue = "5") Integer limit,
            @RequestParam(name="page",required = false,defaultValue = "1") Integer page){

        return ResponseHelper.getResponse(service.findPopularPost(page,limit),HttpStatus.OK);
    }

    @GetMapping("/my/{user-id}")
    public Object findAllPostByUserId(@PathVariable(name="user-id") String userId){
        List<PostDto> postDtos = service.findAllPostByUserId(userId);
        if(postDtos == null)
            return ResponseHelper.getErrorResponse("User is not existed.",HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(postDtos,HttpStatus.OK);
    }
    @GetMapping("/search")
    public Object searchingWithFilter(@RequestParam(name = "keyword") String keyword,
                                      @RequestParam(name="limit",required = false,defaultValue = "10") Integer limit,
                                      @RequestParam(name="page",required = false,defaultValue = "1") Integer page,
                                      @RequestParam(name="order-by",required = false,defaultValue = "title") String orderBy){
        return ResponseHelper.getResponse(service.searching(keyword,page,limit,orderBy),HttpStatus.OK);
    }
    @PostMapping("/{user-id}")
    public Object createPost(@PathVariable(name="user-id") String userId,@Valid @RequestBody CreatePostDto dto,BindingResult bindingResult) {
        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult,HttpStatus.BAD_REQUEST);
        CreatePostDto newPost =service.createPost(dto,userId);
        if(newPost == null)
            return ResponseHelper.getErrorResponse("Something wrong!",HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(newPost, HttpStatus.OK);
    }
    @PostMapping("/like")
    public Object likePost(@Valid @RequestBody PostUserIdDTO dto, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult,HttpStatus.BAD_REQUEST);

        return ResponseHelper.getResponse(service.likePost(dto),HttpStatus.OK);
    }
    @PostMapping("/bookmark")
    public Object bookmark(@RequestBody PostUserIdDTO dto){

        return ResponseHelper.getResponse(service.bookmarkPost(dto),HttpStatus.OK);
    }
    @GetMapping("/get-bookmark/{userId}")
    public Object getBookmark(@PathVariable(name="userId") String userId){
        return ResponseHelper.getResponse(service.getBookmark(userId),HttpStatus.OK);
    }

    @PutMapping("/update/{postId}")
    public Object updatePost(@PathVariable String postId,@Valid @RequestBody CreatePostDto dto,BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return ResponseHelper.getErrorResponse(bindingResult,HttpStatus.BAD_REQUEST);

        PostDto isUpdated = service.updatePost(postId,dto);
        if(isUpdated == null)
            return  ResponseHelper.getResponse("Update failure. Something wrong",HttpStatus.BAD_REQUEST);
        return ResponseHelper.getResponse(isUpdated,HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public Object deletePost(@PathVariable(name="postId") String postId, HttpServletRequest request){

        boolean isDeleted =service.deletePost(postId,request);

        return ResponseHelper.getResponse(isDeleted,HttpStatus.OK);
    }
}
