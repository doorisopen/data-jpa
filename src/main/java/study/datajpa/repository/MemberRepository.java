package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

//    @Query(name = "Member.findByUsername") // 관례가 있어서 있어도 없어도 동작한다.
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //DTO 로 조회하기
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //컬렉션 파라미터로 넣기
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    //반환 타입 (컬렉션, 단건, 단건 Optional)
    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalListByUsername(String username);

    //paging
    @Query(value = "select m from Member m left join m.team t")
//            ,countQuery = "select count(m) from Member m") // 페이지가 많으면 countQuery 를 사용해야한다.
    Page<Member> findByAge(int age, Pageable pageable);

    Slice<Member> findSliceByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) //<--필수 애너테이션 / 자동으로 clear
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    //fetch 조인
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    //EntityGraph
    @Override
    @EntityGraph(attributePaths = {"team"}) //JPQL fetch 조인을 안쓰고 Member 를 조회하면서 Team 도 한번에 조회하고싶을때
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

//    @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all") // Member entity 에서 NameEntityGraph 매핑
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    //JPA Hint : Member 를 조회만 하겠다고 하이버네이트에 말하는것임 -> 최적화
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyUsername(String username);

    //JPA lock
    @Lock(LockModeType.PESSIMISTIC_WRITE) // db 에서 select for update 처럼 건들지 못하게 락 건다.
    List<Member> findLockByUsername(String username);
}
