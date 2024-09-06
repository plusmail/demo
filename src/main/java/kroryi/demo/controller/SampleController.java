package kroryi.demo.controller;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Log4j2
public class SampleController {

    class SampleDTO{
        @Getter
        private String p1,p2,p3;
    }
    @GetMapping("/ex/ex2")
    public void ex2(Model model) {
//        log.info("ex/ex2--------------");
        List<String> strList = IntStream.range(1,10)
                .mapToObj(i->"Data" + i)
                .collect(Collectors.toList());

        model.addAttribute("list", strList);

        Map<String,String> map = new HashMap<>();
        map.put("p1", "p11111111111");
        map.put("p2", "p22222222222");
        map.put("p3", "p33333333333");

        model.addAttribute("map", map);

        SampleDTO sampleDTO = new SampleDTO();
        sampleDTO.p1 = "value-p11111111111";
        sampleDTO.p2 = "value-p22222222222";
        sampleDTO.p3 = "value-p33333333333";

        model.addAttribute("sampleDTO", sampleDTO);


    }




    @GetMapping("/hello")
    public void hello(Model model) {
//        log.info("hello--------------");

        model.addAttribute("message", "Hello World!");
    }

    @GetMapping("/ex/ex1")
    public void ex1(Model model) {
        List<String> list = Arrays.asList("가가가","나나나", "다다닫");
        model.addAttribute("list", list);
    }



    @GetMapping("/ex/ex3")
    public void ex3(Model model) {
        model.addAttribute("arr", new String[]{
                "회사소개",
                "장비구니",
                "회원가입"});
    }


}
