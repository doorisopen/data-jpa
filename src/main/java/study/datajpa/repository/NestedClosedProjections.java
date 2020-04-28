package study.datajpa.repository;

public interface NestedClosedProjections { // 중첩 구조 : 연관된 정보를 가져오기

    String getUsername();
    TeamInfo getTeam();

    interface TeamInfo {
        String getName();
    }

}
