package com.dev.memberblog.user.annotation;

import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueEmailValidator  implements ConstraintValidator<UniqueEmail, String> {
    private String message;

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UniqueEmail uniqueEmail) {
        message = uniqueEmail.message();
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        Optional<User> userOpt = repository.findByEmail(email);
        if (userOpt.isEmpty())
            return true;

        context.buildConstraintViolationWithTemplate(message).addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
