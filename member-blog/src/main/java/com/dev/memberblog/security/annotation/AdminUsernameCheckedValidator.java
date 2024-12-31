package com.dev.memberblog.security.annotation;

import com.dev.memberblog.role.model.Role;
import com.dev.memberblog.user.model.User;
import com.dev.memberblog.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class AdminUsernameCheckedValidator  implements ConstraintValidator<AdminUsernameChecked, String> {
    @Autowired
    private UserRepository userRepository;
    private String message;

    @Override
    public void initialize(AdminUsernameChecked usernameChecked) {
        message = usernameChecked.message();
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty())
            return false;

        User user = userOptional.get();
        Role userRole = user.getRoles().stream().filter(role -> role.getCode().equals("ROLE_ADMIN")).findFirst().orElse(null);
        if(userRole != null)
            return true;

        return false;
    }
}
