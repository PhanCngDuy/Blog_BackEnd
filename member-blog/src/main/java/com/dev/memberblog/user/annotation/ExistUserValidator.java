package com.dev.memberblog.user.annotation;

import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ExistUserValidator implements ConstraintValidator<ExistUser, String> {
    private String message;

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(ExistUser existUser) {
        message = existUser.message();
    }

    @Override
    public boolean isValid(String id, ConstraintValidatorContext context) {
        Optional<User> userOpt = repository.findById(id);
        if (userOpt.isPresent())
            return true;

        context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
