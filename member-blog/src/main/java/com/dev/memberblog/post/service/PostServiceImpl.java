package com.dev.memberblog.post.service;

import com.dev.memberblog.comment.model.Comment;
import com.dev.memberblog.common.dto.PagingDTO;
import com.dev.memberblog.files.upload.FilesStorageService;
import com.dev.memberblog.post.dto.CreatePostDto;
import com.dev.memberblog.post.dto.PostDto;
import com.dev.memberblog.post.dto.PostUserIdDTO;
import com.dev.memberblog.post.model.Post;
import com.dev.memberblog.post.model.PostLikes;
import com.dev.memberblog.post.repository.PostLikeRepository;
import com.dev.memberblog.post.repository.PostRepository;
import com.dev.memberblog.security.dto.UsernameAndRoles;
import com.dev.memberblog.security.jwt.JwtHelper;
import com.dev.memberblog.user.mapper.UserMapper;
import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.model.UserBookmark;
import com.dev.memberblog.user.model.UserBookmarkId;
import com.dev.memberblog.user.repository.UserBookmarkRepository;
import com.dev.memberblog.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService{
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private UserBookmarkRepository userBookmarkRepository;

    @Autowired
    private JwtHelper helper;
    @Override
    public CreatePostDto createPost(CreatePostDto dto, String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty())
            return null;

        Document docContent = Jsoup.parse(dto.getContent());
        Elements imgElements = docContent.select("img");

        for (Element imgElement : imgElements) {
            String srcValue = imgElement.attr("src");

            String fileName = filesStorageService.uploadImage(srcValue);
            if(fileName != null)
                imgElement.attr("src", fileName);
        }
        String coverImage = dto.getCoverImage();
        if(!coverImage.isEmpty()){
            String fileName = filesStorageService.uploadImage(coverImage);
            if(fileName != null)
                coverImage = fileName;
        }

        Post post = postRepository.save(Post.builder()
                .title(dto.getTitle())
                .content(docContent.body().html())
                .coverImage(coverImage)
                .user(userOpt.get())
                 .view(0)
                .build());

        return CreatePostDto.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .coverImage(post.getCoverImage())
                .build();
    }

    @Override
    public List<PostDto> findAllPostByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isEmpty())
            return null;
        User user = userOpt.get();
        Set<Post> myPost = user.getPosts();
        return myPost
                .stream()
                .sorted(Comparator.comparing(Post::getCreateAt).reversed())
                .map(post -> toPostDto(post))
                .collect(Collectors.toList());
    }
    public PostDto toPostDto(Post post){
        return PostDto.builder()
                        .id(post.getId())
                        .createAt(post.getCreateAt())
                        .user(UserMapper.INSTANCE.toUserDetailsDTO(post.getUser()))
                        .title(post.getTitle())
                        .content(post.getContent())
                        .coverImage(post.getCoverImage())
                        .view(post.getView())
                        .comments(post.getComments().size())
                        .userBookmarks(post.getBookmarks().stream().map(b -> b.getId().getUser().getId()).collect(Collectors.toList()))
                        .likesId(post.getLikes().stream().map(u -> u.getUser().getId()).collect(Collectors.toList()))
                        .build();
    }
    @Override
    public PagingDTO latestPost(Integer page,Integer limit, String orderBy) {
        Sort sort = getSort(orderBy);
        Pageable pageable = PageRequest.of(page - 1, limit, sort);
        Page<Post> postPage = postRepository.findAllPaging(pageable);

        List<PostDto> data = postPage.stream().map(post -> toPostDto(post)).collect(Collectors.toList());
        return PagingDTO.builder()
                .data(data)
                .limit(limit)
                .page(page)
                .total(postPage.getTotalPages())
                .build();
    }

    @Override
    public PagingDTO findPopularPost(Integer page, Integer limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC,"view"));
        Page<Post> postPage = postRepository.findAllPaging(pageable);

        List<PostDto> data = postPage.stream().map(post -> toPostDto(post)).collect(Collectors.toList());
        return PagingDTO.builder()
                .data(data)
                .limit(limit)
                .page(page)
                .total(postPage.getTotalElements())
                .build();
    }

    @Override
    public PostDto getByTitle(String title) {
        Optional<Post> postOpt = postRepository.findByTitle(title);
        if(postOpt.isEmpty())
            return null;
        Post post = postOpt.get();
        post.setView(post.getView() + 1);
        postRepository.save(post);
        return toPostDto(postOpt.get());
    }

    @Override
    public PagingDTO searching(String keyword, Integer page, Integer limit, String orderBy) {
        Sort sort = getSort(orderBy);
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Post> postPage = postRepository.findByTitleContains(pageable,keyword);
        List<PostDto> data = postPage.stream().map(post -> toPostDto(post)).collect(Collectors.toList());
        return PagingDTO.builder()
                .data(data)
                .limit(limit)
                .page(page)
                .total(postPage.getTotalPages())
                .build();
    }

    @Override
    public List<String> likePost(PostUserIdDTO dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        Optional<Post> postOpt = postRepository.findById(dto.getPostId());

        Post post = postOpt.get();
        User user = userOpt.get();
        Optional<PostLikes> firstMatchingLike = post.getLikes()
                                                    .stream()
                                                    .filter(u -> u.getUser().getId().equals(user.getId())).findFirst();
        try{
            if(firstMatchingLike.isEmpty()){
                PostLikes postLikes = PostLikes.builder().user(user).post(post).createAt(LocalDateTime.now()).build();
                post.addLikes(postLikes);
                user.addLikes(postLikes);
                post.getUser().increaseLike();
                postLikeRepository.save(postLikes);
            }
            else{
                PostLikes postLikes = firstMatchingLike.get();
                user.removeLikes(postLikes);
                post.removeLikes(postLikes);
                post.getUser().decreaseLike();
                postLikeRepository.delete(postLikes);
            }

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
        }


        return post.getLikes().stream().map(l -> l.getUser().getId()).collect(Collectors.toList());

    }

    @Override
    public PostDto updatePost(String postId, CreatePostDto dto) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if(postOpt.isEmpty())
            return null;

        Post post = postOpt.get();

        if(dto.getCoverImage() != null && !dto.getCoverImage().equals(post.getCoverImage())){
            String fileName = filesStorageService.uploadImage(dto.getCoverImage());
            if(fileName != null){
                filesStorageService.delete(post.getCoverImage());
                post.setCoverImage(fileName);

            }
        }
        if(dto.getContent() != null && !dto.getContent().equals(post.getContent())){
            Document docContentDto = Jsoup.parse(dto.getContent());
            Elements imgElementsDto = docContentDto.select("img");
            Elements imgElementPost = Jsoup.parse(post.getContent()).select("img");
            List<String> fileNameDtos = new LinkedList<>();
            
            for (Element imgElement : imgElementsDto) {
                String srcValue = imgElement.attr("src");
                
                if(srcValue.startsWith("data:image")){
                    String fileName = filesStorageService.uploadImage(srcValue);
                    if(fileName != null)
                        imgElement.attr("src", fileName);
                }else{
                    fileNameDtos.add(srcValue);
                }
            }
            for (Element oldElement : imgElementPost) {
                String srcValue = oldElement.attr("src");
                String file = fileNameDtos.stream().filter(t -> t.equals(srcValue)).findFirst().orElse(null);
                if(file == null)
                    filesStorageService.delete(srcValue);
            }
            post.setContent(docContentDto.body().html());
        }
        post.setTitle(dto.getTitle());
        Post newPost = postRepository.save(post);
        return toPostDto(newPost);
    }

    @Override
    public List<String> bookmarkPost(PostUserIdDTO dto) {
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        Optional<Post> postOpt = postRepository.findById(dto.getPostId());
        if(userOpt.isEmpty() || postOpt.isEmpty()) return Collections.emptyList();;

        Post post = postOpt.get();
        User user = userOpt.get();
        UserBookmarkId bookmarkId = new UserBookmarkId(user, post);

        Optional<UserBookmark> existingBookmark = userBookmarkRepository.findById(bookmarkId);

            if (existingBookmark.isPresent()) {
            UserBookmark userBookmark = existingBookmark.get();
            userBookmarkRepository.delete(userBookmark);
        } else {
            UserBookmark userBookmark = UserBookmark.builder().id(bookmarkId).createAt(LocalDateTime.now()).build();
            userBookmarkRepository.save(userBookmark);
        }


        return post.getBookmarks().stream().map(b -> b.getId().getUser().getId()).collect(Collectors.toList());
    }

    @Override
    public List<PostDto> getBookmark(String userId) {
        Set<UserBookmark> listBookmark =  userBookmarkRepository.findByUserId(userId);
        return listBookmark.stream().map(b -> toPostDto(b.getId().getPost())).collect(Collectors.toList());
    }

    @Override
    public boolean deletePost(String postId, HttpServletRequest request) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if(postOpt.isEmpty()) return false;
        Post post = postOpt.get();
        String token = helper.getToken(request);
        UsernameAndRoles currentUser = helper.getUsernameAndRoleFromToken(token);
        if(!post.getUser().getUsername().equals(currentUser.getUsername())) return false;

        Document docContent = Jsoup.parse(post.getContent());
        Elements imgElements = docContent.select("img");

        for (Element imgElement : imgElements) {
            String srcValue = imgElement.attr("src");
            filesStorageService.delete(srcValue);
        }
        String coverImage = post.getCoverImage();
        if(!coverImage.isEmpty()){
            filesStorageService.delete(coverImage);
        }

        post.getComments().clear();
        post.getLikes().clear();
        post.getBookmarks().clear();

        postRepository.delete(post);

        return true;
    }

    public Sort getSort(String orderBy){
        Sort sort = null;
        if (orderBy != null) {
            String[] orderByParts = orderBy.split(":");
            if (orderByParts.length == 2) {
                String sortBy = orderByParts[0];
                String sortOrder = orderByParts[1].toLowerCase();

                Sort.Direction direction = sortOrder.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                sort = Sort.by(direction, sortBy);
            } else {
                sort = Sort.by(Sort.Direction.DESC,orderBy);
            }
        }
        return sort;
    }
}
