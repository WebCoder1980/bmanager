package com.bmanager.users.service.impl;

import com.bmanager.users.client.DirClient;
import com.bmanager.users.dto.DirPost;
import com.bmanager.users.dto.UserPost;
import com.bmanager.users.dto.UserPut;
import com.bmanager.users.exception.NoValidData;
import com.bmanager.users.model.ResultContainer;
import com.bmanager.users.model.UserModel;
import com.bmanager.users.repository.UserRepository;
import com.bmanager.users.service.UserService;
import com.bmanager.users.util.ResultStatus;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final DirClient dirClient;
    private final UserRepository userRepository;
    private final Logger logger;
    private final PasswordEncoder encoder;


    @Autowired
    public UserServiceImpl(DirClient dirClient, UserRepository userRepository, Logger logger, PasswordEncoder encoder) {
        this.dirClient = dirClient;
        this.userRepository = userRepository;
        this.logger = logger;
        this.encoder = encoder;
    }

    @Override
    public ResultContainer<List<UserModel>> getUsers(UserModel userInfo) {
        ResultContainer<List<UserModel>> result = userRepository.getUsers(userInfo);
        return result;
    }

    @Override
    public ResultContainer<UserModel> getUser(Long id, UserModel userInfo) {
        ResultContainer<UserModel> result = null;
        if (id == userInfo.getId()) {
            result = userRepository.getUser(id, userInfo);
        }
        else {
            result = new ResultContainer<UserModel>(ResultStatus.PermissionDeniedToOtherUsers, null);
        }
        return result;
    }

    @Override
    public ResultContainer<UserModel> addUser(UserPost request, UserModel userInfo) {
        ResultContainer<UserModel> result = new ResultContainer<UserModel>(ResultStatus.CREATED, new UserModel(request));

        try {
            if (hasName(result.getContent().getUsername()) == true) {
                throw new NoValidData("Name already taken");
            }

            userRepository.addUser(request, userInfo);

            DirPost newDir = new DirPost();
            newDir.setName(result.getContent().getUsername());
            newDir.setUserId(result.getContent().getId());
            dirClient.addDir(newDir);
        } catch (NoValidData e) {
            logger.error(e);
            result = new ResultContainer<UserModel>(ResultStatus.WrongData, null);
        } catch (Exception e) {
            logger.error(e);
            result = new ResultContainer<UserModel>(ResultStatus.InternalError, null);
        }
        return result;
    }

    @Override
    public ResultContainer<UserModel> updateUser(Long id, UserPut request, UserModel userInfo) {
        request.setPassword(encoder.encode(request.getPassword()));
        ResultContainer<UserModel> result = null;
        if (id == userInfo.getId()) {
            try {
                if (request.getUsername() != null) {
                    if (hasName(request.getUsername()) == true) {
                        String errorMessage = "Username taken";
                        throw new NoValidData(errorMessage);
                    }
                }

                result = userRepository.updateUser(id, request, userInfo);
            } catch (NoValidData e) {
                logger.error(e);
                result = new ResultContainer<UserModel>(ResultStatus.WrongData, null);
            } catch (Exception e) {
                logger.error(e);
                result = new ResultContainer<UserModel>(ResultStatus.InternalError, null);
            }
        }
        else {
            result = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);
        }

        return result;
    }

    @Override
    public ResultStatus deleteUser(Long id, UserModel userInfo) {
        ResultStatus result = null;
        if (id == userInfo.getId()) {
            result = userRepository.deleteUser(id, userInfo);
        }
        else {
            result = ResultStatus.PermissionDeniedToOtherUsers;
        }

        return result;
    }


    private boolean hasName(String name) {
        boolean result = userRepository.hasName(name);
        return result;
    }
}
