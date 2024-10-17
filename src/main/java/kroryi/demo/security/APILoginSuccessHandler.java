package kroryi.demo.security;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("JWT 로그인 성공... 했습니다.");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        log.info(authentication);
        log.info(authentication.getName());

        Map<String,Object> claim = Map.of("mid", authentication.getName());
        String accessToken = jwtUtil.generateToken(claim, 1);
        String refreshToken = jwtUtil.generateToken(claim, 2);

        Gson gson = new Gson();
        Map<String, String> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken);
        String jsonStr = gson.toJson(keyMap);
        response.getWriter().println(jsonStr);
    }
}
