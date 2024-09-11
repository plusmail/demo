package kroryi.demo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kroryi.demo.domain.Board;
import kroryi.demo.domain.BoardImage;
import kroryi.demo.domain.QBoard;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;


@SpringBootTest
public class BoardRepositoryTests {

    private static final Logger log = LoggerFactory.getLogger(BoardRepositoryTests.class);

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    //
    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("하하하" + i)
                    .content("내용" + i)
                    .writer("사용자" + (i % 10))
                    .build();
            Board result = boardRepository.save(board);
            log.info("BNO: {}", result.getBno());


        });
    }

//    @Test
//    public void testSelect(){
//        Long bno = 100L;
//        Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
//        log.info(board.toString());
//    }
//
//    @Test
//    public void testUpdate(){
//        Long bno = 100L;
//        Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
//        log.info(board.toString());
//        board.change("수정한거야....","내용도 수정 했지롱.");
//        //save 안하면 저장 안됨.
//        boardRepository.save(board);
//    }
//
//    @Test
//    public void testDelete(){
//        Long bno = 1L;
//        boardRepository.deleteById(bno);
//    }
//
//    @Test
//    public void testPaging(){
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
////        Page<Board> result = boardRepository.findAll(pageable);
////        log.info("전체 게시물 수 {}", result.getTotalElements());
////        log.info("전체 페이지 수 {}", result.getTotalPages());
////        log.info("페이지 번호{}", result.getNumber());
////        log.info("한페이지당 수 {}", result.getSize());
////        log.info("한페이지당 수 {}", result.getContent());
////        List<Board> boards = result.getContent();
////        boards.forEach(board -> {
////            log.info(board);
////        });
//
//        Page<Board> resultKeyword = boardRepository.findKeyword("하8",pageable);
//        log.info("전체 게시물 수 {}", resultKeyword.getTotalElements());
//        log.info("전체 페이지 수 {}", resultKeyword.getTotalPages());
//        log.info("페이지 번호{}", resultKeyword.getNumber());
//        log.info("한페이지당 수 {}", resultKeyword.getSize());
//        log.info("한페이지당 수 {}", resultKeyword.getContent());
//        List<Board> boards = resultKeyword.getContent();
//        boards.forEach(board -> {
//            log.info(board);
//        });
//    }

    @Test
    public void testSearch() {
        QBoard qBoard = QBoard.board;
        List<String> boardList = jpaQueryFactory
                .select(qBoard.title)
                .from(qBoard)
                .where(qBoard.title.like("%하하하8%"))
                .orderBy(qBoard.title.asc())
                .fetch();

        for (String board : boardList) {
            System.out.println("--------------");
            System.out.println("title->" + board);
        }

//        List<Tuple> boardList = jpaQueryFactory
//                .select(qBoard.title, qBoard.content)
//                .from(qBoard)
//                .where(qBoard.title.like("하하하7"))
//                .orderBy(qBoard.content.asc())
//                .fetch();
//        System.out.println(boardList);
//
//        for(Tuple board : boardList){
//            System.out.println(board.get(qBoard.title));
//            System.out.println(board.get(qBoard.content));
//        }
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//        boardRepository.search(pageable);

    }

//    @Autowired
//    private BoardSearch boardSearch;

    @Test
    public void testBoardSearch() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        boardRepository.search(pageable);

    }

    @Test
    public void testBoardSearchAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        QBoard qBoard = QBoard.board;

        String[] types = {"t", "c", "w"};
        Page<Board> result = boardRepository.searchAll(types, "하하하8", pageable);
        System.out.println(result.getTotalPages());
        System.out.println(result.getTotalElements());
        System.out.println(result.getContent());
        log.info(String.valueOf(result.getTotalPages()));

        result.getContent().forEach(board -> {
            System.out.println(board.getTitle());
        });
    }


    @Test
    public void testInsertWithImages() {
        Board board = Board.builder()
                .title("이미지 들어간 게시물 1")
                .content("첨부파일 확인")
                .writer("tester")
                .build();

        // 실제 사진은 없고 Board와 BoardImage 테이블에
        // 글도 등록되고 BoardImage에도 파일에 대한 정보가 입력되는지 확인 용
        for (int i = 0; i < 3; i++) {
            board.addImage(UUID.randomUUID().toString(), "file" + i + "jpg");
        }

        boardRepository.save(board);

    }

    @Test
    public void testReadWithImages() {
        Optional<Board> result = boardRepository.findByIdWithImages(409L);
        Board board = result.orElseThrow();

        log.info("board->{}", board);
        log.info("-----------------");
//            log.info("board image->{}", board.getImageSet());
        for (BoardImage boardImage : board.getImageSet()) {
            log.info("boardimage->{}", boardImage);
        }


    }

}
