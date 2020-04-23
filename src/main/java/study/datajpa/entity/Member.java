package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    //엔티티는 기본 생성자가 있어야한다.
    protected Member() { // protected ?? 아무데서나 호출되지 않게 하려고
        // Q. private 이 아니라 protected 인 이유??
        // A. 프록싱 기술을 사용할때 객체를 강제로 만들어야하는데 private 으로 막아두면
        // 문제가 발생할 수 있기 때문에 protected 로 열어둬야한다.
    }

    public Member(String username) {
        this.username = username;
    }

}
