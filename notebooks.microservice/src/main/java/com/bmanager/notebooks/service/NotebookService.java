package com.bmanager.notebooks.service;

import com.bmanager.notebooks.dto.NotebookPost;
import com.bmanager.notebooks.dto.NotebookPut;
import com.bmanager.notebooks.model.NotebookModel;
import com.bmanager.notebooks.model.ResultContainer;
import com.bmanager.notebooks.model.UserModel;
import com.bmanager.notebooks.util.ResultStatus;

import java.util.List;

public interface NotebookService {
    public ResultContainer<List<NotebookModel>> getNotebooks(Long parentDirId, UserModel userInfo);
    public ResultContainer<NotebookModel> getNotebook(Long id, UserModel userInfo);
    public ResultContainer<NotebookModel> addNotebook(NotebookPost request, UserModel userInfo);
    public ResultContainer<NotebookModel> updateNotebook(Long id, NotebookPut request, UserModel userInfo);
    public ResultStatus deleteNotebook(Long id, UserModel userInfo);
}

