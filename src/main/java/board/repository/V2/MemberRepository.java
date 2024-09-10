package board.repository.V2;

import board.entity.V2.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepository implements board.repository.MemberRepository {

    private final EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    // security member 유효성 보장
    @Override
    public Member findById(Long id) {

        return em.find(Member.class, id);
    }

    @Override
    public Optional<Member> findByUsername(String username) {

        String jpql = "select m from Member m where m.username = :username";

        List<Member> members = em.createQuery(jpql, Member.class).setParameter("username", username).getResultList();
        return members.stream().findAny();
    }

    @Override
    public List<Member> findAll() {

        String jpql = "select m from Member m";
        return em.createQuery(jpql, Member.class).getResultList();
    }

    //TODO: count ? exists 쿼리개선 필요
    @Override
    public boolean existsByUsername(String username) {

        String jpql = "select count(m.id) from Member m where m.username = :username";
        Long count = em.createQuery(jpql, Long.class).setParameter("username", username).getSingleResult();
        return count > 0;
    }

    @Override
    public boolean existsByEmail(String email) {

        String jpql = "select count(m.email) from Member m where m.email = :email";
        Long count = em.createQuery(jpql, Long.class).setParameter("email", email).getSingleResult();

        return count > 0;
    }
}
