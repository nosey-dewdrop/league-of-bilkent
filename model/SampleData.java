package model;

import tools.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

/*
 * ┌──────────────────────────────────────────────────────────────┐
 * │                  <<class>> SampleData                        │
 * ├──────────────────────────────────────────────────────────────┤
 * │ + loadSampleData() (static) -> populates DB with demo data  │
 * │   Creates: 5 users (3 students + 2 clubs), 6 events,        │
 * │            follows, attendance, comments, messages, notifs   │
 * ├──────────────────────────────────────────────────────────────┤
 * │ USES:    User, ClubUser, Event, Comment, Database,           │
 * │          AttendanceStatus, PasswordUtil, PosterGenerator      │
 * │ USED BY: MainFile (if database is empty on startup)          │
 * └──────────────────────────────────────────────────────────────┘
 */
public class SampleData {

    public static void loadSampleData() {
        String s1 = PasswordUtil.generateSalt();
        String s2 = PasswordUtil.generateSalt();
        String s3 = PasswordUtil.generateSalt();
        String s4 = PasswordUtil.generateSalt();
        String s5 = PasswordUtil.generateSalt();
        String s6 = PasswordUtil.generateSalt();
        String s7 = PasswordUtil.generateSalt();

        // Users
        User damla = new User("damla", "Damla", "damla@ug.bilkent.edu.tr",
            PasswordUtil.hashPassword("1234", s1), s1, "CS Student | Event Organizer");
        damla.setVerified(true); damla.setXp(120);
        damla.getInterests().add("software"); damla.getInterests().add("ai");
        damla.getInterests().add("music"); damla.getInterests().add("philosophy");

        User eylul = new User("eylul", "Eylül", "eylul@ug.bilkent.edu.tr",
            PasswordUtil.hashPassword("1234", s2), s2, "Art & Photography Lover");
        eylul.setVerified(true); eylul.setXp(80);
        eylul.getInterests().add("art"); eylul.getInterests().add("photography");
        eylul.getInterests().add("cinema");

        User emir = new User("emir_selim", "Emir Selim", "emirselim@ug.bilkent.edu.tr",
            PasswordUtil.hashPassword("1234", s3), s3, "Runner & Sports Enthusiast");
        emir.setVerified(true); emir.setXp(95);
        emir.getInterests().add("sports"); emir.getInterests().add("fitness");
        emir.getInterests().add("basketball");

        User ege = new User("ege", "Ege", "ege@ug.bilkent.edu.tr",
            PasswordUtil.hashPassword("1234", s4), s4, "Music Producer & GameDev");
        ege.setVerified(true); ege.setXp(60);
        ege.getInterests().add("music"); ege.getInterests().add("gamedev");
        ege.getInterests().add("software");

        User bosman = new User("bosman", "Bosman", "bosman@ug.bilkent.edu.tr",
            PasswordUtil.hashPassword("1234", s5), s5, "Entrepreneur & Finance Geek");
        bosman.setVerified(true); bosman.setXp(45);
        bosman.getInterests().add("entrepreneurship"); bosman.getInterests().add("finance");
        bosman.getInterests().add("marketing");

        ClubUser ieee = new ClubUser("ieee_bilkent", "IEEE Bilkent", "ieee@bilkent.edu.tr",
            PasswordUtil.hashPassword("1234", s6), s6, "IEEE Bilkent Student Branch");
        ieee.setVerified(true); ieee.setXp(200);
        ieee.getInterests().add("software"); ieee.getInterests().add("ai");

        ClubUser music = new ClubUser("music_club", "Music Club", "music@bilkent.edu.tr",
            PasswordUtil.hashPassword("1234", s7), s7, "Bilkent Music & Performance Club");
        music.setVerified(true); music.setXp(150);
        music.getInterests().add("music");

        Database.addToDatabase(damla);
        Database.addToDatabase(eylul);
        Database.addToDatabase(emir);
        Database.addToDatabase(ege);
        Database.addToDatabase(bosman);
        Database.addToDatabase(ieee);
        Database.addToDatabase(music);

        Database.addXP("damla", 120);
        Database.addXP("eylul", 80);
        Database.addXP("emir_selim", 95);
        Database.addXP("ege", 60);
        Database.addXP("bosman", 45);
        Database.addXP("ieee_bilkent", 200);
        Database.addXP("music_club", 150);

        // Follows
        Database.addFollow("damla", "ieee_bilkent");
        Database.addFollow("damla", "music_club");
        Database.addFollow("eylul", "damla");
        Database.addFollow("eylul", "music_club");
        Database.addFollow("emir_selim", "damla");
        Database.addFollow("emir_selim", "ege");
        Database.addFollow("ege", "ieee_bilkent");
        Database.addFollow("ege", "damla");
        Database.addFollow("bosman", "ieee_bilkent");
        Database.addFollow("bosman", "emir_selim");

        // Events
        LocalDateTime now = LocalDateTime.now();

        Event e1 = new Event(0, "GameDev Summit", "Learn about game development with Unity and Unreal Engine.",
            "EA-409", now.plusDays(18), now.plusDays(18).plusHours(3), now.plusDays(17), 80, "ieee_bilkent");
        e1.addTag("gamedev"); e1.addTag("software"); e1.setXpReward(15); e1.setMinTierIndex(1);
        int id1 = Database.addToDatabase(e1);
        String p1 = PosterGenerator.generateDefault(e1); if(p1!=null) Database.updateEventImage(id1,p1);

        Event e2 = new Event(0, "Algorithm Contest", "Competitive programming challenge for all levels.",
            "B-Lab", now.plusDays(24), now.plusDays(24).plusHours(4), now.plusDays(23), 40, "ieee_bilkent");
        e2.addTag("algorithms"); e2.addTag("software"); e2.setXpReward(10);
        int id2 = Database.addToDatabase(e2);
        String p2 = PosterGenerator.generateDefault(e2); if(p2!=null) Database.updateEventImage(id2,p2);

        Event e3 = new Event(0, "Acoustic Night", "Student musicians perform live at Odeon.",
            "Odeon", now.plusDays(30), now.plusDays(30).plusHours(3), now.plusDays(29), 100, "music_club");
        e3.addTag("music"); e3.addTag("concert"); e3.setXpReward(5);
        int id3 = Database.addToDatabase(e3);
        String p3 = PosterGenerator.generateDefault(e3); if(p3!=null) Database.updateEventImage(id3,p3);

        Event e4 = new Event(0, "Python Workshop", "Beginner-friendly Python programming workshop.",
            "EA-Z03", now.plusDays(5), now.plusDays(5).plusHours(2), now.plusDays(4), 50, "damla");
        e4.addTag("python"); e4.addTag("education"); e4.setXpReward(10);
        int id4 = Database.addToDatabase(e4);
        String p4 = PosterGenerator.generateDefault(e4); if(p4!=null) Database.updateEventImage(id4,p4);

        Event e5 = new Event(0, "Campus Run", "5km campus run - all fitness levels welcome!",
            "Main Gate", now.plusDays(7), now.plusDays(7).plusHours(2), now.plusDays(6), 200, "emir_selim");
        e5.addTag("sports"); e5.addTag("fitness"); e5.setXpReward(5);
        int id5 = Database.addToDatabase(e5);
        String p5 = PosterGenerator.generateDefault(e5); if(p5!=null) Database.updateEventImage(id5,p5);

        Event e6 = new Event(0, "Art Exhibition", "Student artwork showcase at the library.",
            "Library Foyer", now.plusDays(40), now.plusDays(41), now.plusDays(39), 150, "eylul");
        e6.addTag("art"); e6.addTag("exhibition"); e6.setXpReward(5);
        int id6 = Database.addToDatabase(e6);
        String p6 = PosterGenerator.generateDefault(e6); if(p6!=null) Database.updateEventImage(id6,p6);

        // Attendance
        Database.setAttendance(id1, "damla", AttendanceStatus.GOING);
        Database.setAttendance(id1, "ege", AttendanceStatus.GOING);
        Database.setAttendance(id1, "emir_selim", AttendanceStatus.INTERESTED);
        Database.setAttendance(id2, "damla", AttendanceStatus.GOING);
        Database.setAttendance(id2, "bosman", AttendanceStatus.INTERESTED);
        Database.setAttendance(id3, "eylul", AttendanceStatus.GOING);
        Database.setAttendance(id3, "ege", AttendanceStatus.GOING);
        Database.setAttendance(id3, "damla", AttendanceStatus.GOING);
        Database.setAttendance(id4, "emir_selim", AttendanceStatus.GOING);
        Database.setAttendance(id4, "eylul", AttendanceStatus.GOING);
        Database.setAttendance(id4, "ege", AttendanceStatus.GOING);
        Database.setAttendance(id5, "damla", AttendanceStatus.GOING);
        Database.setAttendance(id5, "bosman", AttendanceStatus.MAYBE);
        Database.setAttendance(id6, "damla", AttendanceStatus.INTERESTED);
        Database.setAttendance(id6, "ege", AttendanceStatus.GOING);

        // Comments
        Comment c1 = new Comment(0, "emir_selim", "Sounds awesome! Will there be prizes?", "14/02 10:30", 0);
        int cid1 = Database.addToDatabase(c1, id1);
        Comment c2 = new Comment(0, "damla", "Yes! Top 3 get awards.", "14/02 11:00", cid1);
        Database.addToDatabase(c2, id1);
        Comment c3 = new Comment(0, "eylul", "Can't wait for this!", "14/02 12:00", 0);
        Database.addToDatabase(c3, id3);
        Comment c4 = new Comment(0, "bosman", "Is this beginner friendly?", "14/02 14:00", 0);
        Database.addToDatabase(c4, id4);

        // Messages
        Database.sendMessage("emir_selim", "damla", "Hey! Are you coming to the campus run?");
        Database.sendMessage("damla", "emir_selim", "Definitely! What time should we meet?");
        Database.sendMessage("ege", "eylul", "Are you performing at acoustic night?");
        Database.sendMessage("eylul", "ege", "Yes! Playing two songs :)");

        // Notifications
        Database.addNotification("damla", "eylul started following you!");
        Database.addNotification("damla", "emir_selim started following you!");
        Database.addNotification("ege", "emir_selim started following you!");
        Database.addNotification("emir_selim", "bosman started following you!");

        System.out.println("Sample data loaded!");
    }
}
