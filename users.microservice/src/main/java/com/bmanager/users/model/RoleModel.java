package com.bmanager.users.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class RoleModel {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="id")
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(name="name", length = 20)
  private ERole name;

  public RoleModel(ERole name) {
    this.name = name;
  }
}