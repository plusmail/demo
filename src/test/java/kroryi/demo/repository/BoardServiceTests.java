package kroryi.demo.repository;

import kroryi.demo.Service.BoardService;
import kroryi.demo.Service.Impl.BoardServiceImpl;
import kroryi.demo.domain.Board;
import kroryi.demo.dto.*;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService service;
    @Autowired
    private BoardServiceImpl boardServiceImpl;

    @Test
    public void testRegister(){

        log.info("register-> {}", service.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("게시판 첫 글")
                .content("내용도 첫 내용")
                .writer("김유신")
                .build();

        Long bno = service.register(boardDTO);
        log.info("board 게시물 등록-> {}", bno);

    }

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(301L)
                .title("게시판 첫 글-------------")
                .content("내용도 첫 내용------------")
                .writer("김유신")
                .build();
        service.modify(boardDTO);
    }

    @Test
    public void testRemove(){
        service.remove(300L);
    }

    @Test
    public void pageTest(){

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("내용")
                .page(1)
                .size(10)
                .build();


        PageResponseDTO<BoardDTO> responseDTO = service.list(pageRequestDTO);

        log.info(responseDTO);
    }

    @Test
    public void testRegisterWithImages(){
        log.info(service.getClass().getName());
        BoardDTO boardDTO = BoardDTO.builder()
                .title("파일등록과 제목 등록")
                .content("내용.... ")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_ccc.jpg"
                )
        );
        Long bno = service.register(boardDTO);
        log.info("등록된 게시글 번혼bno : {}", bno);
    }

    @Test
    public void testReadAll(){
        Long bno = 303L;
        BoardDTO boardDTO = service.readOne(bno);
        log.info(boardDTO);
        for(String fileName: boardDTO.getFileNames()) {
            log.info(fileName);
        }
    }

    @Test
    public void testModifyFile(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(303L)
                .title("파일 업로그 게시물 수정 303번")
                .content("내용 수정 됨.303")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));
        service.modify(boardDTO);
    }

    @Test
    public void testRemoveBno(){
        Long bno = 303L;
        service.remove(bno);
    }

    @Test
    public void testListWithAll(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();
        PageResponseDTO<BoardListAllDTO> responseDTO =
                service.listWithAll(pageRequestDTO);
        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();
        dtoList.forEach(boardListAllDTO -> {
            log.info("{}:{}", boardListAllDTO.getBno(), boardListAllDTO.getTitle());
            if(boardListAllDTO.getBoardImages() != null){
                for(BoardImageDTO boardImage : boardListAllDTO.getBoardImages()){
                    log.info(boardImage);
                }
            }
            log.info("-----------------------------");

        });

    }

}
