package com.educative.app.hlv.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AppRepository<T, ID> extends JpaRepository<T, ID> {
    List<T> findByActiveTrue();
    List<T> findByActiveFalse();
}
