package com.bmanager.dirs.service;

import com.bmanager.dirs.dto.DirPut;
import com.bmanager.dirs.model.ResultContainer;
import com.bmanager.dirs.model.DirModel;
import com.bmanager.dirs.model.UserModel;
import com.bmanager.dirs.repository.DirRepository;
import com.bmanager.dirs.service.impl.DirServiceImpl;
import com.bmanager.dirs.util.ResultStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class DirServiceImplTest {

    @MockBean
    private DirRepository dirRepository;

    @Autowired
    private DirService dirService;

    @Test
    void getDirs_common() {
        UserModel userInfo = new UserModel(1L, "testUser", "testUser", "testUser");;

        ResultContainer<List<DirModel>> expectedResult = new ResultContainer<>(ResultStatus.OK, new LinkedList<>());
        expectedResult.getContent().add(new DirModel(2L, 1L, 1L, "Dir1"));
        expectedResult.getContent().add(new DirModel(3L, 1L, 1L, "Dir2"));

        Long parentDirId = 1L;

        when(dirRepository.getDirs(parentDirId, userInfo)).thenReturn(expectedResult);

        ResultContainer<List<DirModel>> result = dirService.getDirs(parentDirId, userInfo);

        ResultContainer<List<DirModel>> currectResult = new ResultContainer<>(ResultStatus.OK, new LinkedList<>());
        currectResult.getContent().add(new DirModel(2L, 1L, 1L, "Dir1"));
        currectResult.getContent().add(new DirModel(3L, 1L, 1L, "Dir2"));

        ListIterator<DirModel> expectedResultIterator = expectedResult.getContent().listIterator();
        ListIterator<DirModel> currectResultIterator = currectResult.getContent().listIterator();

        while (expectedResultIterator.hasNext()) {
            DirModel i = currectResultIterator.next();
            DirModel j = expectedResultIterator.next();
            i.setDatetime_created(j.getDatetime_created());
        }

        assertEquals(result, currectResult);
    }

    @Test
    void getDir_common() {
        UserModel userInfo = new UserModel(1L, "testUser", "testUser", "testUser");

        ResultContainer<DirModel> expectedResult = new ResultContainer<>(ResultStatus.OK, null);
        expectedResult.setContent(new DirModel(2L, 1L, 1L, "Dir1"));

        Long id = 1L;

        when(dirRepository.getDir(id, userInfo)).thenReturn(expectedResult);

        ResultContainer<DirModel> result = dirService.getDir(id, userInfo);

        ResultContainer<DirModel> currectResult = new ResultContainer<>(ResultStatus.OK, null);
        currectResult.setContent(new DirModel(2L, 1L, 1L, "Dir1", result.getContent().getDatetime_created()));

        assertEquals(result, currectResult);
    }

    @Test
    void getDir_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "testUser2", "testUser2", "testUser2");

        ResultContainer<DirModel> expectedResult = new ResultContainer<>(ResultStatus.OK, null);
        expectedResult.setContent(new DirModel(2L, 1L, 1L, "Dir1"));

        Long id = 1L;

        when(dirRepository.getDir(id, userInfo)).thenReturn(expectedResult);

        ResultContainer<DirModel> result = dirService.getDir(id, userInfo);

        ResultContainer<DirModel> currectResult = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);

        assertEquals(result, currectResult);
    }

    @Test
    void addDir_common() {
        UserModel userInfo = new UserModel(1L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultContainer<DirModel> expectedResult = new ResultContainer<>(ResultStatus.CREATED, null);
        expectedResult.setContent(new DirModel(2L, 4L, 1L, "Created Dir1"));

        ResultContainer<DirModel> parentDirResponse = new ResultContainer<>(ResultStatus.OK, null);
        parentDirResponse.setContent(new DirModel(1L, null, 1L, "Parent Dir"));

        DirPut request = new DirPut(4L, "Created Dir1");

        Long id = 2L;

        when(dirRepository.getParentDir(id, userInfo)).thenReturn(parentDirResponse);
        when(dirRepository.updateDir(id, request, userInfo)).thenReturn(expectedResult);

        ResultContainer<DirModel> result = dirService.updateDir(id, request, userInfo);
        ResultContainer<DirModel> currectResult = new ResultContainer<>(ResultStatus.CREATED, new DirModel(2L, 4L, 1L, "Created Dir1"));
        currectResult.getContent().setDatetime_created(result.getContent().getDatetime_created());

        assertEquals(result, currectResult);
    }

    @Test
    void addDir_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultContainer<DirModel> expectedResult = new ResultContainer<>(ResultStatus.CREATED, null);
        expectedResult.setContent(new DirModel(2L, 4L, 1L, "Created Dir1"));

        ResultContainer<DirModel> parentDirResponse = new ResultContainer<>(ResultStatus.OK, null);
        parentDirResponse.setContent(new DirModel(1L, null, 1L, "Parent Dir"));

        DirPut request = new DirPut(4L, "Created Dir1");

        Long id = 2L;

        when(dirRepository.getParentDir(id, userInfo)).thenReturn(parentDirResponse);
        when(dirRepository.updateDir(id, request, userInfo)).thenReturn(expectedResult);

        ResultContainer<DirModel> result = dirService.updateDir(id, request, userInfo);
        ResultContainer<DirModel> currectResult = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);

        assertEquals(result, currectResult);
    }

    @Test
    void updateDir_common() {
        UserModel userInfo = new UserModel(1L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultContainer<DirModel> expectedResult = new ResultContainer<>(ResultStatus.UPDATED, null);
        expectedResult.setContent(new DirModel(2L, 4L, 1L, "Updated Dir1"));

        ResultContainer<DirModel> parentDirResponse = new ResultContainer<>(ResultStatus.OK, null);
        parentDirResponse.setContent(new DirModel(1L, null, 1L, "Parent Dir"));

        DirPut request = new DirPut(4L, "Updated Dir1");

        Long id = 2L;

        when(dirRepository.getParentDir(id, userInfo)).thenReturn(parentDirResponse);
        when(dirRepository.updateDir(id, request, userInfo)).thenReturn(expectedResult);

        ResultContainer<DirModel> result = dirService.updateDir(id, request, userInfo);
        ResultContainer<DirModel> currectResult = new ResultContainer<>(ResultStatus.UPDATED, new DirModel(2L, 4L, 1L, "Updated Dir1"));
        currectResult.getContent().setDatetime_created(result.getContent().getDatetime_created());

        assertEquals(result, currectResult);
    }

    @Test
    void updateDir_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultContainer<DirModel> expectedResult = new ResultContainer<>(ResultStatus.UPDATED, null);
        expectedResult.setContent(new DirModel(2L, 4L, 1L, "Updated Dir1"));

        ResultContainer<DirModel> parentDirResponse = new ResultContainer<>(ResultStatus.OK, null);
        parentDirResponse.setContent(new DirModel(1L, null, 1L, "Parent Dir"));

        DirPut request = new DirPut(4L, "Updated Dir1");

        Long id = 2L;

        when(dirRepository.getParentDir(id, userInfo)).thenReturn(parentDirResponse);
        when(dirRepository.updateDir(id, request, userInfo)).thenReturn(expectedResult);

        ResultContainer<DirModel> result = dirService.updateDir(id, request, userInfo);
        ResultContainer<DirModel> currectResult = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);

        assertEquals(result, currectResult);
    }

    @Test
    void deleteDir_common() {
        UserModel userInfo = new UserModel(1L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultStatus expectedResult = ResultStatus.DELETED;

        ResultContainer<DirModel> parentDirResponse = new ResultContainer<>(ResultStatus.OK, null);
        parentDirResponse.setContent(new DirModel(1L, null, 1L, "Parent Dir"));

        Long id = 2L;

        when(dirRepository.getParentDir(id, userInfo)).thenReturn(parentDirResponse);
        when(dirRepository.deleteDir(id, userInfo)).thenReturn(expectedResult);

        ResultStatus result = dirService.deleteDir(id, userInfo);
        ResultStatus currectResult = ResultStatus.DELETED;

        assertEquals(result, currectResult);
    }

    @Test
    void deleteDir_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultStatus expectedResult = ResultStatus.DELETED;

        ResultContainer<DirModel> parentDirResponse = new ResultContainer<>(ResultStatus.OK, null);
        parentDirResponse.setContent(new DirModel(1L, null, 1L, "Parent Dir"));

        Long id = 2L;

        when(dirRepository.getParentDir(id, userInfo)).thenReturn(parentDirResponse);
        when(dirRepository.deleteDir(id, userInfo)).thenReturn(expectedResult);

        ResultStatus result = dirService.deleteDir(id, userInfo);
        ResultStatus currectResult = ResultStatus.PermissionDeniedToOtherUsers;

        assertEquals(result, currectResult);
    }
}