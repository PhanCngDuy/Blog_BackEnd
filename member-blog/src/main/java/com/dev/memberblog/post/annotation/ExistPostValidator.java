package com.dev.memberblog.post.annotation;

import com.dev.memberblog.post.model.Post;
import com.dev.memberblog.post.repository.PostRepository;
import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ExistPostValidator implements ConstraintValidator<ExistPost, String> {
    private String message;

    @Autowired
    private PostRepository repository;

    @Override
    public void initialize(ExistPost existUser) {
        message = existUser.message();
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        Optional<Post> postOpt = repository.findById(id);
        if (postOpt.isPresent())
            return true;

        context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
