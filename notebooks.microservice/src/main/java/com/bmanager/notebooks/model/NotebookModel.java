package com.bmanager.notebooks.model;

import com.bmanager.notebooks.dto.NotebookPost;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "notebooks")
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class NotebookModel {
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

    @Column(name = "datetime_updated", nullable = false)
    private LocalDateTime datetime_updated;

    @Column(name = "content", nullable = false, length = 10000)
    private String content;

    public NotebookModel(Long id, Long parentDirId, Long userId, String name, String content) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        setId(id);
        setParentDirId(parentDirId);
        setUserId(userId);
        setName(name);
        setDatetime_created(currentDateTime);
        setDatetime_updated(currentDateTime);
        setContent(content);
    }

    public NotebookModel(Long id, Long parentDirId, Long userId, String name, LocalDateTime datetime_created, LocalDateTime datetime_updated, String content) {
        setId(id);
        setParentDirId(parentDirId);
        setUserId(userId);
        setName(name);
        setDatetime_created(datetime_created);
        setDatetime_updated(datetime_updated);
        setContent(content);
    }

    public NotebookModel(NotebookPost request) {
        setParentDirId(request.getParentDirId());
        setName(request.getName());
        setUserId(request.getUserId());
        setDatetime_created(LocalDateTime.now());
        setDatetime_updated(LocalDateTime.now());
        setContent("");
    }}
