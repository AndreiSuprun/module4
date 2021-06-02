package com.epam.esm.service.security;

import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Autowired
    private MessageSource messageSource;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (ValidationException e) {
            setErrorResponse(e.getErrorCode(), response, e.getErrorCode().getMessageCode());
        } catch (RuntimeException e) {
            setErrorResponse(ErrorCode.BAD_REQUEST, response, ErrorCode.BAD_REQUEST.getMessageCode());
        }
    }

    public void setErrorResponse(ErrorCode status, HttpServletResponse response, String message) throws IOException {
        response.setStatus(status.getCode());
        response.setContentType("application/json");
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(status.getCode().toString());
            json = json.concat(String.join("/n", mapper.writeValueAsString(messageSource.getMessage(message, new Object[] {}, Locale.getDefault()))));
            response.getWriter().write(json);
        } catch (IOException e) {
            response.setStatus(status.getCode());
            response.setContentType("application/json");
            response.getWriter().write(messageSource.getMessage(ErrorCode.BAD_REQUEST.getMessageCode(), new Object[] {}, Locale.getDefault()));
        }
    }
}
