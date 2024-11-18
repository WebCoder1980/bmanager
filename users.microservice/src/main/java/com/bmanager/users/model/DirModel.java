package com.bmanager.users.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "dirs")
@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DirModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "parentDirId", nullable = true)
    private Long parentDirId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "datetime_created", nullable = false)
    private LocalDateTime datetime_created;

}
