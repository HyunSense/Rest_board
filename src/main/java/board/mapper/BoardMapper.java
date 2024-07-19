package board.mapper;

import board.entity.Board;
import board.mapper.resultset.GetBoardResultSet;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    void save(Board board);

    Board findAllByMemberId(Long memberId);

    Board findById(Long id);

    GetBoardResultSet getBoardById(Long id);

    List<GetBoardResultSet> getAllBoard(int limit, int offset);

    void updateBoard(Board board);

    void deleteById(Long id);

}
