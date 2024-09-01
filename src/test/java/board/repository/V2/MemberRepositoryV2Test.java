package board.repository.V2;

import board.entity.V2.Member;
import board.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberRepositoryV2Test {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("저장 및 id로 단일조회")
    void saveAndFindById() {
        //given
        Member member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();


        //when
        memberRepository.save(member);
        Optional<Member> findMember = memberRepository.findById(member.getId());

        //then
        assertThat(findMember.isPresent()).isTrue();
        assertThat(findMember.get()).isEqualTo(member);
        assertThat(findMember.get().getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember.get().getPassword()).isEqualTo(member.getPassword());
    }

    @Test
    @DisplayName("저장되지 않은 회원 단일조회")
    void findByNotExistsId() {
        //given
        Member member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();
        memberRepository.save(member);


        //when
        Long notExistId = 99999L;
        Optional<Member> findMember = memberRepository.findById(notExistId);
        //then

        // em.find의 경우 찾은 entity가 없을 경우 null 반환
        // em.createQuery의 경우(jpal 직접작성) 예외반환
        assertThat(findMember).isEmpty();
    }


    @Test
    @DisplayName("회원 username으로 단일조회")
    void findByUsername() {
        //given
        Member member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();
        memberRepository.save(member);

        //when
        Member findMember = memberRepository.findByUsername(member.getUsername());

        //then
        assertThat(findMember).isEqualTo(member);
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    @DisplayName("저장되지 않은 회원 username으로 단일조회")
    void findByNotExistsUsername() {
        //given
        Member member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();
        memberRepository.save(member);

        //when
        String notExistsUsername = "notExistsUsername";


        //then
        // em.createQuery의 경우(jpal 직접작성) 예외반환
        assertThatThrownBy(() -> memberRepository.findByUsername(notExistsUsername))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    @DisplayName("회원 전체 조회")
    void findAll() {
        //given
        Member member1 = Member.builder()
                .username("firstUsername")
                .password("firstPassword")
                .name("firstName")
                .email("firstEmail")
                .role("ROLE_USER")
                .build();

        Member member2 = Member.builder()
                .username("secondUsername")
                .password("secondPassword")
                .name("secondName")
                .email("secondEmail")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);

        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members).hasSize(2);
        assertThat(members.get(0).getUsername()).isEqualTo(member1.getUsername());
        assertThat(members.get(1).getUsername()).isEqualTo(member2.getUsername());
    }

    @Test
    @DisplayName("저장된 회원이 없을때 전체조회")
    void findAllEmptyMembers() {
        //given

        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members).isEmpty();

    }

    @Test
    @DisplayName("username 중복 될때")
    void existsByUsername() {
        //given
        Member member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        //when
        boolean isExist = memberRepository.existsByUsername(member.getUsername());

        //then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("username 중복이 안될때")
    void notExistsByUsername() {
        //given
        Member member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        //when
        String notExistsUsername = "notExistsUsername";
        boolean isExist = memberRepository.existsByUsername(notExistsUsername);

        //then
        assertThat(isExist).isFalse();
    }

    @Test
    @DisplayName("email 중복 될때")
    void existsByEmail() {
        //given
        Member member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        //when
        boolean isExist = memberRepository.existsByEmail(member.getEmail());

        //then
        assertThat(isExist).isTrue();
    }

    @Test
    @DisplayName("email 중복이 안될때")
    void notExistsByEmail() {
        //given
        Member member = Member.builder()
                .username("testUsername")
                .password("testPassword")
                .name("testName")
                .email("testEmail")
                .role("ROLE_USER")
                .build();

        memberRepository.save(member);

        //when
        String notExistsEmail = "notExistsEmail";
        boolean isExist = memberRepository.existsByEmail(notExistsEmail);

        //then
        assertThat(isExist).isFalse();
    }
}