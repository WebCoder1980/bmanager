package com.bmanager.notebooks.repository;

import com.bmanager.notebooks.dto.NotebookPut;
import com.bmanager.notebooks.model.NotebookModel;
import com.bmanager.notebooks.model.ResultContainer;
import com.bmanager.notebooks.model.UserModel;
import com.bmanager.notebooks.util.ResultStatus;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class NotebookRepository {
    private SessionFactory sessionFactory;
    private Logger logger;

    @Autowired
    public NotebookRepository(SessionFactory sessionFactory, Logger logger) {
        this.sessionFactory = sessionFactory;
        this.logger = logger;
    }

    public ResultContainer<List<NotebookModel>> getNotebooks(Long parentDirId, UserModel userInfo) {
        Session session = null;
        ResultContainer<List<NotebookModel>> result = new ResultContainer<>(ResultStatus.OK, new LinkedList<>());

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String stringQuery = "FROM NotebookModel WHERE parentDirId = :parentDirId";
            Query<NotebookModel> query = session.createQuery(stringQuery, NotebookModel.class);
            query.setParameter("parentDirId", parentDirId);

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

    public ResultContainer<NotebookModel> getNotebook(Long id, UserModel userInfo) {
        Session session = null;
        ResultContainer<NotebookModel> result = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String stringQuery = "FROM NotebookModel WHERE id = :id";
            Query<NotebookModel> query = session.createQuery(stringQuery, NotebookModel.class);
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

    public ResultContainer<NotebookModel> addNotebook(NotebookModel request, UserModel userInfo) {
        ResultContainer<NotebookModel> result = new ResultContainer<>(ResultStatus.CREATED, request);
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

    public ResultContainer<NotebookModel> updateNotebook(Long id, NotebookPut request, UserModel userInfo) {
        Session session = null;
        ResultContainer<NotebookModel> result = new ResultContainer<>(ResultStatus.UPDATED, null);

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            result.setContent(session.get(NotebookModel.class, id));

            if (request.getName() != null) {
                result.getContent().setName(request.getName());
            }
            if (request.getParentDirId() != null) {
                result.getContent().setParentDirId(request.getParentDirId());
            }
            if (request.getContent() != null) {
                result.getContent().setContent(request.getContent());
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

    public ResultStatus deleteNotebook(Long id, UserModel userInfo) {
        ResultStatus resultStatus = ResultStatus.DELETED;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.remove(session.get(NotebookModel.class, id));

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

        String stringQuery = "SELECT COUNT(s) FROM NotebookModel s WHERE s.id = :id";
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
