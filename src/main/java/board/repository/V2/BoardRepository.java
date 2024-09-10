package board.repository.V2;

import board.entity.V2.Board;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepository implements board.repository.BoardRepository {

    private final EntityManager em;

    @Override
    public void save(Board board) {
        em.persist(board);
    }

    @Override
    public Optional<Board> findById(Long id) {

        String jpql = "select b from Board b where b.id = :id and b.isDeleted = false";
        List<Board> boards = em.createQuery(jpql, Board.class).setParameter("id", id).getResultList();

        return boards.stream().findAny();

    }

    @Override
    public Optional<Board> findWithUsernameById(Long id) {

        String jpql = "select b from Board b left join fetch b.member m where b.id = :id and b.isDeleted = false";
        List<Board> boards = em.createQuery(jpql, Board.class).setParameter("id", id).getResultList();

        return boards.stream().findAny();
    }

    @Override
    public List<Board> findAll() {

        String jpql = "select b from Board b where b.isDeleted = false";

        return em.createQuery(jpql, Board.class).getResultList();
    }

    @Override
    public List<Board> findAllWithUsername(int limit, int offset) {

        String jpql = "select b from Board b left join fetch b.member m where b.isDeleted = false";

        return em.createQuery(jpql, Board.class)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    //TODO: 기존 findAll 과 통합 필요 여부?
    @Override
    public List<Board> findBoardByTypeAndKeyword(String type, String keyword) {

        String jpql = "select b from Board b left join fetch b.member m where b.isDeleted = false";
        String pattern = "%" + keyword + "%";

        if (type.equals("username")) {
            jpql += " and m.username like :pattern";
        } else if (type.equals("title")) {
            jpql += " and b.title like :pattern";
        } else if (type.equals("content")) {
            jpql += " and b.content like :pattern";
        }

        return em.createQuery(jpql, Board.class)
                .setParameter("pattern", pattern).getResultList();
    }
}
