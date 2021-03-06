package com.epam.esm.service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class SimpleAccessDeniedHandler implements AccessDeniedHandler {

    private static final String CODING = "utf-8";
    private static final String MESSAGE = "message";
    private static final String CODE = "code";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException, IOException {

        HashMap<String, String> map = new HashMap<>();
        map.put(CODE, HttpStatus.FORBIDDEN.toString());
        map.put(MESSAGE, accessDeniedException.getLocalizedMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setCharacterEncoding(CODING);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        String resBody = objectMapper.writeValueAsString(map);
        PrintWriter printWriter = response.getWriter();
        printWriter.print(resBody);
        printWriter.flush();
        printWriter.close();
    }
}
