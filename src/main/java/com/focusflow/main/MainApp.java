package com.focusflow.main;

import com.focusflow.dao.UserDAO;
import com.focusflow.model.Distraction;
import com.focusflow.model.StudySession;
import com.focusflow.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        UserDAO dao = new UserDAO();
        Scanner sc = new Scanner(System.in);

        System.out.println("=========================================");
        System.out.println("  FocusFlow - Advanced Productivity App  ");
        System.out.println("=========================================");

        while (true) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add User");
            System.out.println("2. Add Session");
            System.out.println("3. View Users");
            System.out.println("4. Calculate Focus & View Reports");
            System.out.println("5. Delete User");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter User Name: ");
                    String name = sc.nextLine();
                    User newUser = new User();
                    newUser.setName(name);
                    dao.save(newUser);
                    System.out.println("User added successfully! (ID: " + newUser.getId() + ")");
                    break;
                case 2:
                    System.out.print("Enter User ID to attach session: ");
                    int uid = sc.nextInt();
                    sc.nextLine();

                    User existingUser = dao.getUserById(uid);
                    if (existingUser == null) {
                        System.out.println("User not found!");
                        break;
                    }

                    System.out.print("Enter Study Duration (mins): ");
                    int duration = sc.nextInt();
                    sc.nextLine();

                    StudySession session = new StudySession();
                    session.setDuration(duration);
                    session.setSessionDate(new Date()); // timestamp
                    session.setUser(existingUser);

                    List<Distraction> distractions = new ArrayList<>();
                    
                    System.out.print("Did you get distracted? (yes/no): ");
                    String hasDistr = sc.nextLine();
                    while (hasDistr.equalsIgnoreCase("yes")) {
                        Distraction d = new Distraction();
                        System.out.print("Enter Distraction Type (e.g. Mobile, Talking, Social Media): ");
                        d.setType(sc.nextLine());
                        System.out.print("Enter Time Lost (mins): ");
                        d.setTimeLost(sc.nextInt());
                        sc.nextLine();
                        d.setSession(session);
                        distractions.add(d);

                        System.out.print("Any other distractions? (yes/no): ");
                        hasDistr = sc.nextLine();
                    }

                    session.setDistractions(distractions);
                    dao.saveSession(session);
                    System.out.println("Session successfully recorded!");
                    break;

                case 3:
                    List<User> users = dao.getAllUsers();
                    System.out.println("\n--- ALL USERS ---");
                    for (User u : users) {
                        System.out.println("ID: " + u.getId() + " | Name: " + u.getName());
                    }
                    break;

                case 4:
                    System.out.print("Enter User ID for focus report: ");
                    int calcUid = sc.nextInt();
                    sc.nextLine();

                    List<StudySession> userSessions = dao.getSessionsByUser(calcUid);
                    if (userSessions == null || userSessions.isEmpty()) {
                        System.out.println("No sessions found for this user.");
                        break;
                    }

                    int totalStudy = 0;
                    int totalDistraction = 0;

                    for (StudySession s : userSessions) {
                        totalStudy += s.getDuration();
                        if (s.getDistractions() != null) {
                            for (Distraction d : s.getDistractions()) {
                                totalDistraction += d.getTimeLost();
                            }
                        }
                    }

                    double score = FocusService.calculateTotalFocus(userSessions);

                    System.out.println("\n--- PRODUCTIVITY REPORT ---");
                    System.out.println("Total Study: " + totalStudy + " mins");
                    System.out.println("Total Distraction: " + totalDistraction + " mins");
                    System.out.println("Focus Score: " + String.format("%.2f", score));

                    // Productivity Level Feature
                    System.out.print("Productivity Level: ");
                    if (score > 0.8) {
                        System.out.println("Highly Focused 🔥");
                    } else if (score > 0.5) {
                        System.out.println("Moderate Focus 👍");
                    } else {
                        System.out.println("Low Focus 🚨");
                    }

                    break;

                case 5:
                    System.out.print("Enter User ID to delete: ");
                    int delId = sc.nextInt();
                    sc.nextLine();
                    dao.deleteUser(delId);
                    System.out.println("User deleted successfully.");
                    break;

                case 6:
                    System.out.println("Exiting FocusFlow. Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
