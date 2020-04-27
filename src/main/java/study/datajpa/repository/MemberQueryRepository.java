package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository { // MemberRepository 인터페이스 구현이 아닌 복잡한 쿼리일때 사용하는 Repository

    private final EntityManager em;

    List<Member> findAllMembers(){
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
