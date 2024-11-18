package com.bmanager.dirs.service;

import com.bmanager.dirs.dto.DirPost;
import com.bmanager.dirs.dto.DirPut;
import com.bmanager.dirs.model.DirModel;
import com.bmanager.dirs.model.ResultContainer;
import com.bmanager.dirs.model.UserModel;
import com.bmanager.dirs.util.ResultStatus;

import java.util.List;

public interface DirService {
    public ResultContainer<List<DirModel>> getDirs(Long parentDirId, UserModel userInfo);
    public ResultContainer<DirModel> getDir(Long id, UserModel userInfo);
    public ResultContainer<DirModel> getRootDir(UserModel userInfo);
    public ResultContainer<DirModel> getParentDir(Long id, UserModel userInfo);
    public ResultContainer<DirModel> addDir(DirPost request, UserModel userInfo);
    public ResultContainer<DirModel> updateDir(Long id, DirPut request, UserModel userInfo);
    public ResultStatus deleteDir(Long id, UserModel userInfo);
}

