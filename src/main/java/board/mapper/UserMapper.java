package board.mapper;

import board.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {


    void save(Member member);

    Member findByUserName(String username);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

}
