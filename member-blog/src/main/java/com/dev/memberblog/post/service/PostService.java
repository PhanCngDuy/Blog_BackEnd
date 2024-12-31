package com.dev.memberblog.post.service;

import com.dev.memberblog.common.dto.PagingDTO;
import com.dev.memberblog.post.dto.CreatePostDto;
import com.dev.memberblog.post.dto.PostDto;
import com.dev.memberblog.post.dto.PostUserIdDTO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PostService {
    CreatePostDto createPost(CreatePostDto dto, String userId);
    List<PostDto> findAllPostByUserId(String userId);

    PagingDTO latestPost( Integer page,Integer limit, String orderBy);

    PagingDTO findPopularPost(Integer page, Integer limit);

    PostDto getByTitle(String title);

    PagingDTO searching(String keyword, Integer page, Integer limit, String orderBy);

    List<String> likePost(PostUserIdDTO dto);

    PostDto updatePost(String postId, CreatePostDto dto);

    List<String> bookmarkPost(PostUserIdDTO dto);

    List<PostDto> getBookmark(String userId);

    boolean deletePost(String postId, HttpServletRequest request);
}
