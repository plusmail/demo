package kroryi.demo.Service.Impl;

import jakarta.transaction.Transactional;
import kroryi.demo.Service.BoardService;
import kroryi.demo.domain.Board;
import kroryi.demo.dto.*;
import kroryi.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {
    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Value("${paging.range}")
    private int defaultPageRange;

    @Override
    public Long register(BoardDTO dto) {

//        Board board = modelMapper.map(dto, Board.class);
        log.info("regist2222->{}", dto);
        Board board = dtoToEntity(dto);
        log.info("regist33333->{}", board);
        return boardRepository.save(board).getBno();
    }

    @Override
    public BoardDTO readOne(Long id) {
//        Optional<Board> result = boardRepository.findById(id);
        Optional<Board> result = boardRepository.findByIdWithImages(id);
        Board board = result.orElseThrow();

//        // 아래는 BoardDTO Board 클래스의 필드가 철자가 불일치 할경우 사용
//        PropertyMap<Board, BoardDTO> boardMap = new PropertyMap<Board, BoardDTO>() {
//            @Override
//            protected void configure() {
//                map(source.getRegDate()).setRegisterDate(null);
//                map(source.getModDate()).setModifyDate(null);
//            }
//        };
//
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.addMappings(boardMap);
//
//        BoardDTO dto = modelMapper.map(board, BoardDTO.class);

        BoardDTO boardDTO = entityToDTO(board);
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO dto) {

        log.info("modify- dto {}", dto);
        Optional<Board> result = boardRepository.findById(dto.getBno());
        Board board = result.orElseThrow();
        board.change(dto.getTitle(), dto.getContent());
        board.clearImages();

        if(dto.getFileNames() != null){
            dto.getFileNames().forEach(fileName -> {
                try {
                    // UUID와 파일명을 저장할 때 URL 인코딩 처리
                    String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());

                    // 정규식으로 UUID 다음의 파일명을 추출
                    Pattern pattern = Pattern.compile("([0-9a-fA-F\\-]+)_(.+)"); // UUID와 파일명을 분리하는 정규식
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
        boardRepository.saveAndFlush(board);
        //영속성 영역의 콘텐트내용을 DB와 동기화 하는 명령
        // save는 즉각 동기화는 하지 않음.
    }

    @Override
    public void remove(Long id) {

        //아래는 댓글도 지우는 것
//        boardRepository.deleteReplyByBoardId(id);
        //댓글은 않지우는 것
        boardRepository.deleteById(id);
    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);

        log.info("list---------> {}", result);

        // 아래는 BoardDTO Board 클래스의 필드가 철자가 불일치 할경우 사용
        PropertyMap<Board, BoardDTO> boardMap = new PropertyMap<Board, BoardDTO>() {
            @Override
            protected void configure() {
                map(source.getRegDate()).setRegisterDate(null);
            }
        };

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(boardMap);

        // resulet에는 total , dtolist,
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());


        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .pageRange(defaultPageRange)
                .dtoList(dtoList)
                .total((int) result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<BoardListReplyCountDTO> result =
                boardRepository.searchWithReplyCount(types, keyword, pageable);

        // 아래는 BoardDTO Board 클래스의 필드가 철자가 불일치 할경우 사용
        PropertyMap<Board, BoardListReplyCountDTO> boardMap = new PropertyMap<Board, BoardListReplyCountDTO>() {
            @Override
            protected void configure() {
                map(source.getRegDate()).setRegisterDate(null);
            }
        };

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(boardMap);

        // resulet에는 total , dtolist,
        List<BoardListReplyCountDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board, BoardListReplyCountDTO.class))
                .collect(Collectors.toList());

        return PageResponseDTO.<BoardListReplyCountDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .pageRange(defaultPageRange)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types,keyword,pageable);

        log.info("listWithAll-> {}", result);
        log.info("listWithAll Page-> {}", pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .pageRange(defaultPageRange)
                .total((int)result.getTotalElements())
                .build();

    }

}
