package com.educative.app.hlv.models;

import com.educative.app.hlv.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"enrollments", "subjectsTeaching"})
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String lastname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(unique = true, length = 20)
    private String registrationNumber;

    private Integer currentSemester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id")
    private Career career;


    @Column(length = 100)
    private String academicTitle;

    @Column(length = 500)
    private String profilePhotoUrl;

    @Column(length = 255)
    private String profilePhotoName;



    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Enrollment> enrollments = new HashSet<>();


    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Subject> subjectsTeaching = new HashSet<>();


    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


    public String getFullName() {
        return this.name + " " + this.lastname;
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(this.role);
    }

    public boolean isTeacher() {
        return Role.TEACHER.equals(this.role);
    }

    public boolean isStudent() {
        return Role.STUDENT.equals(this.role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;    // no manejamos expiración de cuentas por ahora
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;    // no manejamos bloqueo de cuentas por ahora
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;    // no manejamos expiración de credenciales
    }

    @Override
    public boolean isEnabled() {
        return this.active;
    }
}