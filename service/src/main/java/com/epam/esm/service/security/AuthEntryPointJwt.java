package com.epam.esm.service.security;

import com.epam.esm.service.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Autowired
    private MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                messageSource.getMessage(ErrorCode.UNAUTHORIZED_USER.getMessageCode(), new Object[] {}, Locale.getDefault()));
    }
}
