package kroryi.demo.Service;

import kroryi.demo.domain.Member;
import kroryi.demo.domain.MemberRole;
import kroryi.demo.dto.MemberJoinDTO;
import kroryi.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
        String mid = memberJoinDTO.getMid();
        boolean exist = memberRepository.existsById(mid);
        if(exist) throw new MidExistException();

        // memberJoinDTO -> member
        // member.password = 1111 암호화 안된 상태로 변환
        Member member = modelMapper.map(memberJoinDTO, Member.class);
        // member.password = 암호화 된 코드로 변경
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getPassword()));
        member.addRole(MemberRole.EMP);
        log.info("============");
        log.info(member);
        log.info(member.getRoleSet());
        memberRepository.save(member);
    }
}
