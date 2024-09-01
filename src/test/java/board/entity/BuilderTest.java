package board.entity;

import board.entity.V1.Board;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BuilderTest {

    @Test
    void builderTest() {

        Board board = Board.builder()
                .title("title 입니다.")
                .content("content 입니다.")
                .memberId(1L)
                .build();


        assertThat(board.getMemberId()).isEqualTo(1L);
        assertThat(board.getTitle()).isEqualTo("title 입니다.");
    }

}