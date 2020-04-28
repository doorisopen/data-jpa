package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.Valid;

public interface UsernameOnly {

//    String getUsername(); // interface 기반의 close projections

    @Value("#{target.username + ' ' + target.age}") // interface 기반의 open projections
    String getUsername();
}
