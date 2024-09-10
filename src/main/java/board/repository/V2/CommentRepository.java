package board.repository.V2;

import board.entity.V2.Comment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepository implements board.repository.CommentRepository {

    private final EntityManager em;

    @Override
    public void save(Comment comment) {
        em.persist(comment);
    }

    // 단일 조회
    @Override
    public Optional<Comment> findById(Long id) {

        String jpql = "select c from Comment c where c.id = :id and c.isDeleted = false";
        List<Comment> comments = em.createQuery(jpql, Comment.class).setParameter("id", id).getResultList();

        return comments.stream().findAny();
    }

    // 단일 조회
    @Override
    public Optional<Comment> findWithUsernameById(Long id) {

        String jpql = "select c from Comment c left join fetch c.member m where c.id = :id and c.isDeleted = false";
        List<Comment> comments = em.createQuery(jpql, Comment.class).setParameter("id", id).getResultList();

        return comments.stream().findAny();
    }

    @Override
    public List<Comment> findAllByBoardId(Long boardId) {
        String jpql = "select c from Comment c where c.board.id = :boardId and c.isDeleted = false";

        return em.createQuery(jpql, Comment.class).setParameter("boardId", boardId).getResultList();
    }

    @Override
    public List<Comment> findAllWithUsernameByBoardId(Long boardId) {

        String jpql = "select c from Comment c left join fetch c.member m where c.board.id = :boardId and c.isDeleted = false";

        return em.createQuery(jpql, Comment.class).setParameter("boardId", boardId).getResultList();
    }


    @Override
    public void deleteAllByBoardId(Long boardId) {
        findAllByBoardId(boardId).forEach(c -> c.delete());
    }

    @Override
    public int countByBoardId(Long boardId) {
        String jpql = "select count(c) from Comment c where c.board.id = :boardId";
        Long count = em.createQuery(jpql, Long.class).setParameter("boardId", boardId).getSingleResult();

        return count.intValue();
    }
}
