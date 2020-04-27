package study.datajpa.entity;


import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass // 속성을 상속받아 테이블에 생성한다.
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdData;
    private LocalDateTime updatedData;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdData = now;
        updatedData = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedData = LocalDateTime.now();
    }
}
