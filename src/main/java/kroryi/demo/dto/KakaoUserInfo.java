package kroryi.demo.dto;

import kroryi.demo.domain.Member;

import java.util.Map;

public class KakaoUserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getEmail() {
        return (String) ((Map<String, Object>) attributes.get("kakao_account")).get("email");
    }

    public String getNickname() {
        return (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");
    }

    public String getProfileImage() {
        return (String) ((Map<String, Object>) attributes.get("properties")).get("profile_image");
    }

    public Member toEntity() {
        return Member.builder()
                .email(getEmail())
                .nickname(getNickname())
                .profileImage(getProfileImage())
                .build();
    }
}
