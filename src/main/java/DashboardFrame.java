import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardFrame extends JFrame {

    private final User user;
    private final List<User> users;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    private JButton dashboardBtn;
    private JButton subjectBtn;
    private JButton studyPlanBtn;
    private JButton progressBtn;
    private JButton reminderBtn;
    private JButton settingsBtn;


    // =========================================================
    // COLORS
    // =========================================================

    private static final Color BG =
            new Color(246, 249, 255);

    private static final Color SIDEBAR =
            new Color(239, 246, 255);

    private static final Color ACTIVE =
            new Color(218, 228, 255);

    private static final Color CARD =
            Color.WHITE;

    private static final Color TEXT =
            new Color(35, 53, 91);

    private static final Color MUTED =
            new Color(118, 133, 163);

    private static final Color BORDER =
            new Color(220, 228, 242);

    private static final Color PURPLE =
            new Color(91, 78, 220);

    private static final Color PURPLE_LIGHT =
            new Color(241, 237, 255);

    private static final Color BLUE =
            new Color(75, 130, 220);

    private static final Color BLUE_LIGHT =
            new Color(232, 242, 255);

    private static final Color GREEN =
            new Color(70, 181, 132);

    private static final Color GREEN_LIGHT =
            new Color(229, 248, 239);

    private static final Color RED =
            new Color(239, 83, 99);

    private static final Color RED_LIGHT =
            new Color(255, 235, 239);


    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public DashboardFrame(
            User user,
            List<User> users
    ) {

        this.user = user;
        this.users = users;

        setTitle("Smart Study Planner");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(1180, 720);

        setMinimumSize(
                new Dimension(1050, 650)
        );

        setLocationRelativeTo(null);

        getContentPane().setBackground(BG);

        buildUI();

        setVisible(true);
    }


    // =========================================================
    // BUILD UI
    // =========================================================

    private void buildUI() {

        setLayout(
                new BorderLayout()
        );

        add(
                createSidebar(),
                BorderLayout.WEST
        );

        cardLayout =
                new CardLayout();

        contentPanel =
                new JPanel(cardLayout);

        contentPanel.setBackground(BG);


        contentPanel.add(
                createDashboard(),
                "HOME"
        );

        contentPanel.add(
                new SubjectTopicPanel(user, users),
                "SUBJECTS"
        );

        contentPanel.add(
                new StudyPlanPanel(user, users),
                "PLAN"
        );

        contentPanel.add(
                new ProgressPanel(user, users),
                "PROGRESS"
        );

        contentPanel.add(
                new ReminderPanel(user, users),
                "REMINDERS"
        );

        contentPanel.add(
                createSettings(),
                "SETTINGS"
        );


        add(
                contentPanel,
                BorderLayout.CENTER
        );

        showPage("HOME");
    }


    // =========================================================
    // SIDEBAR
    // =========================================================

    private JPanel createSidebar() {

        JPanel sidebar =
                new JPanel(
                        new BorderLayout()
                );

        sidebar.setPreferredSize(
                new Dimension(205, 0)
        );

        sidebar.setBackground(SIDEBAR);

        sidebar.setBorder(
                BorderFactory.createMatteBorder(
                        0,
                        0,
                        0,
                        1,
                        BORDER
                )
        );


        JPanel top =
                new JPanel();

        top.setBackground(SIDEBAR);

        top.setLayout(
                new BoxLayout(
                        top,
                        BoxLayout.Y_AXIS
                )
        );

        top.setBorder(
                new EmptyBorder(
                        18,
                        12,
                        10,
                        12
                )
        );


        // =====================================================
        // LOGO
        // =====================================================

        JPanel logo =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                5,
                                0
                        )
                );

        logo.setBackground(SIDEBAR);


        JLabel logoIcon =
                new JLabel("🎓");

        logoIcon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        20
                )
        );


        JLabel logoText =
                new JLabel(
                        "Smart Study Planner"
                );

        logoText.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        logoText.setForeground(TEXT);

        logo.add(logoIcon);
        logo.add(logoText);

        top.add(logo);

        top.add(
                Box.createVerticalStrut(18)
        );


        // =====================================================
        // USER CARD
        // =====================================================

        JPanel userPanel =
                new JPanel(
                        new BorderLayout(
                                8,
                                0
                        )
                );

        userPanel.setBackground(ACTIVE);

        userPanel.setBorder(
                new EmptyBorder(
                        9,
                        9,
                        9,
                        9
                )
        );


        JLabel userIcon =
                new JLabel("👤");

        userIcon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        17
                )
        );


        JPanel userText =
                new JPanel();

        userText.setBackground(ACTIVE);

        userText.setLayout(
                new BoxLayout(
                        userText,
                        BoxLayout.Y_AXIS
                )
        );


        String nameText =
                user.fullName == null
                        || user.fullName.trim().isEmpty()
                        ? user.username
                        : user.fullName;


        JLabel name =
                new JLabel(nameText);

        name.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        name.setForeground(TEXT);


        JLabel username =
                new JLabel(
                        "@" + user.username
                );

        username.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        username.setForeground(MUTED);


        userText.add(name);

        userText.add(
                Box.createVerticalStrut(2)
        );

        userText.add(username);


        userPanel.add(
                userIcon,
                BorderLayout.WEST
        );

        userPanel.add(
                userText,
                BorderLayout.CENTER
        );


        top.add(userPanel);

        top.add(
                Box.createVerticalStrut(17)
        );


        JLabel menu =
                new JLabel("MENU");

        menu.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        9
                )
        );

        menu.setForeground(MUTED);

        top.add(menu);

        top.add(
                Box.createVerticalStrut(6)
        );


        // =====================================================
        // MENU BUTTONS
        // =====================================================

        dashboardBtn =
                menuButton(
                        "▣",
                        "Dashboard"
                );

        subjectBtn =
                menuButton(
                        "▤",
                        "Subject & Topics"
                );

        studyPlanBtn =
                menuButton(
                        "☷",
                        "Study Plan"
                );

        progressBtn =
                menuButton(
                        "◔",
                        "Progress"
                );

        reminderBtn =
                menuButton(
                        "♢",
                        "Reminders"
                );

        settingsBtn =
                menuButton(
                        "⚙",
                        "Settings"
                );


        top.add(dashboardBtn);
        top.add(subjectBtn);
        top.add(studyPlanBtn);
        top.add(progressBtn);
        top.add(reminderBtn);
        top.add(settingsBtn);


        dashboardBtn.addActionListener(
                e -> showPage("HOME")
        );

        subjectBtn.addActionListener(
                e -> showPage("SUBJECTS")
        );

        studyPlanBtn.addActionListener(
                e -> showPage("PLAN")
        );

        progressBtn.addActionListener(
                e -> showPage("PROGRESS")
        );

        reminderBtn.addActionListener(
                e -> showPage("REMINDERS")
        );

        settingsBtn.addActionListener(
                e -> showPage("SETTINGS")
        );


        sidebar.add(
                top,
                BorderLayout.NORTH
        );


        // =====================================================
        // LOGOUT
        // =====================================================

        JPanel bottom =
                new JPanel();

        bottom.setBackground(SIDEBAR);

        bottom.setLayout(
                new BoxLayout(
                        bottom,
                        BoxLayout.Y_AXIS
                )
        );

        bottom.setBorder(
                new EmptyBorder(
                        8,
                        12,
                        12,
                        12
                )
        );


        JSeparator separator =
                new JSeparator();

        separator.setForeground(BORDER);

        bottom.add(separator);

        bottom.add(
                Box.createVerticalStrut(7)
        );


        JButton logout =
                menuButton(
                        "⇥",
                        "Logout"
                );


        logout.addActionListener(
                e -> logout()
        );


        bottom.add(logout);


        sidebar.add(
                bottom,
                BorderLayout.SOUTH
        );


        return sidebar;
    }


    // =========================================================
    // MENU BUTTON
    // =========================================================

    private JButton menuButton(
            String icon,
            String text
    ) {

        JButton button =
                new JButton(
                        icon + "   " + text
                );


        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        button.setForeground(TEXT);

        button.setBackground(SIDEBAR);

        button.setHorizontalAlignment(
                SwingConstants.LEFT
        );

        button.setFocusPainted(false);

        button.setBorder(
                new EmptyBorder(
                        9,
                        10,
                        9,
                        10
                )
        );


        button.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        37
                )
        );


        button.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        return button;
    }


    // =========================================================
    // DASHBOARD
    // =========================================================

    private JPanel createDashboard() {

        JPanel main =
                new JPanel(
                        new BorderLayout(
                                0,
                                12
                        )
                );

        main.setBackground(BG);

        main.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );


        // =====================================================
        // HEADER
        // =====================================================

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(BG);


        JPanel welcomePanel =
                new JPanel();

        welcomePanel.setBackground(BG);

        welcomePanel.setLayout(
                new BoxLayout(
                        welcomePanel,
                        BoxLayout.Y_AXIS
                )
        );


        String displayName =
                user.fullName == null
                        || user.fullName.trim().isEmpty()
                        ? user.username
                        : user.fullName;


        JLabel welcome =
                new JLabel(
                        "Welcome back, "
                                + displayName
                                + "! 👋"
                );


        welcome.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        21
                )
        );

        welcome.setForeground(TEXT);


        JLabel subtitle =
                new JLabel(
                        "Stay focused. You can do it!"
                );


        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        subtitle.setForeground(MUTED);


        welcomePanel.add(welcome);

        welcomePanel.add(
                Box.createVerticalStrut(2)
        );

        welcomePanel.add(subtitle);


        header.add(
                welcomePanel,
                BorderLayout.WEST
        );


        JLabel date =
                new JLabel(
                        new SimpleDateFormat(
                                "MMM dd, yyyy"
                        ).format(new Date())
                                + "  📅"
                );


        date.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        date.setForeground(MUTED);


        header.add(
                date,
                BorderLayout.EAST
        );


        main.add(
                header,
                BorderLayout.NORTH
        );


        // =====================================================
        // BODY
        // =====================================================

        JPanel body =
                new JPanel();

        body.setBackground(BG);

        body.setLayout(
                new BoxLayout(
                        body,
                        BoxLayout.Y_AXIS
                )
        );


        // =====================================================
        // STAT CARDS
        // =====================================================

        JPanel stats =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                10,
                                0
                        )
                );

        stats.setBackground(BG);

        stats.setPreferredSize(
                new Dimension(
                        0,
                        78
                )
        );

        stats.setMinimumSize(
                new Dimension(
                        0,
                        78
                )
        );

        stats.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        78
                )
        );


        stats.add(
                statCard(
                        "▥",
                        "Total Subjects",
                        String.valueOf(
                                getSubjects()
                        ),
                        BLUE,
                        BLUE_LIGHT
                )
        );


        stats.add(
                statCard(
                        "☷",
                        "Total Topics",
                        String.valueOf(
                                getTopics()
                        ),
                        PURPLE,
                        PURPLE_LIGHT
                )
        );


        stats.add(
                statCard(
                        "✓",
                        "Completed",
                        String.valueOf(
                                getCompleted()
                        ),
                        GREEN,
                        GREEN_LIGHT
                )
        );


        stats.add(
                statCard(
                        "○",
                        "Pending",
                        String.valueOf(
                                getPending()
                        ),
                        RED,
                        RED_LIGHT
                )
        );


        body.add(stats);

        body.add(
                Box.createVerticalStrut(10)
        );


        // =====================================================
        // MIDDLE
        // =====================================================

        JPanel middle =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                10,
                                0
                        )
                );

        middle.setBackground(BG);

        middle.setPreferredSize(
                new Dimension(
                        0,
                        225
                )
        );

        middle.setMinimumSize(
                new Dimension(
                        0,
                        225
                )
        );

        middle.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        225
                )
        );


        middle.add(
                createProgressCard()
        );

        middle.add(
                createDeadlineCard()
        );


        body.add(middle);

        body.add(
                Box.createVerticalStrut(10)
        );


        // =====================================================
        // NEW REMINDER
        // =====================================================

        JPanel reminder =
                createReminderCard();

        reminder.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        body.add(reminder);


        main.add(
                body,
                BorderLayout.CENTER
        );


        return main;
    }


    // =========================================================
    // STAT CARD
    // =========================================================

    private JPanel statCard(
            String icon,
            String title,
            String number,
            Color iconColor,
            Color iconBG
    ) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                9,
                                0
                        )
                );


        card.setBackground(CARD);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                10,
                                11,
                                10,
                                11
                        )
                )
        );


        JLabel iconLabel =
                new JLabel(icon);

        iconLabel.setOpaque(true);

        iconLabel.setBackground(iconBG);

        iconLabel.setForeground(iconColor);

        iconLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        iconLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        iconLabel.setPreferredSize(
                new Dimension(
                        34,
                        34
                )
        );


        JPanel text =
                new JPanel();

        text.setBackground(CARD);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel numberLabel =
                new JLabel(number);

        numberLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18
                )
        );

        numberLabel.setForeground(TEXT);


        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        titleLabel.setForeground(MUTED);


        text.add(numberLabel);

        text.add(
                Box.createVerticalStrut(1)
        );

        text.add(titleLabel);


        card.add(
                iconLabel,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );


        return card;
    }


    // =========================================================
    // PROGRESS CARD
    // =========================================================

    private JPanel createProgressCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );


        card.setBackground(CARD);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                13,
                                14,
                                10,
                                14
                        )
                )
        );


        JLabel title =
                new JLabel(
                        "Overall Progress"
                );


        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        title.setForeground(TEXT);


        card.add(
                title,
                BorderLayout.NORTH
        );


        JPanel center =
                new JPanel(
                        new GridBagLayout()
                );

        center.setBackground(CARD);


        center.add(
                new CircularProgress(
                        getPercentage()
                )
        );


        card.add(
                center,
                BorderLayout.CENTER
        );


        return card;
    }


    // =========================================================
    // CIRCULAR PROGRESS
    // =========================================================

    private static class CircularProgress
            extends JPanel {

        private final double percentage;


        public CircularProgress(
                double percentage
        ) {

            this.percentage =
                    Math.max(
                            0,
                            Math.min(
                                    100,
                                    percentage
                            )
                    );


            setOpaque(false);

            setPreferredSize(
                    new Dimension(
                            170,
                            170
                    )
            );
        }


        @Override
        protected void paintComponent(
                Graphics graphics
        ) {

            super.paintComponent(graphics);


            Graphics2D g =
                    (Graphics2D)
                            graphics.create();


            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            int diameter = 125;

            int x =
                    (getWidth() - diameter)
                            / 2;

            int y =
                    (getHeight() - diameter)
                            / 2;


            g.setStroke(
                    new BasicStroke(
                            10,
                            BasicStroke.CAP_ROUND,
                            BasicStroke.JOIN_ROUND
                    )
            );


            g.setColor(
                    new Color(
                            229,
                            234,
                            243
                    )
            );


            g.drawArc(
                    x,
                    y,
                    diameter,
                    diameter,
                    0,
                    360
            );


            int angle =
                    (int)
                            Math.round(
                                    percentage
                                            * 360
                                            / 100
                            );


            g.setColor(GREEN);


            if (angle > 0) {

                g.drawArc(
                        x,
                        y,
                        diameter,
                        diameter,
                        90,
                        -angle
                );
            }


            String percent =
                    String.format(
                            "%.0f%%",
                            percentage
                    );


            g.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            21
                    )
            );


            FontMetrics fm =
                    g.getFontMetrics();


            int px =
                    getWidth() / 2
                            - fm.stringWidth(
                            percent
                    ) / 2;


            int py =
                    getHeight() / 2
                            + 5;


            g.setColor(TEXT);


            g.drawString(
                    percent,
                    px,
                    py
            );


            String completed =
                    "Completed";


            g.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            9
                    )
            );


            FontMetrics fm2 =
                    g.getFontMetrics();


            int cx =
                    getWidth() / 2
                            - fm2.stringWidth(
                            completed
                    ) / 2;


            g.setColor(MUTED);


            g.drawString(
                    completed,
                    cx,
                    py + 16
            );


            g.dispose();
        }
    }


    // =========================================================
    // UPCOMING DEADLINES
    // =========================================================

    private JPanel createDeadlineCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                0,
                                8
                        )
                );


        card.setBackground(CARD);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                13,
                                14,
                                10,
                                14
                        )
                )
        );


        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(CARD);


        JLabel title =
                new JLabel(
                        "Upcoming Deadlines"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        12
                )
        );

        title.setForeground(TEXT);


        JLabel viewAll =
                new JLabel(
                        "View All"
                );

        viewAll.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        viewAll.setForeground(PURPLE);


        header.add(
                title,
                BorderLayout.WEST
        );

        header.add(
                viewAll,
                BorderLayout.EAST
        );


        card.add(
                header,
                BorderLayout.NORTH
        );


        JPanel list =
                new JPanel();

        list.setBackground(CARD);

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );


        List<Subject> upcoming =
                getUpcomingExamSubjects();


        if (upcoming.isEmpty()) {

            JLabel empty =
                    new JLabel(
                            "No upcoming deadlines."
                    );

            empty.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            10
                    )
            );

            empty.setForeground(MUTED);

            list.add(empty);

        } else {

            int count = 0;

            for (
                    Subject subject
                    : upcoming
            ) {

                list.add(
                        deadlineRow(
                                subject
                        )
                );

                count++;

                if (count >= 4) {
                    break;
                }
            }
        }


        card.add(
                list,
                BorderLayout.CENTER
        );


        return card;
    }


    // =========================================================
    // UPCOMING EXAMS
    // =========================================================

    private List<Subject> getUpcomingExamSubjects() {

        List<Subject> result =
                new ArrayList<>();


        if (user.subjects == null) {
            return result;
        }


        LocalDate today =
                LocalDate.now();


        for (
                Subject subject
                : user.subjects
        ) {

            if (
                    subject.examDate == null
                            ||
                            subject.examDate.trim().isEmpty()
            ) {

                continue;
            }


            LocalDate exam =
                    parseLocalDate(
                            subject.examDate
                    );


            if (
                    exam != null
                            &&
                            !exam.isBefore(today)
            ) {

                result.add(subject);
            }
        }


        result.sort(
                (a, b) -> {

                    LocalDate aDate =
                            parseLocalDate(
                                    a.examDate
                            );

                    LocalDate bDate =
                            parseLocalDate(
                                    b.examDate
                            );

                    return aDate.compareTo(
                            bDate
                    );
                }
        );


        return result;
    }


    // =========================================================
    // DEADLINE ROW
    // =========================================================

    private JPanel deadlineRow(
            Subject subject
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout()
                );


        row.setBackground(CARD);

        row.setBorder(
                new EmptyBorder(
                        5,
                        0,
                        5,
                        0
                )
        );


        JPanel left =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                7,
                                0
                        )
                );


        left.setBackground(CARD);


        JLabel dot =
                new JLabel("●");

        dot.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        dot.setForeground(RED);


        JLabel subjectName =
                new JLabel(
                        subject.name
                );

        subjectName.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        subjectName.setForeground(TEXT);


        left.add(dot);
        left.add(subjectName);


        JLabel date =
                new JLabel(
                        subject.examDate
                );

        date.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        date.setForeground(MUTED);


        row.add(
                left,
                BorderLayout.WEST
        );

        row.add(
                date,
                BorderLayout.EAST
        );


        return row;
    }


    // =========================================================
    // DASHBOARD REMINDER
    // =========================================================

    private JPanel createReminderCard() {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                12,
                                0
                        )
                );


        Color reminderBG =
                new Color(
                        244,
                        240,
                        255
                );


        card.setBackground(
                reminderBG
        );


        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        224,
                                        216,
                                        250
                                )
                        ),
                        new EmptyBorder(
                                8,
                                14,
                                8,
                                14
                        )
                )
        );


        card.setPreferredSize(
                new Dimension(
                        0,
                        62
                )
        );

        card.setMinimumSize(
                new Dimension(
                        0,
                        62
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        62
                )
        );


        // =====================================================
        // BELL
        // =====================================================

        JLabel bell =
                new JLabel("🔔");

        bell.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        18
                )
        );

        bell.setPreferredSize(
                new Dimension(
                        30,
                        30
                )
        );


        // =====================================================
        // TEXT
        // =====================================================

        JPanel text =
                new JPanel();

        text.setBackground(
                reminderBG
        );

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel title =
                new JLabel(
                        "Reminder"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        title.setForeground(
                new Color(
                        45,
                        48,
                        75
                )
        );


        JLabel message =
                new JLabel(
                        getReminderMessage()
                );

        message.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        message.setForeground(
                new Color(
                        85,
                        88,
                        110
                )
        );


        text.add(title);

        text.add(
                Box.createVerticalStrut(3)
        );

        text.add(message);


        // =====================================================
        // VIEW PLAN
        // =====================================================

        JButton viewPlan =
                new JButton(
                        "View Plan"
                );


        viewPlan.setForeground(
                Color.WHITE
        );

        viewPlan.setBackground(
                PURPLE
        );

        viewPlan.setOpaque(true);

        viewPlan.setContentAreaFilled(true);

        viewPlan.setFocusPainted(false);

        viewPlan.setBorderPainted(false);


        viewPlan.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );


        viewPlan.setPreferredSize(
                new Dimension(
                        90,
                        32
                )
        );


        viewPlan.setMinimumSize(
                new Dimension(
                        90,
                        32
                )
        );


        viewPlan.setMaximumSize(
                new Dimension(
                        90,
                        32
                )
        );


        viewPlan.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        viewPlan.addActionListener(
                e -> showPage("PLAN")
        );


        card.add(
                bell,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );

        card.add(
                viewPlan,
                BorderLayout.EAST
        );


        return card;
    }


    // =========================================================
    // REMINDER MESSAGE
    // =========================================================

    private String getReminderMessage() {

        if (
                user.subjects == null
                        ||
                        user.subjects.isEmpty()
        ) {

            return "No class or exam reminders yet.";
        }


        LocalDate today =
                LocalDate.now();


        Subject nearestSubject = null;

        LocalDate nearestDate = null;

        String eventType = "";


        // =====================================================
        // FIND NEAREST CLASS / EXAM
        // =====================================================

        for (
                Subject subject
                : user.subjects
        ) {

            // -----------------------------
            // CLASS
            // -----------------------------

            LocalDate classDate =
                    parseLocalDate(
                            subject.classDate
                    );


            if (
                    classDate != null
                            &&
                            !classDate.isBefore(today)
            ) {

                if (
                        nearestDate == null
                                ||
                                classDate.isBefore(
                                        nearestDate
                                )
                ) {

                    nearestDate =
                            classDate;

                    nearestSubject =
                            subject;

                    eventType =
                            "class";
                }
            }


            // -----------------------------
            // EXAM
            // -----------------------------

            LocalDate examDate =
                    parseLocalDate(
                            subject.examDate
                    );


            if (
                    examDate != null
                            &&
                            !examDate.isBefore(today)
            ) {

                if (
                        nearestDate == null
                                ||
                                examDate.isBefore(
                                        nearestDate
                                )
                ) {

                    nearestDate =
                            examDate;

                    nearestSubject =
                            subject;

                    eventType =
                            "exam";
                }
            }
        }


        // =====================================================
        // NO DATE
        // =====================================================

        if (
                nearestSubject == null
                        ||
                        nearestDate == null
        ) {

            int pending =
                    getPending();


            if (pending > 0) {

                return pending
                        + " pending topic"
                        + (
                        pending > 1
                                ? "s"
                                : ""
                )
                        + ". Start studying today!";
            }


            return "No upcoming class or exam reminders.";
        }


        // =====================================================
        // DAYS LEFT
        // =====================================================

        long days =
                ChronoUnit.DAYS.between(
                        today,
                        nearestDate
                );


        String eventMessage;


        if (days == 0) {

            eventMessage =
                    nearestSubject.name
                            + " "
                            + eventType
                            + " is today.";

        } else if (days == 1) {

            eventMessage =
                    nearestSubject.name
                            + " "
                            + eventType
                            + " is tomorrow.";

        } else {

            eventMessage =
                    nearestSubject.name
                            + " "
                            + eventType
                            + " is in "
                            + days
                            + " days.";
        }


        // =====================================================
        // PENDING TOPICS FOR THAT SUBJECT
        // =====================================================

        int pending =
                getPendingTopics(
                        nearestSubject
                );


        if (pending > 0) {

            return eventMessage
                    + " "
                    + pending
                    + " topic"
                    + (
                    pending > 1
                            ? "s"
                            : ""
            )
                    + " pending.";
        }


        return eventMessage
                + " All topics completed ✓";
    }


    // =========================================================
    // PENDING TOPICS FOR SUBJECT
    // =========================================================

    private int getPendingTopics(
            Subject subject
    ) {

        if (
                subject == null
                        ||
                        subject.topics == null
        ) {

            return 0;
        }


        int count = 0;


        for (
                Topic topic
                : subject.topics
        ) {

            if (
                    topic != null
                            &&
                            !topic.completed
            ) {

                count++;
            }
        }


        return count;
    }


    // =========================================================
    // PARSE LOCAL DATE
    // =========================================================

    private LocalDate parseLocalDate(
            String value
    ) {

        if (
                value == null
                        ||
                        value.trim().isEmpty()
        ) {

            return null;
        }


        try {

            return LocalDate.parse(
                    value.trim(),
                    DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd"
                    )
            );

        } catch (Exception e) {

            return null;
        }
    }


    // =========================================================
    // SETTINGS
    // =========================================================

    private JPanel createSettings() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(BG);

        panel.setBorder(
                new EmptyBorder(
                        18,
                        18,
                        18,
                        18
                )
        );


        JLabel title =
                new JLabel(
                        "Settings"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        20
                )
        );

        title.setForeground(TEXT);


        panel.add(
                title,
                BorderLayout.NORTH
        );


        JPanel card =
                new JPanel();

        card.setBackground(CARD);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel heading =
                new JLabel(
                        "Account Information"
                );

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        heading.setForeground(TEXT);


        card.add(heading);

        card.add(
                Box.createVerticalStrut(15)
        );


        addSetting(
                card,
                "Name",
                user.fullName
        );


        addSetting(
                card,
                "Username",
                user.username
        );


        addSetting(
                card,
                "Email",
                user.email
        );


        panel.add(
                card,
                BorderLayout.CENTER
        );


        return panel;
    }


    // =========================================================
    // SETTING ROW
    // =========================================================

    private void addSetting(
            JPanel parent,
            String title,
            String value
    ) {

        JLabel label =
                new JLabel(
                        title
                                + ": "
                                + value
                );


        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );


        label.setForeground(MUTED);


        parent.add(label);

        parent.add(
                Box.createVerticalStrut(10)
        );
    }


    // =========================================================
    // SHOW PAGE
    // =========================================================

    private void showPage(
            String page
    ) {

        cardLayout.show(
                contentPanel,
                page
        );


        resetMenu();


        switch (page) {

            case "HOME":

                active(
                        dashboardBtn
                );

                break;


            case "SUBJECTS":

                active(
                        subjectBtn
                );

                break;


            case "PLAN":

                active(
                        studyPlanBtn
                );

                break;


            case "PROGRESS":

                active(
                        progressBtn
                );

                break;


            case "REMINDERS":

                active(
                        reminderBtn
                );

                break;


            case "SETTINGS":

                active(
                        settingsBtn
                );

                break;
        }
    }


    // =========================================================
    // RESET MENU
    // =========================================================

    private void resetMenu() {

        JButton[] buttons = {

                dashboardBtn,
                subjectBtn,
                studyPlanBtn,
                progressBtn,
                reminderBtn,
                settingsBtn
        };


        for (
                JButton button
                : buttons
        ) {

            if (button != null) {

                button.setBackground(
                        SIDEBAR
                );

                button.setForeground(
                        TEXT
                );
            }
        }
    }


    // =========================================================
    // ACTIVE MENU
    // =========================================================

    private void active(
            JButton button
    ) {

        button.setBackground(
                ACTIVE
        );

        button.setForeground(
                PURPLE
        );
    }


    // =========================================================
    // LOGOUT
    // =========================================================

    private void logout() {

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "Logout",
                        JOptionPane.YES_NO_OPTION
                );


        if (
                result
                        == JOptionPane.YES_OPTION
        ) {

            dispose();

            new LoginFrame();
        }
    }


    // =========================================================
    // TOTAL SUBJECTS
    // =========================================================

    private int getSubjects() {

        if (user.subjects == null) {
            return 0;
        }

        return user.subjects.size();
    }


    // =========================================================
    // TOTAL TOPICS
    // =========================================================

    private int getTopics() {

        if (user.subjects == null) {
            return 0;
        }


        int total = 0;


        for (
                Subject subject
                : user.subjects
        ) {

            if (subject.topics != null) {

                total +=
                        subject.topics.size();
            }
        }


        return total;
    }


    // =========================================================
    // COMPLETED
    // =========================================================

    private int getCompleted() {

        if (user.subjects == null) {
            return 0;
        }


        int completed = 0;


        for (
                Subject subject
                : user.subjects
        ) {

            if (subject.topics == null) {
                continue;
            }


            for (
                    Topic topic
                    : subject.topics
            ) {

                if (
                        topic != null
                                &&
                                topic.completed
                ) {

                    completed++;
                }
            }
        }


        return completed;
    }


    // =========================================================
    // PENDING
    // =========================================================

    private int getPending() {

        return getTopics()
                - getCompleted();
    }


    // =========================================================
    // PERCENTAGE
    // =========================================================

    private double getPercentage() {

        int total =
                getTopics();

        int completed =
                getCompleted();


        if (total == 0) {
            return 0;
        }


        return completed
                * 100.0
                / total;
    }
}