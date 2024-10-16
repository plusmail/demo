package kroryi.demo.security.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kroryi.demo.security.exception.AccessTokenException;
import kroryi.demo.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        log.info("url--------->{}", path);
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("토큰 체그... 필터 적용됩니다..");
        log.info("JWTUtil: {}", jwtUtil);
        try{
            Map<String, Object> payload = validateAccessToken(request);
            String mid = (String) payload.get("mid");
            log.info("mid:------- {}", mid);
            //아래는 다음 필터로 request, response를 넘겨준다.
            filterChain.doFilter(request, response);
        }catch (AccessTokenException accessTokenException){
            accessTokenException.sendResponseError(response);
        }

    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws
            AccessTokenException {
        String headerString = request.getHeader("Authorization");
        log.info("headerString----->: {}", headerString);

        if (headerString == null || headerString.length() < 8) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }
        // Bearer xexfdfdsdfsererererwe jwt코드
        String tokenType = headerString.substring(0, 6);
        String tokenStr = headerString.substring(7);

        log.info("tokenType----->: {}", tokenType);
        log.info("tokenStr----->: {}", tokenStr);

        if (tokenType.equalsIgnoreCase("Bearer") == false) {
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }
        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        } catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException-----------");
            throw new AccessTokenException((AccessTokenException.TOKEN_ERROR.BALFORM));

        } catch (SignatureException signatureException) {
            log.error("SignatureException-----------");
            throw new AccessTokenException((AccessTokenException.TOKEN_ERROR.BADSIGN));

        } catch (ExpiredJwtException expiredJwtException) {
            log.error("ExpiredJwtException-----------");
            throw new AccessTokenException((AccessTokenException.TOKEN_ERROR.EXPIRED));

        }
    }
}
