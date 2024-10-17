package kroryi.demo.security.filter;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class RefreshTokenFilter extends OncePerRequestFilter {

    private final String refreshToken;
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        if (!path.equals(refreshToken)) {
            log.info("토큰 필터 스킵");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("리플래쉬 토큰 필터 실행");

        Map<String, String> tokens = parseRequestJSON(request);
        String accessToken = tokens.get("accessToken");
        String refreshToken = tokens.get("refreshToken");
        log.info("acessToken: {}", accessToken);
        log.info("refreshToken: {}", refreshToken);

    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request) {

        try(Reader reader = new InputStreamReader(request.getInputStream())){
                Gson gson = new Gson();
                return gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
