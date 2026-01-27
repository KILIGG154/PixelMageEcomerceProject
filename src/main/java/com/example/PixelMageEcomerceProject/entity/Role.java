package com.example.PixelMageEcomerceProject.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Role_Id")
    private Integer roleId;

    @Column(name = "Role_Name", nullable = false, length = 100)
    private String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Account> accounts;
}
