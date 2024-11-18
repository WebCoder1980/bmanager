package com.bmanager.users.controller;

import com.bmanager.users.dto.UserPost;
import com.bmanager.users.dto.UserPut;
import com.bmanager.users.model.ResultContainer;
import com.bmanager.users.model.UserModel;
import com.bmanager.users.service.UserService;
import com.bmanager.users.service.impl.UserServiceImpl;
import com.bmanager.users.util.ResultStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService usersService;
    private final Logger logger;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserController(UserServiceImpl userService, Logger logger, ObjectMapper objectMapper) {
        this.usersService = userService;
        this.logger = logger;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<?> getUsers(@RequestHeader("X-user") String userInfoString) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<List<UserModel>> result = usersService.getUsers(userInfo);

        return new ResponseEntity(result, result.getStatus().toHttpStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@RequestHeader("X-user") String userInfoString, @PathVariable("id") Long id) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<UserModel> result = usersService.getUser(id, userInfo);;

        return new ResponseEntity(result, result.getStatus().toHttpStatus());
    }

    public ResponseEntity<?> addDirs(@RequestHeader("X-user") String userInfoString, @RequestBody UserPost newUser) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<UserModel> result = usersService.addUser(newUser, userInfo);;

        return new ResponseEntity(result, result.getStatus().toHttpStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDir(@RequestHeader("X-user") String userInfoString, @PathVariable("id") Long id, @RequestBody UserPut updatedUser) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultContainer<UserModel> result = usersService.updateUser(id, updatedUser, userInfo);

        return new ResponseEntity(result, result.getStatus().toHttpStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDir(@RequestHeader("X-user") String userInfoString, @PathVariable("id") Long id) {
        UserModel userInfo = getUserInfo(userInfoString);
        ResultStatus result = usersService.deleteUser(id, userInfo);

        return new ResponseEntity(result, result.toHttpStatus());
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
