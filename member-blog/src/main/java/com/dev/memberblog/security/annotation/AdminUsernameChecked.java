package com.dev.memberblog.security.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AdminUsernameCheckedValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminUsernameChecked {
    String message() default "User dont have permission.Please connect to administrator.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
