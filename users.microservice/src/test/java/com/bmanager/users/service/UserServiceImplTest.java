package com.bmanager.users.service;

import com.bmanager.users.client.DirClient;
import com.bmanager.users.dto.UserPut;
import com.bmanager.users.model.ResultContainer;
import com.bmanager.users.model.UserModel;
import com.bmanager.users.repository.UserRepository;
import com.bmanager.users.service.impl.UserServiceImpl;
import com.bmanager.users.util.ResultStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private DirClient dirClient;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getUsers_common() {
        UserModel userInfo = new UserModel(1L, "testUser", "testUser", "testUser");;

        ResultContainer<List<UserModel>> expectedResult = new ResultContainer<>(ResultStatus.OK, new LinkedList<>());
        expectedResult.getContent().add(new UserModel(1L, "user1@mail.com", "password1", "user1"));
        expectedResult.getContent().add(new UserModel(2L, "user2@mail.com", "password2", "user2"));

        when(userRepository.getUsers(userInfo)).thenReturn(expectedResult);

        ResultContainer<List<UserModel>> result = userService.getUsers(userInfo);

        ResultContainer<List<UserModel>> currectResult = new ResultContainer<>(ResultStatus.OK, new LinkedList<>());
        currectResult.getContent().add(new UserModel(1L, "user1@mail.com", "password1", "user1"));
        currectResult.getContent().add(new UserModel(2L, "user2@mail.com", "password2", "user2"));

        assertEquals(result, currectResult);
    }

    @Test
    void getUser_common() {
        UserModel userInfo = new UserModel(1L, "testUser", "testUser", "testUser");;

        ResultContainer<UserModel> expectedResult = new ResultContainer<>(ResultStatus.OK, null);
        expectedResult.setContent(new UserModel(1L, "user1@mail.com", "password1", "user1"));

        Long id = 1L;

        when(userRepository.getUser(id, userInfo)).thenReturn(expectedResult);

        ResultContainer<UserModel> result = userService.getUser(id, userInfo);

        ResultContainer<UserModel> currectResult = new ResultContainer<>(ResultStatus.OK, null);
        currectResult.setContent(new UserModel(1L, "user1@mail.com", "password1", "user1"));

        assertEquals(result, currectResult);
    }

    @Test
    void getUser_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "testUser2", "testUser2", "testUser2");

        ResultContainer<UserModel> userRepositoryResponse = new ResultContainer<>(ResultStatus.OK, null);
        userRepositoryResponse.setContent(new UserModel(1L, "user1@mail.com", "password1", "user1"));

        Long id = 1L;

        when(userRepository.getUser(id, userInfo)).thenReturn(userRepositoryResponse);

        ResultContainer<UserModel> result = userService.getUser(id, userInfo);

        ResultContainer<UserModel> currectResult = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);

        assertEquals(result, currectResult);
    }

    @Test
    void updateUser_common() {
        UserModel userInfo = new UserModel(1L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultContainer<UserModel> userRepositoryResponse = new ResultContainer<>(ResultStatus.UPDATED, null);
        userRepositoryResponse.setContent(new UserModel(1L, "maxsmg@mail.ru", "$2a$10$Kl4hluNcaRa3VCQ/X.fXUe1cum1y.e0L0Q8BMO7u.ffPvo/DYx/PC", "maxsmg"));

        Long id = 1L;
        UserPut request = new UserPut("maxsmg@mail.ru", "123456", "maxsmg");

        when(userRepository.updateUser(id, request, userInfo)).thenReturn(userRepositoryResponse);

        ResultContainer<UserModel> result = userService.updateUser(id, request, userInfo);
        ResultContainer<UserModel> currectResult = new ResultContainer<>(ResultStatus.UPDATED, new UserModel(1L, "maxsmg@mail.ru", "$2a$10$Kl4hluNcaRa3VCQ/X.fXUe1cum1y.e0L0Q8BMO7u.ffPvo/DYx/PC", "maxsmg"));


        assertEquals(result, currectResult);
    }

    @Test
    void updateUser_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "testUser2", "testUser2", "testUser2");

        ResultContainer<UserModel> userRepositoryResponse = new ResultContainer<>(ResultStatus.UPDATED, null);
        userRepositoryResponse.setContent(new UserModel(1L, "maxsmg@mail.ru", "$2a$10$Kl4hluNcaRa3VCQ/X.fXUe1cum1y.e0L0Q8BMO7u.ffPvo/DYx/PC", "maxsmg"));

        Long id = 1L;
        UserPut request = new UserPut("maxsmg@mail.ru", "123456", "maxsmg");

        when(userRepository.updateUser(id, request, userInfo)).thenReturn(userRepositoryResponse);

        ResultContainer<UserModel> result = userService.updateUser(id, request, userInfo);
        ResultContainer<UserModel> currectResult = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);


        assertEquals(result, currectResult);
    }

    @Test
    void deleteUser_common() {
        UserModel userInfo = new UserModel(1L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultStatus userRepositoryResponse = ResultStatus.DELETED;

        Long id = 1L;

        when(userRepository.deleteUser(id, userInfo)).thenReturn(userRepositoryResponse);

        ResultStatus result = userService.deleteUser(id, userInfo);
        ResultStatus currectResult = ResultStatus.DELETED;

        assertEquals(result, currectResult);
    }

    @Test
    void deleteUser_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "testUser2", "testUser2", "testUser2");

        ResultStatus userRepositoryResponse = ResultStatus.DELETED;

        Long id = 1L;

        when(userRepository.deleteUser(id, userInfo)).thenReturn(userRepositoryResponse);

        ResultStatus result = userService.deleteUser(id, userInfo);
        ResultStatus currectResult = ResultStatus.PermissionDeniedToOtherUsers;

        assertEquals(result, currectResult);
    }
}
