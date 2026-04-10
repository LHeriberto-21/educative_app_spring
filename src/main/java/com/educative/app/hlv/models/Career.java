package com.educative.app.hlv.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "careers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"students", "subjects"})
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(length = 300)
    private String description;

    @Column(nullable = false)
    private Integer totalSemesters;

    @Column(nullable = false)
    private Boolean active = true;


    @Builder.Default
    @OneToMany(mappedBy = "career", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<User> students = new HashSet<>();

    // Una carrera tiene muchas materias
    @Builder.Default
    @OneToMany(mappedBy = "career", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private Set<Subject> subjects = new HashSet<>();
}