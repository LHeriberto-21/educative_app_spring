package com.educative.app.hlv.repo;

import com.educative.app.hlv.models.Career;

import java.util.List;


public interface CareerRepository extends AppRepository<Career, Long> {
    List<Career> findByActiveTrue();

    boolean existsByCode(String code);

    boolean existsByName(String name);
}
