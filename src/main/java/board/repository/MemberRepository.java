package board.repository;

import board.entity.V2.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {

    void save(Member member);
    Member findById(Long id);
    Optional<Member> findByUsername(String username);
    List<Member> findAll();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
