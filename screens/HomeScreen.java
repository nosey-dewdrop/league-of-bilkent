package screens;

import model.*;
import model.Event;
import panels.*;
import tools.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HomeScreen extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private ProfilePanel myProfilePanel;
    private ProfilePanel viewedProfilePanel;
    private java.util.Stack<User> profileStack = new java.util.Stack<>();
    private JTextField topSearchField;
    private JLabel notifBadge;

    public HomeScreen() {
        setTitle("Squirrel - " + MainFile.currentUser.getDisplayName());
        setSize(AppConstants.WINDOW_WIDTH, AppConstants.WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
        cardLayout.show(contentPanel, "feed");
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, AppConstants.BORDER),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));

        // Left: notification bell
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(160, 38));

        JButton notifBtn = new JButton("\uD83D\uDD14");
        notifBtn.setFont(new Font("SansSerif", Font.PLAIN, 18));
        notifBtn.setBorderPainted(false);
        notifBtn.setContentAreaFilled(false);
        notifBtn.setFocusPainted(false);
        notifBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        notifBtn.setToolTipText("Notifications");
        notifBtn.addActionListener(e -> showPanel(new NotificationsPanel(this), "notif"));

        // Badge count
        int unreadCount = Database.getNotifications(MainFile.currentUser.getUsername()).size();
        notifBadge = new JLabel(String.valueOf(unreadCount));
        notifBadge.setFont(new Font("SansSerif", Font.BOLD, 9));
        notifBadge.setForeground(Color.WHITE);
        notifBadge.setOpaque(true);
        notifBadge.setBackground(AppConstants.DANGER);
        notifBadge.setBorder(BorderFactory.createEmptyBorder(1, 5, 1, 5));
        notifBadge.setVisible(unreadCount > 0);

        left.add(notifBtn);
        left.add(notifBadge);
        bar.add(left, BorderLayout.WEST);

        // Center: search bar
        topSearchField = UIHelper.createPlaceholderField("Search events, users...");
        topSearchField.setPreferredSize(new Dimension(360, 34));
        topSearchField.addActionListener(e -> {
            String q = UIHelper.getFieldText(topSearchField, "Search events, users...");
            if (!q.isEmpty()) {
                SearchPanel sp = new SearchPanel(this);
                sp.searchFor(q);
                showPanel(sp, "search");
            }
        });

        JPanel centerWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centerWrap.setOpaque(false);
        centerWrap.add(topSearchField);
        bar.add(centerWrap, BorderLayout.CENTER);

        // Right: calendar + profile
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.setPreferredSize(new Dimension(160, 38));
        JButton topCal = makeTopButton("Calendar");
        topCal.addActionListener(e -> showPanel(new CalendarPanel(this), "calendar"));
        JButton topProfile = makeTopButton("Profile");
        topProfile.addActionListener(e -> showMyProfile());
        right.add(topCal);
        right.add(topProfile);
        bar.add(right, BorderLayout.EAST);

        return bar;
    }

    private JPanel buildSidebar() {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setPreferredSize(new Dimension(AppConstants.NAV_WIDTH, 0));
        nav.setBackground(AppConstants.BG_NAV);
        nav.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, AppConstants.BORDER));

        nav.add(Box.createVerticalStrut(18));
        JLabel logo = new JLabel("Squirrel");
        logo.setFont(new Font("SansSerif", Font.BOLD, 15));
        logo.setForeground(AppConstants.TEXT_PRI);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        nav.add(logo);

        JLabel badge = new JLabel(MainFile.currentUser.getProfileBadge());
        badge.setFont(AppConstants.F_TINY);
        badge.setForeground(AppConstants.TEXT_SEC);
        badge.setAlignmentX(Component.CENTER_ALIGNMENT);
        nav.add(badge);
        nav.add(Box.createVerticalStrut(16));

        JSeparator sep = new JSeparator();
        sep.setForeground(AppConstants.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        nav.add(sep);
        nav.add(Box.createVerticalStrut(10));

        JButton btnFeed   = createNavButton("\uD83E\uDD8B  Feed");
        JButton btnDisc   = createNavButton("\u2728  Discover");
        JButton btnBoard  = createNavButton("\uD83C\uDF38  Leaderboard");
        JButton btnCreate = createNavButton("\uD83E\uDEBB  New Event");
        JButton btnMsg    = createNavButton("\uD83D\uDC9C  Messages");
        JButton btnNotif  = createNavButton("\uD83D\uDD2E  Notifications");
        JButton btnLogout = createNavButton("\uD83C\uDF19  Log out");
        btnLogout.setForeground(AppConstants.DANGER);

        for (JButton b : new JButton[]{btnFeed, btnDisc, btnBoard, btnCreate, btnMsg, btnNotif}) {
            nav.add(b);
            nav.add(Box.createVerticalStrut(2));
        }
        nav.add(Box.createVerticalGlue());
        nav.add(btnLogout);
        nav.add(Box.createVerticalStrut(14));

        btnFeed.addActionListener(e -> showFeed());
        btnDisc.addActionListener(e -> showPanel(new DiscoverPanel(this), "discover"));
        btnBoard.addActionListener(e -> showPanel(new LeaderboardPanel(this), "leaderboard"));
        btnCreate.addActionListener(e -> showPanel(new CreateEventPanel(this), "create"));
        btnMsg.addActionListener(e -> showPanel(new MessagingPanel(this), "messages"));
        btnNotif.addActionListener(e -> showPanel(new NotificationsPanel(this), "notif"));
        btnLogout.addActionListener(e -> {
            dispose();
            MainFile.currentUser = null;
            MainFile.loginScreen.setVisible(true);
        });

        return nav;
    }

    private JPanel buildContent() {
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(new FeedPanel(this), "feed");
        contentPanel.add(new SearchPanel(this), "search");
        contentPanel.add(new DiscoverPanel(this), "discover");
        contentPanel.add(new CalendarPanel(this), "calendar");
        contentPanel.add(new LeaderboardPanel(this), "leaderboard");
        contentPanel.add(new CreateEventPanel(this), "create");
        contentPanel.add(new MessagingPanel(this), "messages");
        contentPanel.add(new NotificationsPanel(this), "notif");
        return contentPanel;
    }

    private void showPanel(JPanel panel, String name) {
        contentPanel.add(panel, name);
        cardLayout.show(contentPanel, name);
    }

    public void showFeed() {
        showPanel(new FeedPanel(this), "feed");
    }

    public void showMyProfile() {
        myProfilePanel = new ProfilePanel(MainFile.currentUser, this, false);
        showPanel(myProfilePanel, "myProfile");
    }

    public void navigateToProfile(User user) {
        if (user.getUsername().equals(MainFile.currentUser.getUsername())) { showMyProfile(); return; }
        profileStack.push(user);
        viewedProfilePanel = new ProfilePanel(user, this, true);
        showPanel(viewedProfilePanel, "viewProfile");
    }

    public void goBackFromProfile() {
        if (!profileStack.isEmpty()) profileStack.pop();
        if (!profileStack.isEmpty()) {
            User prev = profileStack.peek();
            viewedProfilePanel = new ProfilePanel(prev, this, true);
            showPanel(viewedProfilePanel, "viewProfile");
        } else {
            showFeed();
        }
    }

    public void showEventDetail(Event event) {
        showPanel(new EventDetailPanel(event, this), "detail");
    }

    public void changeAttendance(Event event, AttendanceStatus status) {
        String me = MainFile.currentUser.getUsername();
        if (status == null) {
            event.removeAttendance(me);
            Database.removeAttendance(event.getId(), me);
        } else {
            if (status == AttendanceStatus.GOING && event.isFull()) {
                UIHelper.showError(this, "Event is full!"); return;
            }
            event.setAttendance(me, status);
            Database.setAttendance(event.getId(), me, status);
            Database.addXP(me, AppConstants.XP_ATTEND_EVENT);
            if (event.getXpReward() > 0) Database.addXP(me, event.getXpReward());
        }
    }

    public void addComment(Event event, String text, int parentId) {
        String me = MainFile.currentUser.getUsername();
        String time = java.time.LocalDateTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM HH:mm"));
        Comment comment = new Comment(0, me, text, time, parentId);
        int cid = Database.addToDatabase(comment, event.getId());
        comment = new Comment(cid, me, text, time, parentId);
        event.addComment(comment);
        Database.addXP(me, AppConstants.XP_COMMENT);

        if (!me.equals(event.getCreatorUsername())) {
            Database.addNotification(event.getCreatorUsername(),
                MainFile.currentUser.getDisplayName() + " commented on: " + event.getTitle());
        }
        if (parentId > 0) {
            for (Comment c : event.getComments()) {
                if (c.getId() == parentId && !c.getUsername().equals(me)) {
                    Database.addNotification(c.getUsername(),
                        MainFile.currentUser.getDisplayName() + " replied to your comment on: " + event.getTitle());
                    break;
                }
            }
        }
    }

    public void followUser(String target) {
        String me = MainFile.currentUser.getUsername();
        if (MainFile.currentUser.getFollowing().contains(target)) return;
        Database.addFollow(me, target);
        MainFile.currentUser.getFollowing().add(target);
        Database.addXP(target, AppConstants.XP_GAIN_FOLLOWER);
        Database.addNotification(target, MainFile.currentUser.getDisplayName() + " started following you!");
    }

    public void unfollowUser(String target) {
        String me = MainFile.currentUser.getUsername();
        Database.deleteFollow(me, target);
        MainFile.currentUser.getFollowing().remove(target);
        Database.addXP(target, -AppConstants.XP_GAIN_FOLLOWER);
    }

    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setFont(AppConstants.F_SMALL);
        b.setForeground(AppConstants.TEXT_PRI);
        b.setBackground(AppConstants.BG_NAV);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(200, 36));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(6, 20, 6, 20));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(AppConstants.PRIMARY_LIGHT); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(AppConstants.BG_NAV); }
        });
        return b;
    }

    private JButton makeTopButton(String text) {
        JButton b = new JButton(text);
        b.setFont(AppConstants.F_SMALL);
        b.setForeground(AppConstants.TEXT_SEC);
        b.setBackground(Color.WHITE);
        b.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppConstants.BORDER, 1, true),
            BorderFactory.createEmptyBorder(5, 14, 5, 14)));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(AppConstants.PRIMARY_LIGHT); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(Color.WHITE); }
        });
        return b;
    }
}
