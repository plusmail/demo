package kroryi.demo.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        log.info("-------- 접근 거부 권한이 없습니ㅏㄷ.");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        String contentType = request.getHeader("Content-Type");
        boolean jsonRequest = contentType != null
                && contentType.startsWith("application/json");
        log.info("isJsonRequest: {}", jsonRequest);
        if(!jsonRequest) {
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }
    }
}
