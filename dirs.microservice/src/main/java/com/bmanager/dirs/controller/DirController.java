package com.bmanager.dirs.controller;

import com.bmanager.dirs.dto.DirPost;
import com.bmanager.dirs.dto.DirPut;
import com.bmanager.dirs.model.DirModel;
import com.bmanager.dirs.model.ResultContainer;
import com.bmanager.dirs.model.UserModel;
import com.bmanager.dirs.service.DirService;
import com.bmanager.dirs.service.impl.DirServiceImpl;
import com.bmanager.dirs.util.ResultStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dirs")
public class DirController {
    private final DirService dirService;
    private final Logger logger;
    private final ObjectMapper objectMapper;

    @Autowired
    public DirController(DirServiceImpl dirsService, Logger logger, ObjectMapper objectMapper) {
        this.dirService = dirsService;
        this.logger = logger;
        this.objectMapper = objectMapper;

        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @GetMapping
    public ResponseEntity<?> getDirs(@RequestHeader("X-user") String userInfoString, @RequestParam(name = "parentDirId") Long parentDirId) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<List<DirModel>> result = result = dirService.getDirs(parentDirId, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDir(@RequestHeader("X-user") String userInfoString, @PathVariable("id") Long id) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<DirModel> result = dirService.getDir(id, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @GetMapping("/getRoot")
    public ResponseEntity<?> getRootDir(@RequestHeader("X-user") String userInfoString) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<DirModel> result = dirService.getRootDir(userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @GetMapping("/getParent")
    public ResponseEntity<?> getParentDir(Long id, @RequestHeader("X-user") String userInfoString) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<DirModel> result = dirService.getParentDir(id, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @PostMapping
    public ResponseEntity<?> addDir(@RequestHeader("X-user") String userInfoString, @RequestBody DirPost newDir) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<DirModel> result = dirService.addDir(newDir, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDir(@RequestHeader("X-user") String userInfoString, @PathVariable(name = "id") Long id, @RequestBody DirPut updatedDir) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<DirModel> result = dirService.updateDir(id, updatedDir, userInfo);

        return new ResponseEntity<>(result, result.getStatus().toHttpStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDir(@RequestHeader("X-user") String userInfoString, @PathVariable(name = "id") Long id) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultStatus result = dirService.deleteDir(id, userInfo);

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

