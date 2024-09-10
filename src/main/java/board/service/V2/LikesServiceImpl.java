package board.service.V2;

import board.dto.response.ResponseDto;
import board.entity.V2.Board;
import board.entity.V2.Likes;
import board.entity.V2.Member;
import board.exception.BoardNotFoundException;
import board.repository.BoardRepository;
import board.repository.LikesRepository;
import board.repository.MemberRepository;
import board.service.LikesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LikesServiceImpl implements LikesService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final LikesRepository likesRepository;

    @Override
    public ResponseDto toggleLikes(Long memberId, Long boardId) {

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

        return ResponseDto.success();
    }
}
