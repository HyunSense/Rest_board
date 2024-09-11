package board.service.V2;

import board.common.util.AuthorizationUtils;
import board.dto.request.board.*;
import board.dto.response.DataResponseDto;
import board.dto.response.ResponseDto;
import board.dto.response.board.V2.BoardDto;
import board.dto.response.board.V2.BoardListDto;
import board.dto.response.board.V2.BoardSearchListDto;
import board.entity.V2.Board;
import board.entity.V2.Member;
import board.exception.BoardNotFoundException;
import board.repository.BoardRepository;
import board.repository.CommentRepository;
import board.repository.LikesRepository;
import board.repository.MemberRepository;
import board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//TODO: 서비스 계층 메서드 네이밍 수정 필요
@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardServiceImpl implements BoardService {
    private final MemberRepository memberRepository;

    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;

    @Override
    public ResponseDto createBoard(Long memberId, PostBoardRequestDto dto) {

        // Security findByUsername을 통해 유효성 검증
        Member member = memberRepository.findById(memberId);

        Board board = PostBoardRequestDto.toEntity(dto);
        board.setMember(member);

        boardRepository.save(board);

        return ResponseDto.success();
    }

    /**
     * dirty checking
     * READ
     */
    @Override
    public ResponseDto getBoardById(Long id) {

        Board board = boardRepository.findWithUsernameById(id)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));
        board.increaseViewCount();

        return DataResponseDto.success(BoardDto.fromEntity(board));
    }

    @Override
    public ResponseDto getAllBoards(GetBoardAllRequestDto dto) {

        int page = dto.getPage();
        int limit = dto.getLimit();

        int offset = (page - 1) * limit;

        List<Board> boards = boardRepository.findAllWithUsername(limit, offset);

        List<BoardDto> boardList = boards.stream()
        .map(b -> BoardDto.fromEntity(b))
        .collect(Collectors.toList());


        return DataResponseDto.success(BoardListDto.fromEntity(page, limit, boardList));
    }

    //TODO: 페이징 구현 필요
    @Override
    public ResponseDto getSearchBoard(GetSearchBoardListRequestDto dto) {

        String type = dto.getType();
        String keyword = dto.getKeyword();
        List<Board> boards = boardRepository.findBoardByTypeAndKeyword(type, keyword);

//        List<BoardDto> boardList = boards.stream()
//                .map(b -> BoardDto.fromEntity(b))
//                .collect(Collectors.toList());

        List<BoardDto> boardList = boards.stream()
                .map(b -> BoardDto.fromEntity(b))
                .collect(Collectors.toList());

//        return DataResponseDto.success(BoardListDto.fromEntity(0, 0, boardList));
        return DataResponseDto.success(BoardSearchListDto.fromEntity(boardList));
    }

    @Override
    public ResponseDto updateBoard(PatchBoardRequestDto dto, Long memberId, Long id) {

        Board board = boardRepository.findWithUsernameById(id)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));


        AuthorizationUtils.validateMemberAuthorization(board.getMember().getId(), memberId);

        board.update(dto.getTitle(), dto.getContent());

        return ResponseDto.success();
    }

    @Override
    public ResponseDto deleteBoard(Long memberId, Long id) {

        Board board = boardRepository.findWithUsernameById(id)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));


        AuthorizationUtils.validateMemberAuthorization(board.getMember().getId(), memberId);

        //TODO: 체크 필요
        int count = commentRepository.countByBoardId(id);

        if (count > 0) {
            commentRepository.deleteAllByBoardId(id);
        }

        board.delete();

        return ResponseDto.success();
    }
}
