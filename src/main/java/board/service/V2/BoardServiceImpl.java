package board.service.V2;

import board.common.util.AuthorizationUtils;
import board.dto.request.board.*;
import board.dto.response.DataResponseDto;
import board.dto.response.ResponseDto;
import board.dto.response.board.V2.PostDto;
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
        PostDto postDto = PostDto.builder().board(board).build();

        return DataResponseDto.success(postDto);
    }

    @Override
    public ResponseDto getAllBoards(GetBoardAllRequestDto dto) {

        int page = dto.getPage();
        int limit = dto.getLimit();

        int offset = (page - 1) * limit;

        List<Board> boards = boardRepository.findAllWithUsername(limit, offset);
        List<PostDto> postList = boards.stream()
                .map(b -> PostDto.builder().board(b).build())
                .collect(Collectors.toList());


        return DataResponseDto.success(postList);
    }

    @Override
    public ResponseDto getSearchBoard(GetSearchBoardListRequestDto dto) {

        String type = dto.getType();
        String keyword = dto.getKeyword();
        List<Board> boards = boardRepository.findBoardByTypeAndKeyword(type, keyword);
        List<PostDto> postList = boards.stream()
                .map(b -> PostDto.builder().board(b).build())
                .collect(Collectors.toList());

        return DataResponseDto.success(postList);
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

    /*@Override
    public ResponseDtoV2 createComment(PostCommentRequestDto dto, Long memberId, Long boardId) {

        Member member = memberRepository.findById(memberId);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));

        Comment comment = Comment.builder()
                .board(board)
                .member(member)
                .content(dto.getContent())
                .build();

        commentRepository.save(comment);
        board.increaseComment();

        return ResponseDtoV2.success();
    }

    @Override
    public ResponseDtoV2 deleteComment(Long memberId, Long boardId, Long id) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));

        Comment comment = commentRepository.findWithUsernameById(id)
                .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글 입니다."));

        AuthorizationUtils.validateMemberAuthorization(comment.getMember().getId(), memberId);

        board.decreaseComment();
        comment.delete();

        return ResponseDtoV2.success();
    }


    @Override
    public ResponseDtoV2 getCommentList(Long boardId) {

        boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));

        List<Comment> comments = commentRepository.findAllWithUsernameByBoardId(boardId);
        List<CommentDto> dtoList = comments.stream()
                .map(c -> CommentDto.builder().comment(c).build())
                .collect(Collectors.toList());

        return DataResponseDto.success(dtoList);
    }

    @Override
    public ResponseDtoV2 toggleLikes(Long memberId, Long boardId) {

        Member member = memberRepository.findById(memberId);
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));

        Optional<Likes> likesOpt = likesRepository.findByMemberIdAndBoardId(memberId, boardId);

        if (likesOpt.isEmpty()) {
            Likes likes = Likes.builder()
                    .member(member)
                    .board(board)
                    .build();

            likesRepository.save(likes);
            board.increaseLikes();

        } else {
            likesRepository.deleteByMemberIdAndBoardId(memberId, boardId);
            board.decreaseLikes();
        }

        return ResponseDtoV2.success();
    }*/
}
