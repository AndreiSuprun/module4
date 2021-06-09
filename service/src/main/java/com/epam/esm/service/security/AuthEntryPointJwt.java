package com.epam.esm.service.security;

import com.epam.esm.service.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Locale;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final String CODING = "utf-8";
    private static final String MESSAGE = "message";
    private static final String CODE = "code";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        HashMap<String, String> map = new HashMap<>();
        map.put(CODE, HttpStatus.UNAUTHORIZED.toString());
        map.put(MESSAGE, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(CODING);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(map);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();
        //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
