package kroryi.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@Log4j2
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/login")
    public String loginGET(String error, String logout){
        log.info("로그인 컨트롤러.......");
        log.info("로그아웃 : {}", logout);
        if(logout != null){
            log.info("사용자 로그아웃 됨.");
        }

        return "member/login";
    }

}
