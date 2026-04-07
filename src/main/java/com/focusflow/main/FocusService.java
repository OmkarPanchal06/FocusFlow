package com.focusflow.main;

import com.focusflow.model.StudySession;
import com.focusflow.model.Distraction;
import java.util.List;

public class FocusService {

    public static double calculateTotalFocus(List<StudySession> sessions) {
        if (sessions == null || sessions.isEmpty()) return 0.0;

        int totalStudy = 0;
        int totalDistraction = 0;

        for (StudySession s : sessions) {
            totalStudy += s.getDuration();

            if (s.getDistractions() != null) {
                for (Distraction d : s.getDistractions()) {
                    totalDistraction += d.getTimeLost();
                }
            }
        }

        if (totalStudy == 0) return 0.0;

        return (double) totalStudy / (totalStudy + totalDistraction);
    }

    public static int calculateSessionXP(StudySession session) {
        if (session == null) return 0;
        int studyTime = session.getDuration();
        int distractionTime = 0;
        
        if (session.getDistractions() != null) {
            for (Distraction d : session.getDistractions()) {
                distractionTime += d.getTimeLost();
            }
        }
        
        // 10 XP per study minute, -20 XP per distraction minute
        int xpGained = (studyTime * 10) - (distractionTime * 20);
        return Math.max(xpGained, 0);
    }

    public static void updateLevelBasedOnXP(com.focusflow.model.User user) {
        int level = (user.getXp() / 100) + 1; // 100 XP per level
        user.setLevel(level);
    }
}
