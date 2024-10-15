package kroryi.demo.controller.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@Tag(name = "User", description = "User 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@CrossOrigin
// CORS는 클라이언트가 자신이 속한 도메인 이외의
// 다른 도메인에 있는 리소스에 접근할 때 발생하는 문제를 해결하기 위한 메커니즘
// @CrossOrigin(origins = "http://example.com") 접근을 허가 하려는 도메인 설정
@RequestMapping("/api/sample")
public class ApiController {
    @ApiOperation("셈플 GET doA")
    @GetMapping("/doA")
    public List<String> doA(){
        return Arrays.asList("aaaaa","bbbbb","cccccc");
    }
}
