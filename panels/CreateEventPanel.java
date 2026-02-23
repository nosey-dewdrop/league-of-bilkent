import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;

public class CreateEventPanel extends JPanel {

    private HomeScreen home;
    private JTextField titleField, locationField, tagsField;
    private JTextArea descArea;
    private JSpinner dayS, monthS, yearS, hourS, minS;
    private JSpinner dayE, monthE, yearE, hourE, minE;
    private JSpinner deadlineDay, deadlineMonth, deadlineYear;
    private JSpinner capacitySpin, xpSpin;
    private JComboBox<String> tierCombo;
    private JLabel imageLabel;
    private String selectedImagePath = "";

    public CreateEventPanel(HomeScreen home) {
        this.home = home;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        buildUI();
    }

    private void buildUI() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(28, 56, 20, 56));

        // Title
        JLabel pageTitle = new JLabel("New Event");
        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        pageTitle.setForeground(AppConstants.TEXT_PRI);
        pageTitle.setAlignmentX(LEFT_ALIGNMENT);
        form.add(pageTitle);
        form.add(Box.createVerticalStrut(4));

        JLabel subTitle = new JLabel("Fill in the details below to create your event.");
        subTitle.setFont(AppConstants.F_SMALL);
        subTitle.setForeground(AppConstants.TEXT_SEC);
        subTitle.setAlignmentX(LEFT_ALIGNMENT);
        form.add(subTitle);
        form.add(Box.createVerticalStrut(24));

        // Event Title
        form.add(createFieldLabel("Event Title"));
        titleField = createCleanField("Give your event a name...");
        form.add(titleField);
        form.add(Box.createVerticalStrut(16));

        // Description
        form.add(createFieldLabel("Description"));
        descArea = new JTextArea(3, 30);
        descArea.setFont(AppConstants.F_NORMAL);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppConstants.BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setAlignmentX(LEFT_ALIGNMENT);
        descScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        form.add(descScroll);
        form.add(Box.createVerticalStrut(16));

        // Location
        form.add(createFieldLabel("Location"));
        locationField = createCleanField("Where is it happening?");
        form.add(locationField);
        form.add(Box.createVerticalStrut(16));

        // Poster image
        form.add(createFieldLabel("Poster Image (optional)"));
        JPanel imgRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        imgRow.setBackground(Color.WHITE);
        imgRow.setAlignmentX(LEFT_ALIGNMENT);
        imageLabel = new JLabel("Auto-generated if empty");
        imageLabel.setFont(AppConstants.F_TINY);
        imageLabel.setForeground(AppConstants.TEXT_LIGHT);
        JButton btnImg = UIHelper.createOutlineButton("Choose Image...", AppConstants.TEXT_SEC);
        btnImg.addActionListener(e -> chooseImage());
        imgRow.add(btnImg);
        imgRow.add(imageLabel);
        form.add(imgRow);
        form.add(Box.createVerticalStrut(20));

        // Separator
        JSeparator sep1 = new JSeparator();
        sep1.setForeground(AppConstants.BORDER);
        sep1.setAlignmentX(LEFT_ALIGNMENT);
        sep1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        form.add(sep1);
        form.add(Box.createVerticalStrut(16));

        // Date/Time section
        JLabel dateSec = new JLabel("Date & Time");
        dateSec.setFont(AppConstants.F_TITLE);
        dateSec.setForeground(AppConstants.TEXT_PRI);
        dateSec.setAlignmentX(LEFT_ALIGNMENT);
        form.add(dateSec);
        form.add(Box.createVerticalStrut(12));

        LocalDateTime now = LocalDateTime.now().plusDays(1);

        // Start
        form.add(createFieldLabel("Start"));
        JPanel startRow = createDateRow(now, true);
        dayS = (JSpinner) startRow.getClientProperty("day");
        monthS = (JSpinner) startRow.getClientProperty("month");
        yearS = (JSpinner) startRow.getClientProperty("year");
        hourS = (JSpinner) startRow.getClientProperty("hour");
        minS = (JSpinner) startRow.getClientProperty("min");
        form.add(startRow);
        form.add(Box.createVerticalStrut(10));

        // End
        form.add(createFieldLabel("End"));
        JPanel endRow = createDateRow(now, true);
        dayE = (JSpinner) endRow.getClientProperty("day");
        monthE = (JSpinner) endRow.getClientProperty("month");
        yearE = (JSpinner) endRow.getClientProperty("year");
        hourE = (JSpinner) endRow.getClientProperty("hour");
        minE = (JSpinner) endRow.getClientProperty("min");
        ((SpinnerNumberModel) hourE.getModel()).setValue(16);
        form.add(endRow);
        form.add(Box.createVerticalStrut(10));

        // Deadline
        form.add(createFieldLabel("Registration Deadline"));
        JPanel deadRow = createDateRow(now, false);
        deadlineDay = (JSpinner) deadRow.getClientProperty("day");
        deadlineMonth = (JSpinner) deadRow.getClientProperty("month");
        deadlineYear = (JSpinner) deadRow.getClientProperty("year");
        form.add(deadRow);
        form.add(Box.createVerticalStrut(20));

        // Separator
        JSeparator sep2 = new JSeparator();
        sep2.setForeground(AppConstants.BORDER);
        sep2.setAlignmentX(LEFT_ALIGNMENT);
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        form.add(sep2);
        form.add(Box.createVerticalStrut(16));

        // Settings section
        JLabel settingsSec = new JLabel("Settings");
        settingsSec.setFont(AppConstants.F_TITLE);
        settingsSec.setForeground(AppConstants.TEXT_PRI);
        settingsSec.setAlignmentX(LEFT_ALIGNMENT);
        form.add(settingsSec);
        form.add(Box.createVerticalStrut(12));

        // Two-column: Capacity + XP
        JPanel twoCol = new JPanel(new GridLayout(1, 2, 24, 0));
        twoCol.setBackground(Color.WHITE);
        twoCol.setAlignmentX(LEFT_ALIGNMENT);
        twoCol.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JPanel capPanel = new JPanel();
        capPanel.setLayout(new BoxLayout(capPanel, BoxLayout.Y_AXIS));
        capPanel.setOpaque(false);
        capPanel.add(createFieldLabel("Capacity"));
        capacitySpin = new JSpinner(new SpinnerNumberModel(
            AppConstants.DEFAULT_CAPACITY, AppConstants.MIN_CAPACITY, AppConstants.MAX_CAPACITY, AppConstants.CAPACITY_STEP));
        capacitySpin.setAlignmentX(LEFT_ALIGNMENT);
        capPanel.add(capacitySpin);
        twoCol.add(capPanel);

        JPanel xpPanel = new JPanel();
        xpPanel.setLayout(new BoxLayout(xpPanel, BoxLayout.Y_AXIS));
        xpPanel.setOpaque(false);
        xpPanel.add(createFieldLabel("XP Reward"));
        xpSpin = new JSpinner(new SpinnerNumberModel(
            AppConstants.DEFAULT_EVENT_XP, AppConstants.MIN_EVENT_XP, AppConstants.MAX_EVENT_XP, 5));
        xpSpin.setAlignmentX(LEFT_ALIGNMENT);
        xpPanel.add(xpSpin);
        twoCol.add(xpPanel);

        form.add(twoCol);
        form.add(Box.createVerticalStrut(12));

        // Tier
        form.add(createFieldLabel("Minimum Tier Required"));
        String[] tierOptions = new String[AppConstants.TIER_NAMES.length + 1];
        tierOptions[0] = "Anyone";
        for (int i = 0; i < AppConstants.TIER_NAMES.length; i++)
            tierOptions[i + 1] = AppConstants.TIER_NAMES[i] + " (" + AppConstants.TIER_THRESHOLDS[i] + "+ XP)";
        tierCombo = new JComboBox<>(tierOptions);
        tierCombo.setFont(AppConstants.F_NORMAL);
        tierCombo.setAlignmentX(LEFT_ALIGNMENT);
        tierCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        form.add(tierCombo);
        form.add(Box.createVerticalStrut(12));

        // Tags
        form.add(createFieldLabel("Tags (comma separated)"));
        tagsField = createCleanField("software, music, sports...");
        form.add(tagsField);
        form.add(Box.createVerticalStrut(24));

        // Submit button
        JButton btnCreate = UIHelper.createButton("Create Event", AppConstants.PRIMARY, Color.WHITE);
        btnCreate.setAlignmentX(LEFT_ALIGNMENT);
        btnCreate.setMaximumSize(new Dimension(200, 40));
        btnCreate.addActionListener(e -> handleCreate());
        form.add(btnCreate);
        form.add(Box.createVerticalStrut(20));

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createDateRow(LocalDateTime now, boolean withTime) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        row.setBackground(Color.WHITE);
        row.setAlignmentX(LEFT_ALIGNMENT);

        JSpinner day = new JSpinner(new SpinnerNumberModel(now.getDayOfMonth(), 1, 31, 1));
        JSpinner month = new JSpinner(new SpinnerNumberModel(now.getMonthValue(), 1, 12, 1));
        JSpinner year = new JSpinner(new SpinnerNumberModel(now.getYear(), 2025, 2030, 1));

        row.add(day); row.add(new JLabel("/"));
        row.add(month); row.add(new JLabel("/"));
        row.add(year);

        row.putClientProperty("day", day);
        row.putClientProperty("month", month);
        row.putClientProperty("year", year);

        if (withTime) {
            JSpinner hour = new JSpinner(new SpinnerNumberModel(14, 0, 23, 1));
            JSpinner min = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
            row.add(Box.createHorizontalStrut(12));
            row.add(new JLabel("Time:"));
            row.add(hour); row.add(new JLabel(":"));
            row.add(min);
            row.putClientProperty("hour", hour);
            row.putClientProperty("min", min);
        }

        return row;
    }

    private JTextField createCleanField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(AppConstants.F_NORMAL);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppConstants.BORDER, 1, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        f.setAlignmentX(LEFT_ALIGNMENT);
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        f.setForeground(AppConstants.TEXT_LIGHT);
        f.setText(placeholder);
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (f.getText().equals(placeholder)) {
                    f.setText("");
                    f.setForeground(AppConstants.TEXT_PRI);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (f.getText().isEmpty()) {
                    f.setText(placeholder);
                    f.setForeground(AppConstants.TEXT_LIGHT);
                }
            }
        });
        return f;
    }

    private String getFieldText(JTextField field, String placeholder) {
        String text = field.getText().trim();
        return text.equals(placeholder) ? "" : text;
    }

    private void chooseImage() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fc.getSelectedFile().getAbsolutePath();
            imageLabel.setText(fc.getSelectedFile().getName());
            imageLabel.setForeground(AppConstants.TEXT_PRI);
        }
    }

    private void handleCreate() {
        String t = getFieldText(titleField, "Give your event a name...");
        if (t.isEmpty()) { UIHelper.showError(this, "Event title is required!"); return; }
        String loc = getFieldText(locationField, "Where is it happening?");
        if (loc.isEmpty()) { UIHelper.showError(this, "Location is required!"); return; }

        try {
            LocalDateTime start = LocalDateTime.of((int) yearS.getValue(), (int) monthS.getValue(),
                (int) dayS.getValue(), (int) hourS.getValue(), (int) minS.getValue());
            LocalDateTime end = LocalDateTime.of((int) yearE.getValue(), (int) monthE.getValue(),
                (int) dayE.getValue(), (int) hourE.getValue(), (int) minE.getValue());
            LocalDateTime deadline = LocalDateTime.of((int) deadlineYear.getValue(), (int) deadlineMonth.getValue(),
                (int) deadlineDay.getValue(), 23, 59);

            Event ev = new Event(0, t, descArea.getText().trim(), loc, start, end, deadline,
                (int) capacitySpin.getValue(), MainFile.currentUser.getUsername());
            ev.setImagePath(selectedImagePath);
            ev.setXpReward((int) xpSpin.getValue());
            ev.setMinTierIndex(tierCombo.getSelectedIndex());

            String tags = getFieldText(tagsField, "software, music, sports...");
            if (!tags.isEmpty()) {
                for (String tag : tags.split(",")) {
                    String trimmed = tag.trim().toLowerCase();
                    if (!trimmed.isEmpty()) ev.addTag(trimmed);
                }
            }

            if (ev.getImagePath() == null || ev.getImagePath().isEmpty()) {
                String generated = PosterGenerator.generateDefault(ev);
                if (generated != null) ev.setImagePath(generated);
            }

            int id = Database.addToDatabase(ev);
            ev.setId(id);
            Database.addXP(MainFile.currentUser.getUsername(), AppConstants.XP_CREATE_EVENT);

            for (String follower : MainFile.currentUser.getFollowers()) {
                Database.addNotification(follower,
                    MainFile.currentUser.getDisplayName() + " created a new event: " + t);
            }

            UIHelper.showSuccess(this, "Event created!");
            home.showFeed();
        } catch (Exception ex) {
            UIHelper.showError(this, "Invalid date! Please check your input.");
        }
    }

    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppConstants.F_SMALL);
        l.setForeground(AppConstants.TEXT_SEC);
        l.setAlignmentX(LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
        return l;
    }
}
