package com.dev.memberblog.role.validation;

import com.dev.memberblog.role.model.Role;
import com.dev.memberblog.role.repository.RoleRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UniqueCodeValidator implements ConstraintValidator<UniqueCode, String> {

    private String message;

    @Autowired
    private RoleRepository repository;

    @Override
    public void initialize(UniqueCode uniqueName) {
        message = uniqueName.message();
    }
    @Override
    public boolean isValid(String roleCode, ConstraintValidatorContext context) {
        Optional<Role> roleOpt = repository.findByCode(roleCode);
        if(roleOpt.isEmpty())
            return true;
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
