package board.entity.V2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikesId implements Serializable {

    private Long member;

    private Long board;
}
