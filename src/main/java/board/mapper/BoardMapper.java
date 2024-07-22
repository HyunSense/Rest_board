package board.mapper;

import board.entity.Board;
import board.entity.Comment;
import board.mapper.resultset.BoardResultSet;
import board.mapper.resultset.CommentListResultSet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    void saveBoard(Board board);

    Board findAllByMemberId(Long memberId);

    Board findBoardById(Long id);

    BoardResultSet getBoardById(Long id);

    List<BoardResultSet> getAllBoard(int limit, int offset);

    void updateViewCountBoard(Board board);

    void updateCommentCountBoard(Board board);

    void updateBoard(Board board);

    void deleteBoardById(Long id);

    Comment findCommentById(Long id);

    int countCommentByBoardId(Long boardId);

    void saveComment(Comment comment);

    void deleteCommentById(Long boardId, Long id);

    void deleteBoardCommentAllByBoardId(Long boardId);

    List<BoardResultSet> findBoardByTypeAndKeyword(String type, String keyword);

    List<CommentListResultSet> findAllCommentByBoardId(Long boardId);
}
