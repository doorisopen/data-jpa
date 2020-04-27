package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.validation.constraints.AssertTrue;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    @Autowired
    EntityManager em;

    @Test
    public void testMember() throws Exception {
        //given
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        //when
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        // java 8에서 기본으로 Optional 을 제공한다. 왜 Optional 이냐면 값이 있을수도 없을수도 있기때문이다.

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void basicCRUD() throws Exception {
        //given
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        //JPA 더티체킹
//        findMember1.setUsername("member!!!!!");

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndAgeGreaterThen() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        //then
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void testNamedQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);

        //then
        Assertions.assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findUser("AAA", 10);
        Member findMember = result.get(0);

        //then
        Assertions.assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<String> result = memberRepository.findUsernameList();
        String findMemberA = result.get(0);
        String findMemberB = result.get(1);

        //then
        Assertions.assertThat(findMemberA).isEqualTo("AAA");
        Assertions.assertThat(findMemberB).isEqualTo("BBB");
    }

    @Test
    public void findMemberDto() throws Exception {
        //given
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member = new Member("AAA", 10);
        member.setTeam(team);
        memberRepository.save(member);

        //when
        List<MemberDto> result = memberRepository.findMemberDto();

        //then
        Assertions.assertThat(result.get(0).getTeamName()).isEqualTo("teamA");

    }

    @Test
    public void findNames() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));

        //then
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(1).getUsername()).isEqualTo("BBB");
    }

    @Test
    public void returnType() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        List<Member> resultList = memberRepository.findListByUsername("AAA"); //리스트 리턴
        Member resultMember = memberRepository.findMemberByUsername("AAA"); //단건 리턴
        Optional<Member> resultOptional = memberRepository.findOptionalListByUsername("AAA"); //Optional 리턴
    }

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Page<Member> page = memberRepository.findByAge(age, pageRequest); // total count 도 같이 조회한다

        //DTO 변환하여 반환가능하게 만들기
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();

        Assertions.assertThat(content.size()).isEqualTo(3);//현재 조회된 데이터 개수
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);//전체 데이터 개수
        Assertions.assertThat(page.getNumber()).isEqualTo(0);//현재 페이지 번호
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);//전체 페이지 개수
        Assertions.assertThat(page.isFirst()).isTrue();//현재 첫번째 페이지인가?
        Assertions.assertThat(page.hasNext()).isTrue();//다음 페이지가 있는가?
    }

    @Test
    public void paging_slice() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        //when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest); // total count 도 같이 조회한다

        //then
        List<Member> content = page.getContent();

        Assertions.assertThat(content.size()).isEqualTo(3);//현재 조회된 데이터 개수
//        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);//전체 데이터 개수
        Assertions.assertThat(page.getNumber()).isEqualTo(0);//현재 페이지 번호
//        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);//전체 페이지 개수
        Assertions.assertThat(page.isFirst()).isTrue();//현재 첫번째 페이지인가?
        Assertions.assertThat(page.hasNext()).isTrue();//다음 페이지가 있는가?
    }

    @Test
    public void bulkUpdate() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        //when
        //나이가 20살 이상이면 나이+1
        int resultCount = memberRepository.bulkAgePlus(20);
        em.flush();
//        em.clear();

        //벌크 연산 이후에는 영속성 컨텍스트를 clear 를 해줘야한다.
        List<Member> findMember = memberRepository.findByUsername("member5");
        System.out.println(findMember);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        //when N + 1
        //select Member 1 + Team N
        List<Member> members = memberRepository.findAll();

        //then
        for (Member member : members) {
            System.out.println("member="+member.getUsername());
            System.out.println("member.teamClass="+member.getTeam().getClass()); // 가짜 클래스 (proxy)
            System.out.println("member.team="+member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() throws Exception  {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        Member findMember = memberRepository.findReadOnlyByUsername("member1");// 실무에서 get() 금지
        findMember.setUsername("member2");

        em.flush();
        //then

    }

    @Test
    public void lock() throws Exception {
        //given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        //when
        List<Member> member = memberRepository.findLockByUsername("member1");

        //then
    }

    @Test
    public void callCustom() throws Exception {
        List<Member> result = memberRepository.findMemberCustom();
    }
}