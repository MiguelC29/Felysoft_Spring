package com.felysoft.felysoftApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;

    @Column(length = 45, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean eliminated;

    //FOREING KEYS SPRING SECURITY
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name="role_permissions", joinColumns = @JoinColumn(name= "role_id"), inverseJoinColumns = @JoinColumn(name= "permission_id"))
    private Set<Permission> permissionList = new HashSet<>();


    // FOREIGN KEYS
//    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<User> user;
}