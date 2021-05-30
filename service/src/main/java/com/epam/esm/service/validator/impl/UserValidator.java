package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.security.RegisterRequest;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserValidator extends EntityValidator<RegisterRequest> {

    private final static String NAME_FIELD = "userName";
    private final static String PASSWORD_FIELD = "password";
    private final static String EMAIL_FIELD = "email";

    private final NameValidator nameValidator;
    private final PasswordValidator passwordValidator;
    private final EmailValidator emailValidator;


    @Autowired
    public UserValidator(NameValidator nameValidator, PasswordValidator passwordValidator,
                         EmailValidator emailValidator) {
        this.nameValidator = nameValidator;
        this.passwordValidator = passwordValidator;
        this.emailValidator = emailValidator;
    }

    @Override
    public void validate(RegisterRequest registerRequest) {
        if (registerRequest == null){
            throw new ValidationException(ErrorCode.BAD_REQUEST);
        }
        validateField(nameValidator,
                registerRequest.getUserName(), ErrorCode.USER_FIELD_INVALID,
                NAME_FIELD, registerRequest.getUserName());
        validateField(passwordValidator,
                registerRequest.getPassword(), ErrorCode.USER_FIELD_INVALID,
                PASSWORD_FIELD, registerRequest.getPassword());
        validateField(emailValidator,
                registerRequest.getEmail(), ErrorCode.USER_FIELD_INVALID,
                EMAIL_FIELD, registerRequest.getEmail());
    }
}
