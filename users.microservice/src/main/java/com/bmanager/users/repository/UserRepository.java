package com.bmanager.users.repository;

import com.bmanager.users.client.DirClient;
import com.bmanager.users.dto.DirPost;
import com.bmanager.users.dto.UserPost;
import com.bmanager.users.dto.UserPut;
import com.bmanager.users.model.ResultContainer;
import com.bmanager.users.model.UserModel;
import com.bmanager.users.util.ResultStatus;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserRepository {
    private final SessionFactory sessionFactory;
    private final DirClient dirsClient;
    private final Logger logger;

    @Autowired
    public UserRepository(SessionFactory sessionFactory, DirClient dirsClient, Logger logger) {
        this.sessionFactory = sessionFactory;
        this.dirsClient = dirsClient;
        this.logger = logger;
    }

    public ResultContainer<List<UserModel>> getUsers(UserModel userInfo) {
        Session session = null;
        ResultContainer<List<UserModel>> result = new ResultContainer<List<UserModel>>(ResultStatus.OK, new LinkedList<UserModel>());

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String stringQuery = "FROM UserModel";
            Query<UserModel> query = session.createQuery(stringQuery, UserModel.class);

            result.getContent().addAll(query.getResultList());

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }

            logger.error(e);
            result = new ResultContainer<List<UserModel>>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public ResultContainer<UserModel> getUser(Long id, UserModel userInfo) {
        Session session = null;
        ResultContainer<UserModel> result = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            String stringQuery = "FROM UserModel WHERE id = :id";
            Query<UserModel> query = session.createQuery(stringQuery, UserModel.class);
            query.setParameter("id", id);

            result = new ResultContainer<UserModel>(ResultStatus.OK, query.getSingleResult());

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = new ResultContainer<UserModel>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public ResultContainer<UserModel> addUser(UserPost request, UserModel userInfo) {
        ResultContainer<UserModel> result = new ResultContainer<UserModel>(ResultStatus.CREATED, new UserModel(request));
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.persist(result.getContent());

            session.getTransaction().commit();

            DirPost newDir = new DirPost();
            newDir.setName(result.getContent().getUsername());
            newDir.setUserId(result.getContent().getId());
            dirsClient.addDir(newDir);
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = new ResultContainer<UserModel>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }
        return result;
    }

    public ResultContainer<UserModel> updateUser(Long id, UserPut request, UserModel userInfo) {
        Session session = null;
        ResultContainer<UserModel> result = new ResultContainer<UserModel>(ResultStatus.UPDATED, null);

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            result.setContent(session.get(UserModel.class, id));

            if (request.getEmail() != null) {
                result.getContent().setEmail(request.getEmail());
            }

            if (request.getPassword() != null) {
                result.getContent().setPassword(request.getPassword());
            }

            if (request.getUsername() != null) {
                result.getContent().setUsername(request.getUsername());
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = new ResultContainer<UserModel>(ResultStatus.InternalError, null);

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public ResultStatus deleteUser(Long id, UserModel userInfo) {
        ResultStatus result = ResultStatus.DELETED;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();

            session.remove(session.get(UserModel.class, id));

            session.getTransaction().commit();

        } catch(Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            logger.error(e);
            result = ResultStatus.InternalError;

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return result;
    }

    public boolean hasName(String name) {
        boolean result = false;

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String stringQuery = "SELECT COUNT(s) FROM UserModel s WHERE s.username = :username";
        Query query = session.createQuery(stringQuery, Long.class);
        query.setParameter("username", name);
        Long count = (Long) query.getSingleResult();

        session.getTransaction().commit();
        session.close();

        if (count != 0) {
            result = true;
        }
        return result;
    }
}
