package com.felysoft.felysoftApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.felysoft.felysoftApp.util.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User implements UserDetails {
    public enum Gender {FEMENINO, MASCULINO}
    public enum TypeDoc {CC,TI,CE}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(length = 15, unique = true)
    private Long numIdentification;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeDoc typeDoc;

    @Column(length = 45, nullable = false)
    private String names;

    @Column(length = 45, nullable = false)
    private String lastNames;

    @Column(length = 45, nullable = false)
    private String address;

    @Column(length = 10, nullable = false)
    private Long phoneNumber;

    @Column(length = 320, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(length = 45, nullable = false)
    private String user_name;

    @Column(nullable = false)
    private String password;

    private String nameImg;

    @Column(length = 50)
    private String typeImg;

    @Column(length = 5000000)
    @Lob
    private byte[] image;

    @Column(nullable = false)
    @CreationTimestamp
    private Timestamp dateRegister;

    @Column(nullable = false)
    @UpdateTimestamp
    private Timestamp lastModification;

    @Column(nullable = false)
    private boolean eliminated;

    @Enumerated(EnumType.STRING)
    private Role role;

    // FOREIGN KEYS
    /*@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fkIdRole", nullable = false)
    private Role role;*/

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reserve> reserves;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
