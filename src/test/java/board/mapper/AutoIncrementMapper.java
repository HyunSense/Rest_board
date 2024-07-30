package board.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AutoIncrementMapper {

    @Update("alter table member auto_increment = 1")
    void resetMemberAutoIncrement();

    @Update("alter table board auto_increment = 1")
    void resetBoardAutoIncrement();

    @Update("alter table comment auto_increment = 1")
    void resetCommentAutoIncrement();

}
