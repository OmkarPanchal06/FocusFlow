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
}
