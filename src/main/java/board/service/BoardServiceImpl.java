package board.service;

import board.dto.request.board.*;
import board.dto.response.*;
import board.dto.response.board.*;
import board.entity.Board;
import board.entity.Comment;
import board.entity.Likes;
import board.mapper.BoardMapper;
import board.mapper.resultset.BoardResultSet;
import board.mapper.resultset.CommentResultSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class BoardServiceImpl implements BoardService {

    private final static String TYPE_AUTHOR = "author";
    private final static String TYPE_TITLE = "title";
    private final static String TYPE_CONTENT = "content";

    private final BoardMapper boardMapper;

    @Override
    public ResponseEntity<? super PostBoardResponseDto> createBoard(Long memberId, PostBoardRequestDto dto) {

        try
        {
            String title = dto.getTitle();
            String content = dto.getContent();

            Board board = Board.builder()
                    .memberId(memberId)
                    .title(title)
                    .content(content)
                    .build();

            boardMapper.saveBoard(board);

        } catch (Exception e) {
            log.warn("createBoard exception = ", e);
            return ResponseDto.databaseError();
        }

        return PostBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetBoardAllResponseDto> getAllBoards(GetBoardAllRequestDto dto) {

        List<BoardResultSet> boards = null;
        int page = dto.getPage();
        int limit = dto.getLimit();

        try {

            int offset = (page - 1) * limit;
            boards = boardMapper.getAllBoard(limit, offset);

            if (boards == null) {
                return GetBoardAllResponseDto.notExistBoard();
            }

        } catch (Exception e) {
            log.warn("getAllBoards exception = ", e);
            return ResponseDto.databaseError();
        }

        return GetBoardAllResponseDto.success(page, limit, boards);
    }

    @Override
    public ResponseEntity<? super GetBoardResponseDto> getBoardById(Long id) {

        BoardResultSet resultSet = null;

        try {

            resultSet = boardMapper.getBoardById(id);

            if (resultSet == null) {
                return GetBoardResponseDto.notExistBoard();
            }

            Board board = boardMapper.findBoardById(id);
            board.increaseViewCount();
            boardMapper.updateViewCountBoard(board);

        } catch (Exception e) {
            log.warn("getBoardById exception = ", e);
            return ResponseDto.databaseError();
        }

        return GetBoardResponseDto.success(resultSet);
    }

    @Override
    public ResponseEntity<? super PatchBoardResponseDto> updateBoard(PatchBoardRequestDto dto, Long memberId, Long id) {

        try {

            Board board = boardMapper.findBoardById(id);

            if (board == null) {
                return PatchBoardResponseDto.notExistBoard();
            }

            Long boardMemberId = board.getMemberId();
            boolean isMemberId = boardMemberId.equals(memberId);

            if (!isMemberId) {
                return PatchBoardResponseDto.notPermission();
            }

            if (!StringUtils.hasText(dto.getTitle()) && !StringUtils.hasText(dto.getContent())) {
                return ResponseDto.validationFailed();
            }

            if (!StringUtils.hasText(dto.getTitle())) {
                dto.setTitle(board.getTitle());
            }

            if (!StringUtils.hasText(dto.getContent())) {
                dto.setContent(board.getContent());
            }

            board.patchBoard(dto);
            boardMapper.updateBoard(board);

        } catch (Exception e) {
            log.warn("updateBoardById exception = ", e);
            return ResponseDto.databaseError();
        }

        return PatchBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteBoardResponseDto> deleteBoard(Long memberId, Long id) {

        try {

            Board board = boardMapper.findBoardById(id);

            if (board == null) {
                return DeleteBoardResponseDto.notExistBoard();
            }

            Long boardMemberId = board.getMemberId();
            boolean isMemberId = boardMemberId.equals(memberId);

            if (!isMemberId) {
                return DeleteBoardResponseDto.notPermission();
            }

            int count = boardMapper.countCommentByBoardId(id);
            if (count > 0) {
                boardMapper.deleteCommentBoardAllByBoardId(id);
            }

            boardMapper.deleteBoardById(id);

        } catch (Exception e) {
            log.warn("deleteBoardById exception = ", e);
            return ResponseDto.databaseError();
        }

        return DeleteBoardResponseDto.success();
    }

    @Override
    public ResponseEntity<? super PostCommentResponseDto> createComment(PostCommentRequestDto dto, Long memberId, Long boardId) {

        try {

            Board board = boardMapper.findBoardById(boardId);

            if (board == null) {
                return PostCommentResponseDto.notExistBoard();
            }

            board.increaseComment();
            boardMapper.updateCommentCountBoard(board);

            String content = dto.getContent();

            Comment comment = Comment.builder()
                    .memberId(memberId)
                    .boardId(boardId)
                    .content(content)
                    .build();

            boardMapper.saveCommentBoard(comment);

        } catch (Exception e) {
            log.warn("createComment exception = ", e);
            return ResponseDto.databaseError();
        }

        return PostCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteCommentResponseDto> deleteComment(Long memberId, Long boardId, Long id) {

        try {

            Board board = boardMapper.findBoardById(boardId);
            if (board == null) {
                return DeleteCommentResponseDto.notExistBoard();
            }

            Comment comment = boardMapper.findCommentById(id);
            if (comment == null) {
                return DeleteCommentResponseDto.notExistComment();
            }

            Long commentMemberId = comment.getMemberId();
            boolean isMemberId = commentMemberId.equals(memberId);
            if (!isMemberId) {
                return DeleteCommentResponseDto.notPermission();
            }

            boardMapper.deleteCommentBoardById(boardId, id);

            board.decreaseComment();
            boardMapper.updateCommentCountBoard(board);

        } catch (Exception e) {
            log.warn("deleteComment exception = ", e);
            return ResponseDto.databaseError();
        }

        return DeleteCommentResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetSearchBoardListResponseDto> getSearchBoard(GetSearchBoardListRequestDto dto) {

        String type = dto.getType();
        String keyword = dto.getKeyword();

        if (type.equals("author")) {
            type = "username";
        }

        log.info("type = {}", type);

        List<BoardResultSet> searchedList = null;
        int count = 0;

        try {

            searchedList = boardMapper.findBoardByTypeAndKeyword(type, keyword);
            count = searchedList.size();

        } catch (Exception e) {
            log.warn("getSearchBoard exception e = ", e);
            return ResponseDto.databaseError();
        }

        return GetSearchBoardListResponseDto.success(type, keyword, count, searchedList);
    }

    @Override
    public ResponseEntity<? super GetCommentListResponseDto> getCommentList(Long boardId) {

        List<CommentResultSet> commentList = null;
        int count = 0;
        try {
            Board board = boardMapper.findBoardById(boardId);
            if (board == null) {
                return GetCommentListResponseDto.notExistBoard();
            }

            commentList = boardMapper.findAllCommentByBoardId(boardId);
            count = commentList.size();

        } catch (Exception e) {
            log.warn("getCommentList exception = ", e);
            return ResponseDto.databaseError();
        }

        return GetCommentListResponseDto.success(commentList, count);
    }

    @Override
    public ResponseEntity<? super GetLikesResponseDto> toggleLikes(Long memberId, Long boardId) {

        try {

            Board board = boardMapper.findBoardById(boardId);
            if (board == null) {
                return GetLikesResponseDto.notExistBoard();
            }

            Likes findLikes = boardMapper.findLikesByMemberIdAndBoardId(memberId, boardId);
            log.info("findLikes = {}", findLikes);

            if (findLikes == null) {

                Likes likes = Likes.builder()
                        .memberId(memberId)
                        .boardId(boardId)
                        .build();
                boardMapper.saveLikesBoard(likes);
                board.increaseLikes();
                log.info("after increase = {}", board);
            } else {
                log.info("else findLikes = {}", findLikes);
                boardMapper.deleteLikesBoard(findLikes);
                board.decreaseLikes();
                log.info("after decrease = {}", board);

            }

            boardMapper.updateLikesCountBoard(board);

        } catch (Exception e) {
            log.warn("toggleLikes exception = ", e);
            return ResponseDto.databaseError();
        }

        return GetLikesResponseDto.success();
    }
}
