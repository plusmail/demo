package kroryi.demo.security.exception;

import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

public class AccessTokenException extends RuntimeException {

    TOKEN_ERROR token_error;

    public enum TOKEN_ERROR {
        UNACCEPT(401, "1토큰 길이가 너무 짧습니다."),
        BADTYPE(401,"2토큰 타입이 Bearer이 아닙니다."),
        BALFORM(403,"3Malformed Token"),
        BADSIGN(403,"4Signatured가 잘못된 Token"),
        EXPIRED(403,"5만기된 토큰 입니다.");
        private int status;
        private String msg;
        TOKEN_ERROR(int status, String msg) {
            this.status = status;
            this.msg = msg;
        }
        public int getStatus() {
            return this.status;
        }
        public String getMsg() {
            return this.msg;
        }
    }

    public AccessTokenException(AccessTokenException.TOKEN_ERROR error) {
        super(error.name());
        this.token_error = error;
    }

    public void sendResponseError(HttpServletResponse response){
        response.setStatus(this.token_error.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        Gson gson = new Gson();
        String responseStr = gson.toJson(
                Map.of("msg", token_error.getMsg(), "time", new Date())
        );

        try{
            response.getWriter().println(responseStr);
        }catch (IOException e){
            throw  new RuntimeException(e);
        }
    }

}

