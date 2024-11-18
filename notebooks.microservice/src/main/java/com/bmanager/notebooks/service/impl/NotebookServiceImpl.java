package com.bmanager.notebooks.service.impl;

import com.bmanager.notebooks.dto.NotebookPost;
import com.bmanager.notebooks.dto.NotebookPut;
import com.bmanager.notebooks.exception.NoValidData;
import com.bmanager.notebooks.model.NotebookModel;
import com.bmanager.notebooks.model.ResultContainer;
import com.bmanager.notebooks.model.UserModel;
import com.bmanager.notebooks.repository.NotebookRepository;
import com.bmanager.notebooks.util.ResultStatus;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmanager.notebooks.service.NotebookService;

import java.util.List;

@Service
public class NotebookServiceImpl implements NotebookService {
    private Logger logger;
    private NotebookRepository notebookRepository;
    
    @Autowired
    public NotebookServiceImpl(Logger logger, NotebookRepository notebookRepository) {
        this.logger = logger;
        this.notebookRepository = notebookRepository;
    }

    @Override
    public ResultContainer<List<NotebookModel>> getNotebooks(Long parentDirId, UserModel userInfo) {
        ResultContainer<List<NotebookModel>> result = notebookRepository.getNotebooks(parentDirId, userInfo);

        return result;
    }
    
    @Override
    public ResultContainer<NotebookModel> getNotebook(Long id, UserModel userInfo) {
        ResultContainer<NotebookModel> result = notebookRepository.getNotebook(id, userInfo);

        if (result.getContent().getUserId() != userInfo.getId()) {
            result = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);
        }

        return result;
    }

    @Override
    public ResultContainer<NotebookModel> addNotebook(NotebookPost request, UserModel userInfo) {
        NotebookModel newNotebookModel = new NotebookModel(request);
        ResultContainer<NotebookModel> result = null;
        try {
            if (hasUser(newNotebookModel.getUserId()) == false) {
                String errorMessage = "Invalid user id";
                throw new NoValidData(errorMessage);
            }
            if (newNotebookModel.getParentDirId() != null && hasDir(newNotebookModel.getParentDirId()) == false) {
                String errorMessage = "Incorrect parent directory id";
                throw new NoValidData(errorMessage);
            }

            result = notebookRepository.addNotebook(newNotebookModel, userInfo);
        } catch (Exception e) {
            logger.error(e);
            result = new ResultContainer(ResultStatus.WrongData);
        }

        return result;
    }

    @Override
    public ResultContainer<NotebookModel> updateNotebook(Long id, NotebookPut request, UserModel userInfo) {
        ResultContainer<NotebookModel> result = null;
        ResultContainer<NotebookModel> currentNotebook = getNotebook(id, userInfo);

        if (currentNotebook.getStatus() != ResultStatus.OK) {
            result = new ResultContainer<>(currentNotebook.getStatus(), null);
        }
        else if (currentNotebook.getContent().getUserId() != userInfo.getId()) {
            result = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);
        }
        else {
            try {
                if (request.getParentDirId() != null) {
                    if (!hasDir(request.getParentDirId())) {
                        throw new NoValidData("Wrong parent dir id ");
                    }
                }

                result = notebookRepository.updateNotebook(id, request, userInfo);
            } catch (Exception e) {
                logger.error(e);
                result = new ResultContainer<NotebookModel>(ResultStatus.WrongData);
            }
        }

        return result;
    }

    @Override
    public ResultStatus deleteNotebook(Long id, UserModel userInfo) {
        ResultContainer<NotebookModel> currentNotebook = notebookRepository.getNotebook(id, userInfo);
        ResultStatus result = null;

        if (currentNotebook.getStatus() != ResultStatus.OK) {
            result = currentNotebook.getStatus();
        }
        else if (currentNotebook.getContent().getUserId() != userInfo.getId()) {
            result = ResultStatus.PermissionDeniedToOtherUsers;
        }
        else {
            result = notebookRepository.deleteNotebook(id, userInfo);
        }

        return result;
    }

    private boolean hasUser(Long userId) {
        return true;
    }

    private boolean hasDir(Long id) {
        return true;
    }
}

