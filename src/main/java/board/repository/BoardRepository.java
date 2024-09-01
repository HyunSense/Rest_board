package board.repository;

import board.entity.V2.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    void save(Board board);

    Board findById(Long id);

    Board findWithUsernameById(Long id);

    List<Board> findAllByMemberId(Long memberId);

    List<Board> findAllWithUsernameByMemberId(Long memberId,int limit, int offset);

    void update(Long id, String title, String content);

    void deleteById(Long id);

}
