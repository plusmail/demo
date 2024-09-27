package kroryi.demo.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AuthUtils {
    public String getPrincipalUsername(Authentication authentication){
        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails)principal).getUsername();
        }else if(principal instanceof org.springframework.security.oauth2.core.user.OAuth2User){
            Map<String, Object> attributes = ((OAuth2User) principal).getAttributes();
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            if (kakaoAccount != null) {
                return (String) kakaoAccount.get("email");
            }
        }
        return null;
    }
}
