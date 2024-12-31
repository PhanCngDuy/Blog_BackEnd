package com.dev.memberblog.comment.service;

import com.dev.memberblog.comment.dto.CommentDto;
import com.dev.memberblog.comment.dto.CommentReqDTO;
import com.dev.memberblog.comment.mapper.CommentMapper;
import com.dev.memberblog.comment.model.Comment;
import com.dev.memberblog.comment.repository.CommentRepository;
import com.dev.memberblog.notification.dto.NotificationDTO;
import com.dev.memberblog.notification.model.Notification;
import com.dev.memberblog.notification.model.NotificationType;
import com.dev.memberblog.notification.service.NotificationService;
import com.dev.memberblog.post.model.Post;
import com.dev.memberblog.post.repository.PostRepository;
import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private  SimpMessagingTemplate messagingTemplate;


    @Override
    public List<CommentDto> commentPost(CommentReqDTO dto) {
        Optional<Post> postOpt = postRepository.findById(dto.getPostId());
        Optional<User> userOpt = userRepository.findById(dto.getUserId());
        Post post = postOpt.get();
        User user = userOpt.get();

        Comment newComment = Comment.builder().text(dto.getText()).user(user).post(post).build();
        String parentCommentId = dto.getParentCommentId();
        String userIdNotified = "";

        if(parentCommentId != null || !parentCommentId.isEmpty()){
            Optional<Comment> parentCommentOpt = commentRepository.findById(parentCommentId);
            if(parentCommentOpt.isPresent()){
                Comment parentComment = parentCommentOpt.get();
                newComment.setParentComment(parentComment);
                if(!parentCommentId.equals(user.getId()) && !parentCommentId.equals(user.getId())){
                    NotificationDTO notificationDTO = notificationService.create(Notification.builder()
                            .sender(user)
                            .receiver(parentComment.getUser())
                            .createAt(LocalDateTime.now())
                            .body("Replied to your comment")
                            .content(dto.getText())
                            .isRead(false)
                            .path(post.getTitle())
                            .type(NotificationType.COMMENT)
                            .build());
                    userIdNotified = parentComment.getUser().getId();
                    messagingTemplate.convertAndSend("/topic/notification/" + parentComment.getUser().getId(),notificationDTO);

                }
            }
        }

        String postUserId = post.getUser().getId();
        if(!postUserId.equals(user.getId()) && !userIdNotified.equals(postUserId)){
            NotificationDTO notificationDTO = notificationService.create(Notification.builder()
                    .sender(user)
                    .receiver(post.getUser())
                    .createAt(LocalDateTime.now())
                    .content(dto.getText())
                    .body("Commented on your post")
                    .isRead(false)
                    .path(post.getTitle())
                    .type(NotificationType.COMMENT)
                    .build());
            messagingTemplate.convertAndSend("/topic/notification/" + postUserId,notificationDTO);
       }
        commentRepository.save(newComment);

        List<Comment> comments = commentRepository.findAllByPostAndParentCommentIsNullOrderByCreateAtDesc(post);
        sortCommentsRecursively(comments);
        return comments.stream().map(comment -> CommentMapper.INSTANCE.toDto(comment)).collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> findAllCommentByPostId(String postId) {
        Optional<Post> postOpt = postRepository.findById(postId);
        if(postOpt.isEmpty())
            return null;
        List<Comment> comments = commentRepository.findAllByPostAndParentCommentIsNullOrderByCreateAtDesc(postOpt.get());

        sortCommentsRecursively(comments);
        return comments.stream().map(comment -> CommentMapper.INSTANCE.toDto(comment)).collect(Collectors.toList());
    }

    @Override
    public boolean deleteCommentById(String commentId) {
        try {
            commentRepository.deleteById(commentId);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private void sortCommentsRecursively(List<Comment> comments) {
        comments.sort(Comparator.comparing(Comment::getCreateAt).reversed());
        for (Comment comment : comments) {
            if(comment.getChildComments() != null)
            sortCommentsRecursively(comment.getChildComments());
        }
    }
}
