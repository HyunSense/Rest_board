package board.entity.V2;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
@IdClass(LikesId.class)
public class Likes {

    @Id
    @JoinColumn(name = "member_id")
    @ManyToOne
    private Member member;

    @Id
    @JoinColumn(name = "board_id")
    @ManyToOne
    private Board board;
}
