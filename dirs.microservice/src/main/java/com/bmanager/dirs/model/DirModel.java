package com.bmanager.dirs.model;

import com.bmanager.dirs.dto.DirPost;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "dirs")
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DirModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "parentDirId")
    private Long parentDirId;

    @Column(name = "userId", nullable = false)
    private Long userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "datetime_created", nullable = false)
    private LocalDateTime datetime_created;

    public DirModel(Long id, Long parentDirId, Long userId, String name) {
        setId(id);
        setParentDirId(parentDirId);
        setUserId(userId);
        setName(name);
        setDatetime_created(LocalDateTime.now());
    }

    public DirModel(Long id, Long parentDirId, Long userId, String name, LocalDateTime datetime_created) {
        setId(id);
        setParentDirId(parentDirId);
        setUserId(userId);
        setName(name);
        setDatetime_created(datetime_created);
    }

    public DirModel(DirPost request) {
        setParentDirId(request.getParentDirId());
        setName(request.getName());
        setDatetime_created(LocalDateTime.now());
    }}
