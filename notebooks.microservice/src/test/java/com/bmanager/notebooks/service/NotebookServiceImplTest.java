package com.bmanager.notebooks.service;

import com.bmanager.notebooks.dto.NotebookPost;
import com.bmanager.notebooks.dto.NotebookPut;
import com.bmanager.notebooks.model.NotebookModel;
import com.bmanager.notebooks.model.ResultContainer;
import com.bmanager.notebooks.model.UserModel;
import com.bmanager.notebooks.repository.NotebookRepository;
import com.bmanager.notebooks.util.ResultStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class NotebookServiceImplTest {
    @MockBean
    private NotebookRepository notebookRepository;

    @Autowired
    private NotebookService notebookService;

    @Test
    void getNotebooks_common() {
        UserModel userInfo = new UserModel(1L, "testUser", "testUser", "testUser");

        ResultContainer<List<NotebookModel>> expectedResult = new ResultContainer<>(ResultStatus.OK, new LinkedList<>());
        expectedResult.getContent().add(new NotebookModel(2L, 1L, 1L, "Notebook1", ""));
        expectedResult.getContent().add(new NotebookModel(3L, 1L, 1L, "Notebook2", ""));

        Long parentDirId = 1L;

        when(notebookRepository.getNotebooks(parentDirId, userInfo)).thenReturn(expectedResult);

        ResultContainer<List<NotebookModel>> result = notebookService.getNotebooks(parentDirId, userInfo);

        ResultContainer<List<NotebookModel>> currectResult = new ResultContainer<>(ResultStatus.OK, new LinkedList<>());
        currectResult.getContent().add(new NotebookModel(2L, 1L, 1L, "Notebook1", ""));
        currectResult.getContent().add(new NotebookModel(3L, 1L, 1L, "Notebook2", ""));

        ListIterator<NotebookModel> expectedResultIterator = expectedResult.getContent().listIterator();
        ListIterator<NotebookModel> currectResultIterator = currectResult.getContent().listIterator();

        while (expectedResultIterator.hasNext()) {
            NotebookModel i = currectResultIterator.next();
            NotebookModel j = expectedResultIterator.next();

            i.setDatetime_created(j.getDatetime_created());
            i.setDatetime_updated(j.getDatetime_updated());
        }

        assertEquals(result, currectResult);
    }

    @Test
    void getNotebook_common() {
        UserModel userInfo = new UserModel(1L, "testUser", "testUser", "testUser");

        ResultContainer<NotebookModel> expectedResult = new ResultContainer<>(ResultStatus.OK, null);
        expectedResult.setContent(new NotebookModel(2L, 1L, 1L, "Notebook1", ""));

        Long id = 1L;

        when(notebookRepository.getNotebook(id, userInfo)).thenReturn(expectedResult);

        ResultContainer<NotebookModel> result = notebookService.getNotebook(id, userInfo);

        ResultContainer<NotebookModel> currectResult = new ResultContainer<>(ResultStatus.OK, null);
        currectResult.setContent(new NotebookModel(2L, 1L, 1L, "Notebook1", result.getContent().getDatetime_created(), result.getContent().getDatetime_updated(), ""));

        assertEquals(result, currectResult);
    }

    @Test
    void getNotebook_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "testUser2", "testUser2", "testUser2");

        ResultContainer<NotebookModel> expectedResult = new ResultContainer<>(ResultStatus.OK, null);
        expectedResult.setContent(new NotebookModel(2L, 1L, 1L, "Notebook1", ""));

        Long id = 1L;

        when(notebookRepository.getNotebook(id, userInfo)).thenReturn(expectedResult);

        ResultContainer<NotebookModel> result = notebookService.getNotebook(id, userInfo);

        ResultContainer<NotebookModel> currectResult = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);

        assertEquals(result, currectResult);
    }

    @Test
    void addDir_common() {
        UserModel userInfo = new UserModel(1L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultContainer<NotebookModel> expectedResult = new ResultContainer<>(ResultStatus.CREATED, null);
        expectedResult.setContent(new NotebookModel(2L, 4L, 1L, "Created Notebook1", ""));

        NotebookPost request = new NotebookPost(4L, "Created Notebook1", 2L);
        NotebookModel requestModel = new NotebookModel(request);

        when(notebookRepository.addNotebook(any(), eq(userInfo))).thenReturn(expectedResult);

        ResultContainer<NotebookModel> result = notebookService.addNotebook(request, userInfo);

        ResultContainer<NotebookModel> currectResult = new ResultContainer<>(ResultStatus.CREATED, new NotebookModel(2L, 4L, 1L, "Created Notebook1", ""));
        currectResult.getContent().setDatetime_created(result.getContent().getDatetime_created());
        currectResult.getContent().setDatetime_updated(result.getContent().getDatetime_updated());

        assertEquals(result, currectResult);
    }

    @Test
    void updateNotebook_common() {
        UserModel userInfo = new UserModel(1L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultContainer<NotebookModel> currentNotebook = new ResultContainer<>(ResultStatus.OK, null);
        currentNotebook.setContent(new NotebookModel(2L, 1L, 1L, "Notebook1", ""));

        ResultContainer<NotebookModel> expectedResult = new ResultContainer<>(ResultStatus.UPDATED, null);
        expectedResult.setContent(new NotebookModel(2L, 4L, 1L, "Updated Notebook1", "Updated"));

        NotebookPut request = new NotebookPut(4L, "Updated Notebook1", "Updated");

        Long id = 2L;

        when(notebookRepository.getNotebook(id, userInfo)).thenReturn(currentNotebook);
        when(notebookRepository.updateNotebook(id, request, userInfo)).thenReturn(expectedResult);

        ResultContainer<NotebookModel> result = notebookService.updateNotebook(id, request, userInfo);
        System.out.println(result == null);
        ResultContainer<NotebookModel> currectResult = new ResultContainer<>(ResultStatus.UPDATED, new NotebookModel(2L, 4L, 1L, "Updated Notebook1", "Updated"));
        currectResult.getContent().setDatetime_created(result.getContent().getDatetime_created());
        currectResult.getContent().setDatetime_updated(result.getContent().getDatetime_updated());

        assertEquals(result, currectResult);
    }

    @Test
    void updateNotebook_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultContainer<NotebookModel> currentNotebook = new ResultContainer<>(ResultStatus.OK, null);
        currentNotebook.setContent(new NotebookModel(2L, 1L, 1L, "Notebook1", ""));

        ResultContainer<NotebookModel> expectedResult = new ResultContainer<>(ResultStatus.UPDATED, null);
        expectedResult.setContent(new NotebookModel(2L, 4L, 1L, "Updated Notebook1", "Updated"));

        NotebookPut request = new NotebookPut(4L, "Updated Notebook1", "Updated");

        Long id = 2L;

        when(notebookRepository.getNotebook(id, userInfo)).thenReturn(currentNotebook);
        when(notebookRepository.updateNotebook(id, request, userInfo)).thenReturn(expectedResult);

        ResultContainer<NotebookModel> result = notebookService.updateNotebook(id, request, userInfo);
        ResultContainer<NotebookModel> currectResult = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);

        assertEquals(result, currectResult);
    }

    @Test
    void deleteNotebook_common() {
        UserModel userInfo = new UserModel(1L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultStatus expectedResult = ResultStatus.DELETED;

        ResultContainer<NotebookModel> notebookModel = new ResultContainer<>(ResultStatus.OK, null);
        notebookModel.setContent((new NotebookModel(2L, 1L, 1L, "Notebook1", "")));

        Long id = 2L;

        when(notebookRepository.deleteNotebook(id, userInfo)).thenReturn(expectedResult);
        when(notebookRepository.getNotebook(id, userInfo)).thenReturn(notebookModel);

        ResultStatus result = notebookService.deleteNotebook(id, userInfo);
        ResultStatus currectResult = ResultStatus.DELETED;

        assertEquals(result, currectResult);
    }

    @Test
    void deleteNotebook_permission_denied_for_other_user() {
        UserModel userInfo = new UserModel(2L, "maxsmg", "123456", "maxsmg@mail.ru");;

        ResultStatus expectedResult = ResultStatus.DELETED;

        ResultContainer<NotebookModel> notebookModel = new ResultContainer<>(ResultStatus.OK, null);
        notebookModel.setContent((new NotebookModel(2L, 1L, 1L, "Notebook1", "")));

        Long id = 2L;

        when(notebookRepository.deleteNotebook(id, userInfo)).thenReturn(expectedResult);
        when(notebookRepository.getNotebook(id, userInfo)).thenReturn(notebookModel);

        ResultStatus result = notebookService.deleteNotebook(id, userInfo);
        ResultStatus currectResult = ResultStatus.PermissionDeniedToOtherUsers;

        assertEquals(result, currectResult);
    }
}
