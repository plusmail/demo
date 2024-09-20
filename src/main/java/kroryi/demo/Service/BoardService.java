package kroryi.demo.Service;

import kroryi.demo.domain.Board;
import kroryi.demo.dto.*;
import lombok.extern.log4j.Log4j2;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    Long register(BoardDTO dto);

    BoardDTO readOne(Long id);

    void modify(BoardDTO dto);

    void remove(Long id);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    default Board dtoToEntity(BoardDTO boardDTO){

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())

                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName -> {
                try {
                    // UUID와 파일명을 저장할 때 URL 인코딩 처리
                    String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
                    String[] arr = encodedFileName.split("_", 3);
                    // 앞부분 UUID, 뒷부분 파일명으로 분리
                    //  /view/s_00834d8d-1e9d-4fbf-9cf4-1584b44c18f9_test_01_992.jpg
//                    System.out.println("2222222222222--->" + arr[0] +":" + arr[1] +":" + arr[2]);
                    board.addImage(arr[1], arr[2]);
//디비에 저장될때는 한글이 인코딩되어서 저장.
// 다시 꺼낼대는 디코딩 해서 사용
//                    String decoded = URLDecoder.decode(arr[2], StandardCharsets.UTF_8.toString());
//                    System.out.println("Decoded: " + decoded);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return board;
    }
    default BoardDTO entityToDTO(Board board) {
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .registerDate(board.getRegDate())
                .modifyDate(board.getModDate())
                .build();
        List<String> fileNames =
                board.getImageSet().stream().sorted().map(boardImage ->
                    boardImage.getUuid()+ "_" + boardImage.getFileName())
                    .collect(Collectors.toList());
        boardDTO.setFileNames(fileNames);

        return boardDTO;
    }

}
