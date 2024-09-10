package board.repository;

import board.entity.V2.Board;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {

    void save(Board board);

    Optional<Board> findById(Long id);

    Optional<Board> findWithUsernameById(Long id);

    List<Board> findAll();

    List<Board> findAllWithUsername(int limit, int offset);

    List<Board> findBoardByTypeAndKeyword(String type, String keyword);

//    void update(Long id, String title, String content);

//    void deleteById(Long id);

}
