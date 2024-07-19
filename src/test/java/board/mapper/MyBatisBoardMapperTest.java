package board.mapper;

import board.entity.Board;
import board.entity.Member;
import board.mapper.resultset.GetBoardResultSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MyBatisBoardMapperTest {

    @Autowired
    BoardMapper boardMapper;

    @Autowired
    UserMapper userMapper;

    Member saveMember;
    Board saveBoard;


    @BeforeAll
    void setUp() {

        Member member = Member.builder()
                .username("jaehoon1022")
                .password("1234")
                .name("hyun")
                .email("jaehoon1022@naver.com")
                .role("ROLE_USER")
                .build();

        userMapper.save(member);
        saveMember = userMapper.findByUserName("jaehoon1022");

        Board board = Board.builder()
                .memberId(1L)
                .title("테스트용 제목입니다.")
                .content("테스트용 내용입니다.")
                .build();

        boardMapper.save(board);
        saveBoard = boardMapper.findAllByMemberId(1L);
    }

    @Test
    void save() {

        Board board = Board.builder()
                .memberId(1L)
                .title("제목입니다.")
                .content("내용입니다.")
                .build();


        System.out.println("board = " + board);


        boardMapper.save(board);
        Board saveBoard = boardMapper.findAllByMemberId(1L);
        System.out.println("saveBoard = " + saveBoard);

        assertThat(saveBoard.getMemberId()).isEqualTo(1L);
        assertThat(saveBoard.getTitle()).isEqualTo("제목입니다.");
        assertThat(saveBoard.getContent()).isEqualTo("내용입니다.");

    }

    @Test
    void read() {

        GetBoardResultSet resultSet = boardMapper.getBoardById(1L);
        System.out.println("resultSet = " + resultSet);

    }

}
