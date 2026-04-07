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

    public List<Object[]> getTopDistractions(int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Object[]> query = session.createQuery(
            "SELECT d.type, SUM(d.timeLost) FROM Distraction d JOIN d.session s WHERE s.user.id = :uid GROUP BY d.type ORDER BY SUM(d.timeLost) DESC", 
            Object[].class
        );
        query.setParameter("uid", userId);
        List<Object[]> results = query.list();
        session.close();
        return results;
    }

    public List<User> getLeaderboard() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<User> query = session.createQuery("from User u order by u.xp desc", User.class);
        query.setMaxResults(5);
        List<User> list = query.list();
        session.close();
        return list;
    }
}
