package com.bmanager.users.service;

import com.bmanager.users.dto.UserPost;
import com.bmanager.users.dto.UserPut;
import com.bmanager.users.model.ResultContainer;
import com.bmanager.users.model.UserModel;
import com.bmanager.users.util.ResultStatus;

import java.util.List;

public interface UserService {
    public ResultContainer<List<UserModel>> getUsers(UserModel userInfo);
    public ResultContainer<UserModel> getUser(Long id, UserModel userInfo);
    public ResultContainer<UserModel> addUser(UserPost request, UserModel userInfo);
    public ResultContainer<UserModel> updateUser(Long id, UserPut request, UserModel userInfo);
    public ResultStatus deleteUser(Long id, UserModel userInfo);
}
