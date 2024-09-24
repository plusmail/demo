package kroryi.demo.repository;

import kroryi.demo.domain.Member;
import kroryi.demo.domain.MemberRole;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .mid("회원"+i)
                    .password(passwordEncoder.encode("1111"))
                    .email("email"+i+"@gmail.com")
//                    .social("내부")
                    .build();
            if(i >= 90){
                member.addRole(MemberRole.ADMIN);
            }else{
                member.addRole(MemberRole.EMP);
            }
            repository.save(member);
        });
    }

    @Test
    public void testTest(){
        Optional<Member> result = repository.getWithRoles("회원90");
        Member member = result.orElseThrow();
        log.info(member.toString());
        log.info(member.getRoleSet());
        member.getRoleSet().forEach(role->log.info(role.name()));

    }


}
