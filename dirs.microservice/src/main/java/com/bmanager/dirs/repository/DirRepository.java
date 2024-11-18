package com.bmanager.dirs.repository;

import com.bmanager.dirs.dto.DirPost;
import com.bmanager.dirs.dto.DirPut;
import com.bmanager.dirs.exception.NoValidData;
import com.bmanager.dirs.model.DirModel;
import com.bmanager.dirs.model.ResultContainer;
import com.bmanager.dirs.model.UserModel;
import com.bmanager.dirs.util.ResultStatus;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DirRepository {
    private SessionFactory sessionFactory;
    private Logger logger;

    @Autowired
    public DirRepository(SessionFactory sessionFactory, Logger logger) {
        this.sessionFactory = sessionFactory;
        this.logger = logger;
    }

    public ResultContainer<List<DirModel>> getDirs(Long parentDirId, UserModel userInfo) {
        Session session = null;
        ResultContainer<List<DirModel>> result = new ResultContainer<>(ResultStatus.OK, new LinkedList<>());

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String stringQuery = "FROM DirModel WHERE parentDirId = :parentDirId AND userId = :userId";
            Query<DirModel> query = session.createQuery(stringQuery, DirModel.class);
            query.setParameter("parentDirId", parentDirId);
            query.setParameter("userId", userInfo.getId());

            result.getContent().addAll(query.getResultList());

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = new ResultContainer<>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public ResultContainer<DirModel> getDir(Long id, UserModel userInfo) {
        Session session = null;
        ResultContainer<DirModel> result = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String stringQuery = "FROM DirModel WHERE id = :id";
            Query<DirModel> query = session.createQuery(stringQuery, DirModel.class);
            query.setParameter("id", id);

            result = new ResultContainer<>(ResultStatus.OK, query.getSingleResult());

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = new ResultContainer<>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public ResultContainer<DirModel> getRootDir(UserModel userInfo) {
        Session session = null;
        ResultContainer<DirModel> result = new ResultContainer<>(ResultStatus.OK, null);

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String stringQuery = "FROM DirModel WHERE userId = :userId AND parentDirId IS NULL";
            Query<DirModel> query = session.createQuery(stringQuery, DirModel.class);
            query.setParameter("userId", userInfo.getId());

            List<DirModel> queryResult = query.getResultList();

            if(queryResult.size() != 0) {
                result.setContent(query.getResultList().get(0));
            }
            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = new ResultContainer<>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public ResultContainer<DirModel> getParentDir(Long id, UserModel userInfo) {
        Session session = null;
        ResultContainer<DirModel> result = null;

        ResultContainer<DirModel> currentDirModel = getDir(id, userInfo);

        if (currentDirModel.getStatus() != ResultStatus.OK) {
            result = currentDirModel;
        }
        else {
            try {
                session = sessionFactory.openSession();
                session.beginTransaction();

                String stringQuery = "FROM DirModel WHERE id = :id";
                Query<DirModel> query = session.createQuery(stringQuery, DirModel.class);
                query.setParameter("id", currentDirModel.getContent().getParentDirId());

                List<DirModel> queryResult = query.getResultList();

                if(queryResult.size() != 0) {
                    result = new ResultContainer<>(ResultStatus.OK, query.getResultList().get(0));
                }
                else {
                    result = new ResultContainer<>(ResultStatus.NotFound, null);
                }
                session.getTransaction().commit();

            } catch (Exception e) {
                if (session != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                logger.error(e);
                result = new ResultContainer<>(ResultStatus.InternalError, null);

            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }

        return result;
    }

    public ResultContainer<DirModel> addDir(DirModel request, UserModel userInfo) {
        ResultContainer<DirModel> result = new ResultContainer<>(ResultStatus.CREATED, request);
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.persist(result.getContent());

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = new ResultContainer<>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    public ResultContainer<DirModel> updateDir(Long id, DirPut request, UserModel userInfo) {
        Session session = null;
        ResultContainer<DirModel> result = new ResultContainer<>(ResultStatus.UPDATED, null);

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            result.setContent(session.get(DirModel.class, id));

            if (request.getName() != null) {
                result.getContent().setName(request.getName());
            }
            if (request.getParentDirId() != null) {
                result.getContent().setParentDirId(request.getParentDirId());
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = new ResultContainer<>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public ResultStatus deleteDir(Long id, UserModel userInfo) {
        ResultStatus resultStatus = ResultStatus.DELETED;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.remove(session.get(DirModel.class, id));

            session.getTransaction().commit();

        } catch(Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return resultStatus;
    }

    public boolean hasDir(Long id) {
        boolean result = false;

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String stringQuery = "SELECT COUNT(s) FROM DirModel s WHERE s.id = :id";
        Query<Long> query = session.createQuery(stringQuery, Long.class);
        query.setParameter("id", id);
        Long count = query.uniqueResult();

        session.getTransaction().commit();
        session.close();

        if (count != 0) {
            result = true;
        }
        return result;
    }
}
