package screens;

import model.*;
import model.Event;
import panels.*;
import tools.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class LoginScreen extends JFrame {

    private JTextField loginUsername;
    private JPasswordField loginPassword;
    private ArrayList<User> users;
    private JPanel formArea;
    private CardLayout formCards;

    public LoginScreen() {
        setTitle("\uD83D\uDC3F Squirrel");
        setSize(420, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        refreshUsers();
        buildUI();
    }

    public void refreshUsers() {
        users = Database.getAllUsers();
    }

    private void buildUI() {
        JPanel bg = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0xF3, 0xEE, 0xFF), 0, getHeight(), new Color(0xFF, 0xF0, 0xF5));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(0xC5, 0x6C, 0xF0, 18));
                g2.fillOval(-50, -50, 160, 160);
                g2.fillOval(getWidth() - 70, getHeight() - 90, 140, 140);
                g2.setColor(new Color(0xFF, 0x9A, 0xA2, 14));
                g2.fillOval(getWidth() - 30, -30, 100, 100);
                Random r = new Random(42);
                g2.setColor(new Color(0xC5, 0x6C, 0xF0, 25));
                for (int i = 0; i < 10; i++)
                    g2.fillOval(r.nextInt(getWidth()), r.nextInt(getHeight()), 2 + r.nextInt(3), 2 + r.nextInt(3));
            }
        };

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(30, 44, 20, 44));
        card.setPreferredSize(new Dimension(360, 500));

        JLabel emoji = new JLabel("\uD83D\uDC3F\uFE0F");
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 26));
        emoji.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(emoji);
        card.add(Box.createVerticalStrut(2));

        JLabel title = new JLabel("Squirrel");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(AppConstants.PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        JLabel sub = new JLabel("where campus comes alive \u2728");
        sub.setFont(AppConstants.F_SMALL);
        sub.setForeground(AppConstants.TEXT_SEC);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(sub);
        card.add(Box.createVerticalStrut(20));

        // Switch buttons instead of tabs
        JPanel switchRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        switchRow.setOpaque(false);
        JButton btnToLogin = makeSwitch("Login", true);
        JButton btnToReg = makeSwitch("Register", false);
        switchRow.add(btnToLogin);
        switchRow.add(btnToReg);
        switchRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        switchRow.setMaximumSize(new Dimension(200, 28));
        card.add(switchRow);
        card.add(Box.createVerticalStrut(14));

        formCards = new CardLayout();
        formArea = new JPanel(formCards);
        formArea.setOpaque(false);
        formArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        formArea.add(createLoginForm(), "login");
        JScrollPane regScroll = new JScrollPane(new RegisterScreen(this)); regScroll.setBorder(null); regScroll.setOpaque(false); regScroll.getViewport().setOpaque(false); formArea.add(regScroll, "register");
        card.add(formArea);

        card.add(Box.createVerticalGlue());
        JLabel made = new JLabel("made by nosey-dewdrop \uD83E\uDD8B");
        made.setFont(AppConstants.F_TINY);
        made.setForeground(AppConstants.TEXT_LIGHT);
        made.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(Box.createVerticalStrut(8));
        card.add(made);

        btnToLogin.addActionListener(e -> {
            formCards.show(formArea, "login");
            btnToLogin.setForeground(AppConstants.ACCENT);
            btnToReg.setForeground(AppConstants.TEXT_LIGHT);
        });
        btnToReg.addActionListener(e -> {
            formCards.show(formArea, "register");
            btnToReg.setForeground(AppConstants.ACCENT);
            btnToLogin.setForeground(AppConstants.TEXT_LIGHT);
        });

        bg.add(card);
        setContentPane(bg);
    }

    private JButton makeSwitch(String text, boolean active) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(active ? AppConstants.ACCENT : AppConstants.TEXT_LIGHT);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private JPanel createLoginForm() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);

        p.add(lbl("Username"));
        p.add(Box.createVerticalStrut(3));
        loginUsername = UIHelper.createStyledField();
        loginUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        loginUsername.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(loginUsername);
        p.add(Box.createVerticalStrut(10));

        p.add(lbl("Password"));
        p.add(Box.createVerticalStrut(3));
        loginPassword = UIHelper.createStyledPasswordField();
        loginPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        loginPassword.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(loginPassword);
        p.add(Box.createVerticalStrut(16));

        JButton btnLogin = new JButton("Log in") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(0xC5, 0x6C, 0xF0), getWidth(), 0, new Color(0xFF, 0x9A, 0xA2));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        btnLogin.setPreferredSize(new Dimension(0, 30));
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(btnLogin);
        p.add(Box.createVerticalStrut(6));

        JButton btnForgot = new JButton("Forgot Password?");
        btnForgot.setFont(AppConstants.F_TINY);
        btnForgot.setForeground(AppConstants.ACCENT);
        btnForgot.setBorderPainted(false);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnForgot.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(btnForgot);

        btnLogin.addActionListener(e -> handleLogin());
        loginPassword.addActionListener(e -> handleLogin());
        btnForgot.addActionListener(e -> new ForgotPasswordDialog(this).setVisible(true));
        return p;
    }

    private JLabel lbl(String t) {
        JLabel l = new JLabel(t);
        l.setFont(AppConstants.F_SMALL);
        l.setForeground(AppConstants.TEXT_SEC);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private void handleLogin() {
        String username = loginUsername.getText().trim().toLowerCase();
        String password = new String(loginPassword.getPassword());
        if (username.isEmpty() || password.isEmpty()) { UIHelper.showError(this, "Please enter username and password!"); return; }
        refreshUsers();
        User found = null;
        for (User u : users) { if (u.getUsername().equals(username)) { found = u; break; } }
        if (found == null) { UIHelper.showError(this, "User not found!"); return; }
        boolean match;
        if (found.getSalt() != null && !found.getSalt().isEmpty()) {
            match = PasswordUtil.hashPassword(password, found.getSalt()).equals(found.getPassword());
        } else { match = password.equals(found.getPassword()); }
        if (!match) { UIHelper.showError(this, "Wrong password!"); return; }
        if (!found.isVerified()) { UIHelper.showError(this, "Account not verified."); return; }
        MainFile.currentUser = found;
        new HomeScreen().setVisible(true);
        setVisible(false);
    }
}
