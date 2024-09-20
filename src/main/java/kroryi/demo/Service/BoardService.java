package kroryi.demo.Service;

import kroryi.demo.domain.Board;
import kroryi.demo.dto.*;
import lombok.extern.log4j.Log4j2;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

                    // 정규식으로 UUID 다음의 파일명을 추출
                    Pattern pattern = Pattern.compile("s_([0-9a-fA-F\\-]+)_(.+)"); // UUID와 파일명을 분리하는 정규식
                    Matcher matcher = pattern.matcher(encodedFileName);

                    if (matcher.find()) {
                        String uuid = matcher.group(1);    // UUID 부분
                        String originalFileName = URLDecoder.decode(matcher.group(2), StandardCharsets.UTF_8.toString());  // 파일명 부분
                        System.out.println("UUID: " + uuid);
                        System.out.println("Original File Name: " + originalFileName);

                        board.addImage(uuid, originalFileName);
                    } else {
                        System.out.println("파일명 패턴이 일치하지 않습니다: " + fileName);
                    }


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
