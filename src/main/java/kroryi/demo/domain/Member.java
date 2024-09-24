package kroryi.demo.domain;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "roleSet")
public class Member extends BaseEntity{

    @Id
    private String mid;
    private String name;
    private String email;
    private String password;
    private boolean retirement;
    private String social;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }
    public void changeSocial(String newSocial) {
        this.social = newSocial;
    }

    public void changeRetire(boolean retirement) {
        this.retirement=retirement;
    }

    public void addRole(MemberRole role) {
        this.roleSet.add(role);
    }
    public void clearRoles(){
        this.roleSet.clear();
    }
}
