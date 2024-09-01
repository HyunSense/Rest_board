package board.entity.V1;

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
