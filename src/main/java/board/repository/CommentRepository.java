package board.repository;

import board.entity.V2.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    void save(Comment comment);

    Optional<Comment> findById(Long id);

    Optional<Comment> findWithUsernameById(Long id);

    List<Comment> findAllByBoardId(Long boardId);

    List<Comment> findAllWithUsernameByBoardId(Long boardId);

//    void deleteById(Long id);

    void deleteAllByBoardId(Long boardId);

    int countByBoardId(Long boardId);


}
