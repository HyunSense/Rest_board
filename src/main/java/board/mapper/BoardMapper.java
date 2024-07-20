package board.mapper;

import board.entity.Board;
import board.entity.Comment;
import board.mapper.resultset.GetBoardResultSet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    void saveBoard(Board board);

    Board findAllByMemberId(Long memberId);

    Board findBoardById(Long id);

    GetBoardResultSet getBoardById(Long id);

    List<GetBoardResultSet> getAllBoard(int limit, int offset);

    void updateViewCountBoard(Board board);

    void updateCommentCountBoard(Board board);

    void updateBoard(Board board);

    void deleteBoardById(Long id);

    Comment findCommentById(Long id);

    int countCommentByBoardId(Long boardId);

    void saveComment(Comment comment);

    void deleteCommentById(Long boardId, Long id);

    void deleteBoardCommentAllByBoardId(Long boardId);
}
