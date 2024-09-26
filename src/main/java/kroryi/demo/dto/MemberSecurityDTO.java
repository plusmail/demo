package kroryi.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
@ToString
public class MemberSecurityDTO extends User {
    private String email;
    private String social;
    private String nickname;
    private String profileImage;
    private boolean retirement;
    public MemberSecurityDTO(String username,
                             String password,
                             String email,
                             String nickname,
                             String profileImage,
                             boolean retirement,
                             String social,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.retirement = retirement;
        this.social = social;
    }
}
