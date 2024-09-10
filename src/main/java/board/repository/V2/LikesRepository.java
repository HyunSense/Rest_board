package board.repository.V2;

import board.entity.V2.Likes;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LikesRepository implements board.repository.LikesRepository {

    private final EntityManager em;

    @Override
    public void save(Likes likes) {
        em.persist(likes);
    }

    @Override
    public Optional<Likes> findByMemberIdAndBoardId(Long memberId, Long boardId) {

        String jpql = "select l from Likes l" +
                " left join fetch l.member m" +
                " left join fetch l.board b" +
                " where l.member.id = :memberId and l.board.id = :boardId";
        List<Likes> likesList = em.createQuery(jpql, Likes.class).setParameter("memberId", memberId).setParameter("boardId", boardId).getResultList();
        return likesList.stream().findAny();
    }

    @Override
    public void deleteByMemberIdAndBoardId(Long memberId, Long boardId) {

        String jpql = "delete from Likes l where l.member.id = :memberId and l.board.id = :boardId";
        em.createQuery(jpql).setParameter("memberId", memberId).setParameter("boardId", boardId).executeUpdate();
    }
}
