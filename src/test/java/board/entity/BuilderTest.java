package board.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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