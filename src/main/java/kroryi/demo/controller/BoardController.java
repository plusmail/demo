package kroryi.demo.controller;

import jakarta.validation.Valid;
import kroryi.demo.Service.BoardService;
import kroryi.demo.dto.BoardDTO;
import kroryi.demo.dto.BoardListReplyCountDTO;
import kroryi.demo.dto.PageRequestDTO;
import kroryi.demo.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(PageRequestDTO pageRequestDTO, Model model) {
        if (pageRequestDTO.getPage() < 1) {
            pageRequestDTO.setPage(1);  // 페이지 번호가 1보다 작은 경우 1로 설정
        }
//        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        PageResponseDTO<BoardListReplyCountDTO> responseDTO =
                boardService.listWithReplyCount(pageRequestDTO);

        log.info(responseDTO);

        model.addAttribute("responseDTO", responseDTO);

        return "board/list";
    }

    @GetMapping("/register")
    public String register(Model model) {

        return "board/register";
    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        log.info("-------register -------");
        if(bindingResult.hasErrors()) {
            log.info("에러 발생");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/board/register";
        }
        log.info(boardDTO);
        Long bno = boardService.register(boardDTO);
        redirectAttributes.addFlashAttribute("result", bno);

        //http://localhost:8080/boart/list?bno=xxxx
        return "redirect:/board/list";

    }

    @GetMapping("/read")
    public String read(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("-------read -------");
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info("/board/list ---> {}", boardDTO);
        model.addAttribute("dto", boardDTO);

        return "board/read";
    }

    @GetMapping("/modify")
    public String modify(Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info("-------modify -------");
        BoardDTO boardDTO = boardService.readOne(bno);
        log.info("/board/modify ---> {}", boardDTO);
        model.addAttribute("dto", boardDTO);

        return "board/modify";
    }

    @PostMapping("/modify")
    public String modify(@Valid BoardDTO boardDTO,
                         PageRequestDTO pageRequestDTO,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        log.info("-------modify post -------");

        if(bindingResult.hasErrors()) {
            log.info("/modify post 에러 발생 : {}", bindingResult.getAllErrors());
            String link = pageRequestDTO.getLink();
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("bno", boardDTO.getBno());
            return "redirect:/board/modify?" + link;
        }

        boardService.modify(boardDTO);
        redirectAttributes.addFlashAttribute("result", "수정됨");
        redirectAttributes.addAttribute("bno", boardDTO.getBno());


        // /board/read?bno=xxxx
        return "redirect:/board/read";
    }



}
