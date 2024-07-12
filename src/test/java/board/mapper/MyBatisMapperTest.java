package board.mapper;

import board.dto.Member;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@MybatisTest
//@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MyBatisMapperTest {


    @Autowired
    UserMapper userMapper;

    @Test
    void save() {

        Member member = Member.builder()
                .loginId("jaehoon1022")
                .password("1234")
                .name("hyun")
                .email("jaehoon1022@naver.com")
                .role("ROLE_USER")
                .build();

        System.out.println("member = " + member);

        userMapper.save(member);
//
        Member foundMember = userMapper.findByUserName("jaehoon1022");
//
        System.out.println("foundUser = " + foundMember);

    }
}
