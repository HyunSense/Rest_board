package board.mapper;

import board.entity.Board;
import board.entity.Comment;
import board.entity.Likes;
import board.mapper.resultset.BoardResultSet;
import board.mapper.resultset.CommentResultSet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    // ----------------- Board -----------------
    void saveBoard(Board board);

    List<Board> findAllByMemberId(Long memberId);

    Board findBoardById(Long id);

    BoardResultSet getBoardById(Long id);

    List<BoardResultSet> getAllBoard(Integer limit, Integer offset);

    void updateViewCountBoard(Board board);

    void updateCommentCountBoard(Board board);

    void updateLikesCountBoard(Board board);

    void updateBoard(Board board);

    void deleteBoardById(Long id);

    // ----------------- Comment -----------------

    Comment findCommentById(Long id);
    int countCommentByBoardId(Long boardId);
    void saveCommentBoard(Comment comment);

    void deleteCommentBoardById(Long boardId, Long id);

    void deleteCommentBoardAllByBoardId(Long boardId);
    List<CommentResultSet> findAllCommentByBoardId(Long boardId);

    // ----------------- Search Board -----------------

    List<BoardResultSet> findBoardByTypeAndKeyword(String type, String keyword);

    // ----------------- likes -----------------

    void saveLikesBoard(Likes likes);
    Likes findLikesByMemberIdAndBoardId(Long memberId, Long boardId);
    void deleteLikesBoard(Likes likes);

}
