package board.repository.V1.mapper;

import board.entity.V1.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {


    void save(Member member);

    Member findByUsername(String username);

    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);

}
