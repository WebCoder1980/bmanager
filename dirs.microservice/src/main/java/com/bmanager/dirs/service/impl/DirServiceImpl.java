package com.bmanager.dirs.service.impl;

import com.bmanager.dirs.dto.DirPost;
import com.bmanager.dirs.dto.DirPut;
import com.bmanager.dirs.exception.NoValidData;
import com.bmanager.dirs.model.DirModel;
import com.bmanager.dirs.model.ResultContainer;
import com.bmanager.dirs.model.UserModel;
import com.bmanager.dirs.repository.DirRepository;
import com.bmanager.dirs.util.ResultStatus;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bmanager.dirs.service.DirService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DirServiceImpl implements DirService {
    private Logger logger;
    private DirRepository dirRepository;
    
    @Autowired
    public DirServiceImpl(Logger logger, DirRepository dirRepository) {
        this.logger = logger;
        this.dirRepository = dirRepository;
    }

    @Override
    public ResultContainer<List<DirModel>> getDirs(Long parentDirId, UserModel userInfo) {
        ResultContainer<List<DirModel>> result = dirRepository.getDirs(parentDirId, userInfo);
        return result;
    }
    
    @Override
    public ResultContainer<DirModel> getDir(Long id, UserModel userInfo) {
        ResultContainer<DirModel> result = dirRepository.getDir(id, userInfo);

        if (result.getContent().getUserId() != userInfo.getId()) {
            result = new ResultContainer<>(ResultStatus.PermissionDeniedToOtherUsers, null);
        }

        return result;
    }

    @Override
    public ResultContainer<DirModel> addDir(DirPost request, UserModel userInfo) {
        DirModel newDirModel = new DirModel(request);
        newDirModel.setUserId(userInfo.getId());

        ResultContainer<DirModel> parentDirModel = dirRepository.getParentDir(request.getParentDirId(), userInfo);
        ResultContainer<DirModel> result = null;

        if (parentDirModel.getStatus() != ResultStatus.OK) {
            result = parentDirModel;
        }
        else if (parentDirModel.getContent().getUserId() != userInfo.getId()) {
            result = new ResultContainer<DirModel>(ResultStatus.PermissionDeniedToOtherUsers, null);
        }
        else {
            try {
                if (hasUser(newDirModel.getUserId()) == false) {
                    String errorMessage = "Invalid user id";
                    throw new NoValidData(errorMessage);
                }
                if (newDirModel.getParentDirId() != null && hasDir(newDirModel.getParentDirId()) == false) {
                    String errorMessage = "Incorrect parent directory id";
                    throw new NoValidData(errorMessage);
                }

                result = dirRepository.addDir(newDirModel, userInfo);
            } catch (Exception e) {
                logger.error(e);
                result = new ResultContainer(ResultStatus.WrongData);
            }
        }

        return result;
    }

    @Override
    public ResultContainer<DirModel> getRootDir(UserModel userInfo) {
        ResultContainer<DirModel> result = dirRepository.getRootDir(userInfo);
        if (result.getContent() == null) {
            logger.info("result is null!");
            DirModel newDir = new DirModel(null, null, userInfo.getId(), userInfo.getUsername(), LocalDateTime.now());
            result = dirRepository.addDir(newDir, userInfo);
        }
        return result;
    }

    @Override
    public ResultContainer<DirModel> getParentDir(Long id, UserModel userInfo) {
        ResultContainer<DirModel> result = dirRepository.getParentDir(id, userInfo);
        if (result.getContent() == null) {
            logger.info("result is null!");
            DirModel newDir = new DirModel(null, null, userInfo.getId(), userInfo.getUsername(), LocalDateTime.now());
            result = dirRepository.addDir(newDir, userInfo);
        }
        return result;
    }

    @Override
    public ResultContainer<DirModel> updateDir(Long id, DirPut request, UserModel userInfo) {
        ResultContainer<DirModel> parentDirModel = dirRepository.getParentDir(id, userInfo);
        ResultContainer<DirModel> result = null;

        if (parentDirModel.getStatus() != ResultStatus.OK) {
            result = parentDirModel;
        }
        else if (parentDirModel.getContent().getUserId() != userInfo.getId()) {
            result = new ResultContainer<DirModel>(ResultStatus.PermissionDeniedToOtherUsers, null);
        }
        else {
            try {
                result = dirRepository.updateDir(id, request, userInfo);
            } catch (Exception e) {
                logger.error("Error updating directory model: ", e);
                result = new ResultContainer<DirModel>(ResultStatus.WrongData);
            }
        }

        return result;
    }

    @Override
    public ResultStatus deleteDir(Long id, UserModel userInfo) {
        ResultContainer<DirModel> dirModel = dirRepository.getParentDir(id, userInfo);
        ResultStatus result = null;

        if (dirModel.getStatus() != ResultStatus.OK) {
            result = dirModel.getStatus();
        }
        else if (dirModel.getContent().getUserId() != userInfo.getId()) {
            result = ResultStatus.PermissionDeniedToOtherUsers;
        }
        else {
            result = dirRepository.deleteDir(id, userInfo);
        }

        return result;
    }

    private boolean hasUser(Long userId) {
        return true;
    }

    private boolean hasDir(Long id) {
        boolean result = dirRepository.hasDir(id);
        return result;
    }
}

