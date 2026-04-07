package com.focusflow.dao;

import com.focusflow.model.User;
import com.focusflow.model.StudySession;
import com.focusflow.model.Distraction;
import com.focusflow.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDAO {

    public void save(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(user);
        tx.commit();
        session.close();
    }

    public void saveSession(StudySession studySession) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(studySession);
        if (studySession.getDistractions() != null) {
            for (Distraction d : studySession.getDistractions()) {
                session.saveOrUpdate(d);
            }
        }
        tx.commit();
        session.close();
    }

    public List<User> getAllUsers() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<User> list = session.createQuery("from User", User.class).list();
        session.close();
        return list;
    }

    public User getUserById(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        User user = session.get(User.class, userId);
        session.close();
        return user;
    }

    public List<StudySession> getSessionsByUser(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<StudySession> query = session.createQuery("from StudySession where user.id = :uid", StudySession.class);
        query.setParameter("uid", userId);
        List<StudySession> sessions = query.list();
        session.close();
        return sessions;
    }

    public void deleteUser(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        User user = session.get(User.class, userId);
        if (user != null) {
            session.delete(user);
        }
        tx.commit();
        session.close();
    }
}
