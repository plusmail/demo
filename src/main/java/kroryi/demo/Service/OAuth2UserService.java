package kroryi.demo.Service;

import kroryi.demo.domain.Member;
import kroryi.demo.dto.KakaoUserInfo;
import kroryi.demo.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public OAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Role generate
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");

        String provider = userRequest.getClientRegistration().getRegistrationId();

        // nameAttributeKey
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // DB 저장로직이 필요하면 추가
        // 닉네임 프로파일 이메일
        Member member = saveOrUpdate(attributes);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
                oAuth2User.getAttributes(),
                userNameAttributeName
        );
    }

    private Member saveOrUpdate(Map<String, Object> attributes) {
        System.out.println("kakao-------->" + attributes);


        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(attributes);
        Member member = memberRepository.findByEmail(kakaoUserInfo.getEmail())
                .map(entity -> entity.update(kakaoUserInfo.getEmail(),kakaoUserInfo.getNickname(), kakaoUserInfo.getProfileImage()))
                .orElse(kakaoUserInfo.toEntity());
        return memberRepository.save(member);
    }
}
