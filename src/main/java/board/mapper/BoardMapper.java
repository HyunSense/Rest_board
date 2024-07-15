package board.mapper;

import board.entity.Board;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {

    void save(Board board);

    Board findByMemberId(Long memberId);

}
