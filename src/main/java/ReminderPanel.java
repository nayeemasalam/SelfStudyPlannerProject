import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ReminderPanel extends JPanel {

    private final User user;
    private final List<User> users;

    private static final Color BG =
            new Color(246, 248, 255);

    private static final Color CARD =
            Color.WHITE;

    private static final Color PRIMARY =
            new Color(91, 78, 220);

    private static final Color TEXT =
            new Color(40, 45, 70);

    private static final Color MUTED =
            new Color(125, 130, 150);

    private static final Color BORDER =
            new Color(224, 227, 240);

    private static final Color RED =
            new Color(235, 77, 89);

    private static final Color ORANGE =
            new Color(235, 167, 55);

    private static final Color GREEN =
            new Color(47, 171, 118);


    public ReminderPanel(
            User user,
            List<User> users
    ) {

        this.user = user;
        this.users = users;

        setBackground(BG);

        setLayout(
                new BorderLayout()
        );

        setBorder(
                new EmptyBorder(
                        5,
                        5,
                        5,
                        5
                )
        );

        createUI();
    }


    private void createUI() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(BG);


        JLabel title =
                new JLabel(
                        "Reminders"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        title.setForeground(TEXT);


        JLabel subtitle =
                new JLabel(
                        "Classes, exams and pending topics"
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        subtitle.setForeground(MUTED);


        JPanel heading =
                new JPanel();

        heading.setBackground(BG);

        heading.setLayout(
                new BoxLayout(
                        heading,
                        BoxLayout.Y_AXIS
                )
        );

        heading.add(title);

        heading.add(
                Box.createVerticalStrut(3)
        );

        heading.add(subtitle);


        header.add(
                heading,
                BorderLayout.WEST
        );


        add(
                header,
                BorderLayout.NORTH
        );


        JPanel list =
                new JPanel();

        list.setBackground(BG);

        list.setLayout(
                new BoxLayout(
                        list,
                        BoxLayout.Y_AXIS
                )
        );


        List<Subject> subjects =
                getSubjectsWithDates();


        if (subjects.isEmpty()) {

            JPanel empty =
                    createCard();

            JLabel label =
                    new JLabel(
                            "No class or exam reminders yet."
                    );

            label.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            11
                    )
            );

            label.setForeground(MUTED);

            empty.add(label);

            list.add(empty);

        } else {

            for (
                    Subject subject
                    : subjects
            ) {

                list.add(
                        createReminderCard(
                                subject
                        )
                );

                list.add(
                        Box.createVerticalStrut(10)
                );
            }
        }


        JScrollPane scroll =
                new JScrollPane(list);

        scroll.setBorder(null);

        scroll.setBackground(BG);

        scroll.getViewport()
                .setBackground(BG);

        scroll.getVerticalScrollBar()
                .setUnitIncrement(12);


        add(
                scroll,
                BorderLayout.CENTER
        );
    }


    private JPanel createReminderCard(
            Subject subject
    ) {

        JPanel card =
                createCard();

        card.setLayout(
                new BorderLayout(
                        15,
                        0
                )
        );


        JLabel bell =
                new JLabel("🔔");

        bell.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        23
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


        JLabel subjectTitle =
                new JLabel(
                        subject.name
                );

        subjectTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        subjectTitle.setForeground(TEXT);


        text.add(subjectTitle);

        text.add(
                Box.createVerticalStrut(6)
        );


        // =========================================
        // CLASS DATE
        // =========================================

        if (
                subject.classDate != null
                        &&
                        !subject.classDate
                                .trim()
                                .isEmpty()
        ) {

            JLabel classDate =
                    new JLabel(
                            "Class Date: "
                                    + subject.classDate
                    );

            classDate.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            10
                    )
            );

            classDate.setForeground(
                    PRIMARY
            );

            text.add(classDate);

            text.add(
                    Box.createVerticalStrut(4)
            );


            JLabel classMessage =
                    new JLabel(
                            getClassReminderMessage(
                                    subject
                            )
                    );

            classMessage.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            10
                    )
            );

            classMessage.setForeground(
                    PRIMARY
            );

            text.add(classMessage);

            text.add(
                    Box.createVerticalStrut(5)
            );
        }


        // =========================================
        // EXAM DATE
        // =========================================

        if (
                subject.examDate != null
                        &&
                        !subject.examDate
                                .trim()
                                .isEmpty()
        ) {

            JLabel examDate =
                    new JLabel(
                            "Exam Date: "
                                    + subject.examDate
                    );

            examDate.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            10
                    )
            );

            examDate.setForeground(
                    ORANGE
            );

            text.add(examDate);

            text.add(
                    Box.createVerticalStrut(4)
            );


            JLabel examMessage =
                    new JLabel(
                            getExamReminderMessage(
                                    subject
                            )
                    );

            examMessage.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            10
                    )
            );

            examMessage.setForeground(
                    ORANGE
            );

            text.add(examMessage);

            text.add(
                    Box.createVerticalStrut(5)
            );
        }


        // =========================================
        // INCOMPLETE TOPICS
        // =========================================

        int incomplete =
                getIncompleteTopicCount(
                        subject
                );


        if (incomplete > 0) {

            JLabel topics =
                    new JLabel(
                            "Pending Topics: "
                                    + incomplete
                    );

            topics.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            10
                    )
            );

            topics.setForeground(RED);

            text.add(topics);

            text.add(
                    Box.createVerticalStrut(3)
            );


            JLabel topicMessage =
                    new JLabel(
                            "Complete these topics before the upcoming date."
                    );

            topicMessage.setFont(
                    new Font(
                            "Segoe UI",
                            Font.PLAIN,
                            10
                    )
            );

            topicMessage.setForeground(MUTED);

            text.add(topicMessage);

        } else {

            JLabel complete =
                    new JLabel(
                            "All topics completed ✓"
                    );

            complete.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            10
                    )
            );

            complete.setForeground(GREEN);

            text.add(complete);
        }


        card.add(
                bell,
                BorderLayout.WEST
        );

        card.add(
                text,
                BorderLayout.CENTER
        );


        return card;
    }


    // =========================================
    // CLASS REMINDER
    // =========================================

    private String getClassReminderMessage(
            Subject subject
    ) {

        try {

            LocalDate classDate =
                    LocalDate.parse(
                            subject.classDate.trim(),
                            DateTimeFormatter.ofPattern(
                                    "yyyy-MM-dd"
                            )
                    );

            LocalDate today =
                    LocalDate.now();

            long days =
                    ChronoUnit.DAYS.between(
                            today,
                            classDate
                    );


            if (days < 0) {

                return "Class date has passed.";

            } else if (days == 0) {

                return "Class is today!";

            } else if (days == 1) {

                return "Class is tomorrow.";

            } else {

                return "Class is in "
                        + days
                        + " days.";
            }

        } catch (Exception e) {

            return "Check your class schedule.";
        }
    }


    // =========================================
    // EXAM REMINDER
    // =========================================

    private String getExamReminderMessage(
            Subject subject
    ) {

        try {

            LocalDate examDate =
                    LocalDate.parse(
                            subject.examDate.trim(),
                            DateTimeFormatter.ofPattern(
                                    "yyyy-MM-dd"
                            )
                    );

            LocalDate today =
                    LocalDate.now();

            long days =
                    ChronoUnit.DAYS.between(
                            today,
                            examDate
                    );


            if (days < 0) {

                return "Exam date has passed.";

            } else if (days == 0) {

                return "Exam is today!";

            } else if (days == 1) {

                return "Exam is tomorrow.";

            } else {

                return "Exam is in "
                        + days
                        + " days.";
            }

        } catch (Exception e) {

            return "Check your exam schedule.";
        }
    }


    // =========================================
    // INCOMPLETE TOPICS
    // =========================================

    private int getIncompleteTopicCount(
            Subject subject
    ) {

        if (subject.topics == null) {
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


    // =========================================
    // SUBJECT LIST
    // =========================================

    private List<Subject> getSubjectsWithDates() {

        List<Subject> result =
                new ArrayList<>();


        if (user.subjects == null) {
            return result;
        }


        for (
                Subject subject
                : user.subjects
        ) {

            boolean hasClassDate =
                    subject.classDate != null
                            &&
                            !subject.classDate
                                    .trim()
                                    .isEmpty();


            boolean hasExamDate =
                    subject.examDate != null
                            &&
                            !subject.examDate
                                    .trim()
                                    .isEmpty();


            if (
                    hasClassDate
                            ||
                            hasExamDate
            ) {

                result.add(subject);
            }
        }


        result.sort(
                Comparator.comparing(
                        this::getNearestDate,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                )
        );


        return result;
    }


    // =========================================
    // NEAREST DATE
    // =========================================

    private Date getNearestDate(
            Subject subject
    ) {

        Date classDate =
                parseDate(
                        subject.classDate
                );

        Date examDate =
                parseDate(
                        subject.examDate
                );


        if (classDate == null) {

            return examDate;
        }


        if (examDate == null) {

            return classDate;
        }


        if (
                classDate.before(examDate)
        ) {

            return classDate;

        } else {

            return examDate;
        }
    }


    // =========================================
    // DATE PARSER
    // =========================================

    private Date parseDate(
            String date
    ) {

        if (
                date == null
                        ||
                        date.trim().isEmpty()
        ) {

            return null;
        }


        try {

            java.text.SimpleDateFormat format =
                    new java.text.SimpleDateFormat(
                            "yyyy-MM-dd"
                    );

            format.setLenient(false);

            return format.parse(
                    date.trim()
            );

        } catch (java.text.ParseException e) {

            return null;
        }
    }


    // =========================================
    // CARD
    // =========================================

    private JPanel createCard() {

        JPanel panel =
                new JPanel();

        panel.setBackground(CARD);

        panel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER
                        ),
                        new EmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        return panel;
    }
}