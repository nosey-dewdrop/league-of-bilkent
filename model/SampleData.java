package model;

import tools.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
        User damla = new User("damla", "Damla", "damla@email.com",
            PasswordUtil.hashPassword("1234", s1), s1, "Developer & community builder");
        damla.setVerified(true); damla.setXp(120);
        damla.getInterests().add("software"); damla.getInterests().add("ai");
        damla.getInterests().add("music"); damla.getInterests().add("philosophy");

        User ali = new User("ali_k", "Ali K.", "ali@email.com",
            PasswordUtil.hashPassword("1234", s2), s2, "Runner & fitness coach");
        ali.setVerified(true); ali.setXp(80);
        ali.getInterests().add("sports"); ali.getInterests().add("fitness");
        ali.getInterests().add("food");

        User elif = new User("elif_s", "Elif S.", "elif@email.com",
            PasswordUtil.hashPassword("1234", s3), s3, "Artist & photographer");
        elif.setVerified(true); elif.setXp(45);
        elif.getInterests().add("art"); elif.getInterests().add("photography");
        elif.getInterests().add("cinema");

        User can = new User("can_t", "Can T.", "can@email.com",
            PasswordUtil.hashPassword("1234", s4), s4, "Music producer & DJ");
        can.setVerified(true); can.setXp(95);
        can.getInterests().add("music"); can.getInterests().add("concert");
        can.getInterests().add("nightlife");

        ClubUser techHub = new ClubUser("techhub", "TechHub", "info@techhub.io",
            PasswordUtil.hashPassword("1234", s5), s5, "Community for developers & makers");
        techHub.setVerified(true); techHub.setXp(200);
        techHub.getInterests().add("software"); techHub.getInterests().add("ai");
        techHub.getInterests().add("gamedev");

        ClubUser artCollective = new ClubUser("art_collective", "Art Collective", "hello@artcollective.co",
            PasswordUtil.hashPassword("1234", s6), s6, "Creative community for artists");
        artCollective.setVerified(true); artCollective.setXp(150);
        artCollective.getInterests().add("art"); artCollective.getInterests().add("photography");
        artCollective.getInterests().add("cinema");

        ClubUser fitCrew = new ClubUser("fitcrew", "FitCrew", "hello@fitcrew.com",
            PasswordUtil.hashPassword("1234", s7), s7, "Fitness & wellness community");
        fitCrew.setVerified(true); fitCrew.setXp(130);
        fitCrew.getInterests().add("sports"); fitCrew.getInterests().add("fitness");

        Database.addToDatabase(damla);
        Database.addToDatabase(ali);
        Database.addToDatabase(elif);
        Database.addToDatabase(can);
        Database.addToDatabase(techHub);
        Database.addToDatabase(artCollective);
        Database.addToDatabase(fitCrew);

        Database.addXP("damla", 120);
        Database.addXP("ali_k", 80);
        Database.addXP("elif_s", 45);
        Database.addXP("can_t", 95);
        Database.addXP("techhub", 200);
        Database.addXP("art_collective", 150);
        Database.addXP("fitcrew", 130);

        // Follows
        Database.addFollow("damla", "techhub");
        Database.addFollow("damla", "art_collective");
        Database.addFollow("ali_k", "damla");
        Database.addFollow("ali_k", "fitcrew");
        Database.addFollow("elif_s", "damla");
        Database.addFollow("elif_s", "art_collective");
        Database.addFollow("can_t", "damla");
        Database.addFollow("can_t", "techhub");

        // Events
        LocalDateTime now = LocalDateTime.now();

        Event e1 = new Event(0, "GameDev Jam", "48-hour game development jam. Build something amazing!",
            "TechHub HQ", now.plusDays(12), now.plusDays(14), now.plusDays(11), 60, "techhub");
        e1.addTag("gamedev"); e1.addTag("software"); e1.setXpReward(20); e1.setMinTierIndex(1);
        int id1 = Database.addToDatabase(e1);
        String p1 = PosterGenerator.generateDefault(e1); if(p1!=null) Database.updateEventImage(id1,p1);

        Event e2 = new Event(0, "AI Workshop", "Hands-on intro to machine learning with Python.",
            "Online (Zoom)", now.plusDays(5), now.plusDays(5).plusHours(3), now.plusDays(4), 100, "techhub");
        e2.addTag("ai"); e2.addTag("python"); e2.addTag("education"); e2.setXpReward(15);
        int id2 = Database.addToDatabase(e2);
        String p2 = PosterGenerator.generateDefault(e2); if(p2!=null) Database.updateEventImage(id2,p2);

        Event e3 = new Event(0, "Acoustic Night", "Live acoustic performances by local artists.",
            "Vinyl Cafe", now.plusDays(8), now.plusDays(8).plusHours(3), now.plusDays(7), 80, "can_t");
        e3.addTag("music"); e3.addTag("concert"); e3.setXpReward(5);
        int id3 = Database.addToDatabase(e3);
        String p3 = PosterGenerator.generateDefault(e3); if(p3!=null) Database.updateEventImage(id3,p3);

        Event e4 = new Event(0, "City Run 10K", "10K run through the city center. All levels welcome!",
            "Central Park", now.plusDays(15), now.plusDays(15).plusHours(2), now.plusDays(14), 200, "fitcrew");
        e4.addTag("sports"); e4.addTag("fitness"); e4.setXpReward(10);
        int id4 = Database.addToDatabase(e4);
        String p4 = PosterGenerator.generateDefault(e4); if(p4!=null) Database.updateEventImage(id4,p4);

        Event e5 = new Event(0, "Street Photography Walk", "Explore the city through your lens.",
            "Old Town Square", now.plusDays(10), now.plusDays(10).plusHours(3), now.plusDays(9), 30, "art_collective");
        e5.addTag("photography"); e5.addTag("art"); e5.setXpReward(5);
        int id5 = Database.addToDatabase(e5);
        String p5 = PosterGenerator.generateDefault(e5); if(p5!=null) Database.updateEventImage(id5,p5);

        Event e6 = new Event(0, "Film Screening: Stalker", "Tarkovsky's masterpiece on the big screen.",
            "Indie Cinema", now.plusDays(20), now.plusDays(20).plusHours(3), now.plusDays(19), 60, "art_collective");
        e6.addTag("cinema"); e6.addTag("art"); e6.setXpReward(5);
        int id6 = Database.addToDatabase(e6);
        String p6 = PosterGenerator.generateDefault(e6); if(p6!=null) Database.updateEventImage(id6,p6);

        Event e7 = new Event(0, "Yoga in the Park", "Morning yoga session for beginners.",
            "Riverside Park", now.plusDays(3), now.plusDays(3).plusHours(1), now.plusDays(2), 40, "fitcrew");
        e7.addTag("fitness"); e7.addTag("wellness"); e7.setXpReward(5);
        int id7 = Database.addToDatabase(e7);
        String p7 = PosterGenerator.generateDefault(e7); if(p7!=null) Database.updateEventImage(id7,p7);

        Event e8 = new Event(0, "Open Mic Night", "Share your poetry, music, or comedy.",
            "The Basement Bar", now.plusDays(6), now.plusDays(6).plusHours(3), now.plusDays(5), 50, "can_t");
        e8.addTag("music"); e8.addTag("art"); e8.addTag("social"); e8.setXpReward(5);
        int id8 = Database.addToDatabase(e8);
        String p8 = PosterGenerator.generateDefault(e8); if(p8!=null) Database.updateEventImage(id8,p8);

        Event e9 = new Event(0, "Hackathon: Green Tech", "Build solutions for sustainability.",
            "Co-Work Space", now.plusDays(25), now.plusDays(26), now.plusDays(24), 80, "techhub");
        e9.addTag("software"); e9.addTag("environment"); e9.setXpReward(25); e9.setMinTierIndex(1);
        int id9 = Database.addToDatabase(e9);
        String p9 = PosterGenerator.generateDefault(e9); if(p9!=null) Database.updateEventImage(id9,p9);

        // Attendance
        Database.setAttendance(id1, "damla", AttendanceStatus.GOING);
        Database.setAttendance(id1, "can_t", AttendanceStatus.INTERESTED);
        Database.setAttendance(id2, "damla", AttendanceStatus.GOING);
        Database.setAttendance(id2, "elif_s", AttendanceStatus.INTERESTED);
        Database.setAttendance(id3, "damla", AttendanceStatus.GOING);
        Database.setAttendance(id3, "elif_s", AttendanceStatus.GOING);
        Database.setAttendance(id4, "ali_k", AttendanceStatus.GOING);
        Database.setAttendance(id4, "damla", AttendanceStatus.INTERESTED);
        Database.setAttendance(id5, "elif_s", AttendanceStatus.GOING);
        Database.setAttendance(id5, "damla", AttendanceStatus.MAYBE);
        Database.setAttendance(id7, "ali_k", AttendanceStatus.GOING);
        Database.setAttendance(id7, "elif_s", AttendanceStatus.GOING);
        Database.setAttendance(id8, "can_t", AttendanceStatus.GOING);
        Database.setAttendance(id8, "damla", AttendanceStatus.GOING);
        Database.setAttendance(id9, "damla", AttendanceStatus.GOING);

        // Comments
        Comment c1 = new Comment(0, "can_t", "This sounds epic! Will there be prizes?", "20/02 10:30", 0);
        int cid1 = Database.addToDatabase(c1, id1);
        Comment c2 = new Comment(0, "damla", "Yes! Top 3 teams get awards.", "20/02 11:00", cid1);
        Database.addToDatabase(c2, id1);
        Comment c3 = new Comment(0, "elif_s", "Can't wait for acoustic night!", "20/02 12:00", 0);
        Database.addToDatabase(c3, id3);
        Comment c4 = new Comment(0, "ali_k", "Who's joining the 10K? Let's go!", "21/02 09:00", 0);
        Database.addToDatabase(c4, id4);

        // Messages
        Database.sendMessage("ali_k", "damla", "Hey! Are you coming to the city run?");
        Database.sendMessage("damla", "ali_k", "Definitely! What time should we meet?");
        Database.sendMessage("can_t", "damla", "Want to collab on the open mic night?");
        Database.sendMessage("damla", "can_t", "Sure! I can do a short set.");

        // Notifications
        Database.addNotification("damla", "ali_k started following you!");
        Database.addNotification("damla", "elif_s started following you!");
        Database.addNotification("damla", "can_t started following you!");
        Database.addNotification("damla", "New event: AI Workshop by TechHub");
        Database.addNotification("ali_k", "FitCrew posted: City Run 10K");

        System.out.println("Sample data loaded!");
    }
}
