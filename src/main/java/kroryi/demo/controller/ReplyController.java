package kroryi.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kroryi.demo.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/replies")
@Log4j2
public class ReplyController {

    @PostMapping(value="/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                                     BindingResult bindingResult) throws BindException {

        log.info("replyDTO----->{}", replyDTO);
// replyer을 빼고 전송하면 bindingResult에 아래 메세지를 클라이언트로 전송 한다.
//        {
//            "replyer": "널이어서는 안됩니다"
//        }
        if(bindingResult.hasErrors()) {
//            bindingResult.rejectValue("bno","값을 넣어야 합니다.");
            throw new BindException(bindingResult);
        }

        Map<String,Long> resultMap = new HashMap<>();
        resultMap.put("success_code", 500L);
        resultMap.put("rno", 111L);

        return resultMap;

    }

}
