package dev.caiovitor.streamingmusic.security;

import dev.caiovitor.streamingmusic.dto.ErrorResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                accessDeniedException.getMessage(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                request.getRequestURI()
        );

        String json = mapper.writeValueAsString(errorResponseDTO);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.getWriter().write(json);


    }
}
