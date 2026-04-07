package com.focusflow.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int duration;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sessionDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Distraction> distractions;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public Date getSessionDate() { return sessionDate; }
    public void setSessionDate(Date sessionDate) { this.sessionDate = sessionDate; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Distraction> getDistractions() { return distractions; }
    public void setDistractions(List<Distraction> distractions) { this.distractions = distractions; }
}
