package board.entity;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@ToString
public class Likes {

    private Long memberId;
    private Long boardId;

}
