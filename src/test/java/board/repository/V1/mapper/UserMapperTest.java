package board.repository.V1.mapper;

import board.entity.V1.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@MybatisTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserMapperTest {


    @Autowired
    private UserMapper userMapper;

    @Test
    @DisplayName("Member 저장 및 조회")
    void saveAndFindByUsername() {
        // given
        Member member = Member.builder()
                .username("jaehoon1022")
                .password("1234")
                .name("hyun")
                .email("jaehoon1022@naver.com")
                .role("ROLE_USER")
                .build();

        // when
        userMapper.save(member);
        Member saveMember = userMapper.findByUsername("jaehoon1022");

        // then
        assertThat(saveMember.getUsername()).isEqualTo("jaehoon1022");
        assertThat(saveMember.getName()).isEqualTo("hyun");
    }
    
    @Test
    @DisplayName("존재하지않는 Member 조회")
    void findByNonExistUsername() {
        //when
        Member nonExistMember = userMapper.findByUsername("nonExistMember");

        //then
        assertThat(nonExistMember).isNull();
    }
    
    @Test
    @DisplayName("중복된 Member 이름 저장")
    void saveDuplicateUsername() {
        //given
        Member member1 = Member.builder()
                .username("duplicateMember")
                .password("1234")
                .name("firstMember")
                .email("firstMember@naver.com")
                .role("ROLE_USER")
                .build();

        Member member2 = Member.builder()
                .username("duplicateMember")
                .password("1234")
                .name("secondMember")
                .email("secondMember@naver.com")
                .role("ROLE_USER")
                .build();

        //when
        userMapper.save(member1);

        //then
        assertThatThrownBy(() -> userMapper.save(member2))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("email 중복")
    void notExistsByEmail() {

        //given
        Member member = Member.builder()
                .username("jaehoon1022")
                .password("1234")
                .name("hyun")
                .email("jaehoon1022@naver.com")
                .role("ROLE_USER")
                .build();

        userMapper.save(member);

        //when
        Boolean isEmail = userMapper.existsByEmail(member.getEmail());

        // then
        assertThat(isEmail).isTrue();
    }

    @Test
    @DisplayName("email 중복되지 않음")
    void existsByEmail() {

        //given
        String email = "testEmail";

        //when
        Boolean isEmail = userMapper.existsByEmail(email);

        // then
        assertThat(isEmail).isFalse();
    }

    @Test
    @DisplayName("username 중복" )
    void notExistByUsername() {

        // given
        Member member = Member.builder()
                .username("jaehoon1022")
                .password("1234")
                .name("hyun")
                .email("jaehoon1022@naver.com")
                .role("ROLE_USER")
                .build();

        userMapper.save(member);

        //when
        Boolean isUsername = userMapper.existsByUsername(member.getUsername());

        //then
        assertThat(isUsername).isTrue();
    }

    @Test
    @DisplayName("username 중복 되지않음" )
    void existByUsername() {

        // given
        String username = "testUsername";

        //when
        Boolean isUsername = userMapper.existsByUsername(username);

        //then
        assertThat(isUsername).isFalse();
    }
}
