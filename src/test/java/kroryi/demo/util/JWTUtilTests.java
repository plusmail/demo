package kroryi.demo.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
@Log4j2
public class JWTUtilTests {
    @Autowired
    private JWTUtil jwtUtil;

    @Test
    public void testGenerate(){
        Map<String, Object> claimMap = Map.of("mid", "ABCDE");
        String jwtStr = jwtUtil.generateToken(claimMap, 1);
        log.info(jwtStr);
    }

    @Test
    public void testValidate(){
        String jwtString ="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtaWQiOiJBQkNERSIsImlhdCI6MTcyOTA0MjY5OSwiZXhwIjoxNzI5MDQyODc5fQ.NmoMhvaYRtN76fZXG4JnMsRrsFoLbfmYBD1ePV_VLyU";
        Map<String, Object> claimMap = jwtUtil.validateToken(jwtString);

        log.info(claimMap);

    }
}
