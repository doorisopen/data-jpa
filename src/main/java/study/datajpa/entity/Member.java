package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
        name = "Member.findByUsername",
        query = "select m from Member m where m.username = :username"
)
@NamedEntityGraph(name = "Member.all", attributeNodes = @NamedAttributeNode("team"))
public class Member extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // 엔티티는 기본 생성자가 있어야한다.
    // protected ?? 아무데서나 호출되지 않게 하려고
    // Q. private 이 아니라 protected 인 이유??
    // A. 프록싱 기술을 사용할때 객체를 강제로 만들어야하는데 private 으로 막아두면
    // 문제가 발생할 수 있기 때문에 protected 로 열어둬야한다.

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    //==연관관계 메서드==//
    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

}
