package board.mapper;

import board.dto.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {


    void save(Member member);

    Member findByUserName(String loginId);

}
