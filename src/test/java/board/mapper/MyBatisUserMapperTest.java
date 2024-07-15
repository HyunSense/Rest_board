package board.mapper;

import board.entity.Member;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.*;

@MybatisTest
//@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MyBatisUserMapperTest {


    @Autowired
    UserMapper userMapper;

    @Test
    void save() {

        Member member = Member.builder()
                .username("jaehoon1022")
                .password("1234")
                .name("hyun")
                .email("jaehoon1022@naver.com")
                .role("ROLE_USER")
                .build();

        System.out.println("member = " + member);

        userMapper.save(member);
//
        Member saveMember = userMapper.findByUserName("jaehoon1022");

        assertThat(saveMember.getUsername()).isEqualTo("jaehoon1022");
        assertThat(saveMember.getName()).isEqualTo("hyun");

        System.out.println("saveMember = " + saveMember);

    }
}
