package com.focusflow.model;

import javax.persistence.*;

@Entity
public class Distraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String type; // e.g. Mobile, Talking, Social Media
    private int timeLost;

    @ManyToOne
    @JoinColumn(name = "session_id")
    private StudySession session;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getTimeLost() { return timeLost; }
    public void setTimeLost(int timeLost) { this.timeLost = timeLost; }

    public StudySession getSession() { return session; }
    public void setSession(StudySession session) { this.session = session; }
}
