package board.service.V2;

import board.common.util.AuthorizationUtils;
import board.dto.request.board.PostCommentRequestDto;
import board.dto.response.DataResponseDto;
import board.dto.response.ResponseDto;
import board.dto.response.board.V2.CommentDto;
import board.dto.response.board.V2.CommentListDto;
import board.entity.V2.Board;
import board.entity.V2.Comment;
import board.entity.V2.Member;
import board.exception.BoardNotFoundException;
import board.exception.CommentNotFoundException;
import board.repository.BoardRepository;
import board.repository.CommentRepository;
import board.repository.MemberRepository;
import board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentServiceImpl implements CommentService{

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;


    @Override
    public ResponseDto createComment(PostCommentRequestDto dto, Long memberId, Long boardId) {

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

        return ResponseDto.success();
    }

    @Override
    public ResponseDto deleteComment(Long memberId, Long boardId, Long id) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));

        Comment comment = commentRepository.findWithUsernameById(id)
                .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글 입니다."));

        AuthorizationUtils.validateMemberAuthorization(comment.getMember().getId(), memberId);

        board.decreaseComment();
        comment.delete();

        return ResponseDto.success();
    }

    @Override
    public ResponseDto getCommentList(Long boardId) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("존재하지 않는 게시글 입니다."));

        List<Comment> comments = commentRepository.findAllWithUsernameByBoardId(boardId);
//        List<CommentDto> dtoList = comments.stream()
//                .map(c -> CommentDto.builder().comment(c).build())
//                .collect(Collectors.toList());
//
        List<CommentDto> commentList = comments.stream()
                .map(c -> CommentDto.fromEntity(c))
                .collect(Collectors.toList());

//        return DataResponseDto.success(dtoList);
        return DataResponseDto.success(CommentListDto.fromEntity(commentList));
    }


}
