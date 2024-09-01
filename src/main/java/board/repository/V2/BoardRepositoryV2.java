package board.repository.V2;

import board.entity.V2.Board;
import board.repository.BoardRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryV2 implements BoardRepository {

    private final EntityManager em;

    @Override
    public void save(Board board) {
        em.persist(board);
    }

    @Override
    public Board findById(Long id) {

//        Board board = em.find(Board.class, id);
        String jpql = "select b from Board b where b.id = :id and b.isDeleted = false";

        return em.createQuery(jpql, Board.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public Board findWithUsernameById(Long id) {

        String jpql = "select b from Board b left join fetch b.member m where b.id = :id and b.isDeleted = false";

        return em.createQuery(jpql, Board.class).setParameter("id", id).getSingleResult();
    }

    @Override
    public List<Board> findAllByMemberId(Long memberId) {

        String jpql = "select b from Board b where b.member.id = :memberId and b.isDeleted = false";

        return em.createQuery(jpql, Board.class).setParameter("memberId", memberId).getResultList();
    }

    @Override
    public List<Board> findAllWithUsernameByMemberId(Long memberId, int limit, int offset) {

        String jpql = "select b from Board b left join fetch b.member m where m.id = :memberId and b.isDeleted = false";

        return em.createQuery(jpql, Board.class)
                .setParameter("memberId", memberId)
                .setMaxResults(limit)
                .setFirstResult(offset)
                .getResultList();
    }

    @Override
    public void update(Long id, String title, String content) {
        Board board = em.find(Board.class, id);
        board.updateBoard(title, content);
    }

    @Override
    public void deleteById(Long id) {
        Board board = em.find(Board.class, id);
        board.deleteBoard();
    }
}
