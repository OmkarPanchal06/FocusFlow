package com.focusflow.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int xp = 0;
    private int level = 1;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StudySession> sessions;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public List<StudySession> getSessions() { return sessions; }
    public void setSessions(List<StudySession> sessions) { this.sessions = sessions; }
}
