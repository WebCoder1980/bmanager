package com.bmanager.notebooks.controller;

import com.bmanager.notebooks.dto.NotebookPost;
import com.bmanager.notebooks.dto.NotebookPut;
import com.bmanager.notebooks.model.NotebookModel;
import com.bmanager.notebooks.model.ResultContainer;
import com.bmanager.notebooks.model.UserModel;
import com.bmanager.notebooks.service.NotebookService;
import com.bmanager.notebooks.service.impl.NotebookServiceImpl;
import com.bmanager.notebooks.util.ResultStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notebooks")
public class NotebookController {
    private final NotebookService notebookService;
    private final Logger logger;
    private final ObjectMapper objectMapper;

    @Autowired
    public NotebookController(NotebookServiceImpl dirsService, Logger logger, ObjectMapper objectMapper) {
        this.notebookService = dirsService;
        this.logger = logger;
        this.objectMapper = objectMapper;

        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @GetMapping
    public ResponseEntity<?> getNotebooks(@RequestHeader("X-user") String userInfoString, @RequestParam(name = "parentDirId") Long parentDirId) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<List<NotebookModel>> result = notebookService.getNotebooks(parentDirId, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotebook(@RequestHeader("X-user") String userInfoString, @PathVariable("id") Long id) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<NotebookModel> result = notebookService.getNotebook(id, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @PostMapping
    public ResponseEntity<?> addNotebook(@RequestHeader("X-user") String userInfoString, @RequestBody NotebookPost newDir) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<NotebookModel> result = notebookService.addNotebook(newDir, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateNotebook(@RequestHeader("X-user") String userInfoString, @PathVariable(name = "id") Long id, @RequestBody NotebookPut updatedDir) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<NotebookModel> result = notebookService.updateNotebook(id, updatedDir, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNotebook(@RequestHeader("X-user") String userInfoString, @PathVariable(name = "id") Long id) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultStatus result = notebookService.deleteNotebook(id, userInfo);

        return new ResponseEntity<>(result, result.toHttpStatus());
    }

    private UserModel getUserInfo(String userModelString) {
        UserModel userModel = null;
        try {
            userModel = objectMapper.readValue(userModelString, UserModel.class);
        }
        catch(Exception e) {
            logger.error(e);
        }

        return userModel;
    }
}

