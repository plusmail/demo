package kroryi.demo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    private String nickname;
    private String profileImage;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<>();

    // 생성 메서드에서 mid 자동 생성
    @PrePersist
    public void prePersist() {
        if (this.mid == null || this.mid.isEmpty()) {
            this.mid = UUID.randomUUID().toString();
        }
    }



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

    public Member update(String email, String nickname, String profileImage) {
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        return this;
    }

    // getRoleKey() 메서드 구현
    public String getRoleKey() {
        // roleSet이 여러 개일 경우, 특정 조건으로 주요 역할을 반환하거나, 첫 번째 역할을 반환할 수 있음
        return this.roleSet.stream()
                .findFirst()  // 첫 번째 역할 반환 (여러 개 있을 경우, 임의 선택)
                .map(Enum::name)  // Enum의 이름을 String으로 변환
                .orElse("EMP");  // 역할이 없을 경우 기본값 설정
    }
}
