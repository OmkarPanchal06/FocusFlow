package com.focusflow.main;

import com.focusflow.dao.UserDAO;
import com.focusflow.model.Distraction;
import com.focusflow.model.StudySession;
import com.focusflow.model.User;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WebBackendApp {

    public static class FocusRequest {
        public String name;
        public int studyDuration;
        public int distractionTime;

        public FocusRequest() {}
    }

    public static void main(String[] args) {

        UserDAO dao = new UserDAO();

        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
            
            // Enable CORS so Github Pages can communicate with your local backend!
            config.plugins.enableCors(cors -> {
                cors.add(it -> it.anyHost());
            });
        }).start(7070);

        app.post("/api/focus", ctx -> {
            try {
                FocusRequest req = ctx.bodyAsClass(FocusRequest.class);

                // Find or create user
                User user = new User();
                user.setName(req.name);
                dao.save(user);

                // Create Session
                StudySession session = new StudySession();
                session.setDuration(req.studyDuration);
                session.setSessionDate(new Date());
                session.setUser(user);

                // Create Distractions if any
                List<Distraction> distractions = new ArrayList<>();
                if (req.distractionTime > 0) {
                    Distraction d = new Distraction();
                    d.setTimeLost(req.distractionTime);
                    d.setType("General Web Distraction");
                    d.setSession(session);
                    distractions.add(d);
                }
                session.setDistractions(distractions);

                // Save everything
                dao.saveSession(session);

                // Calculate Score
                List<StudySession> tempSessions = new ArrayList<>();
                tempSessions.add(session);
                double score = FocusService.calculateTotalFocus(tempSessions);

                ctx.json(Map.of("score", score));

            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Internal Server Error");
            }
        });

        System.out.println("==================================================");
        System.out.println(" Web Backend is RUNNING on http://localhost:7070 ");
        System.out.println("==================================================");
    }
}
