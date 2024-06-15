package com.lautadev.microservice_user.Throwable;

import com.lautadev.microservice_user.model.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserValidator {
    private final Validator validator;

    public UserValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public void validate(User user) {
        if (user == null) {
            throw new UserException("User cannot be null");
        }

        // Validaciones estándar usando Hibernate Validator
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<User> violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
            throw new UserException(sb.toString());
        }

    }
}
