package study.datajpa.repository;

public class UsernameOnlyDto { // class 기반 projections

    private final String username;

    public UsernameOnlyDto(String username) { // 파라미터 이름을 가지고 분석을한다.
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
