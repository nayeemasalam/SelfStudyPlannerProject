import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ProgressPanel extends JPanel {

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

    private static final Color GREEN =
            new Color(47, 171, 118);

    private static final Color WARNING =
            new Color(235, 167, 55);


    public ProgressPanel(
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


    // =========================================================
    // CREATE UI
    // =========================================================

    private void createUI() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(BG);

        JLabel title =
                new JLabel("Progress");

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        22
                )
        );

        title.setForeground(TEXT);

        header.add(
                title,
                BorderLayout.WEST
        );

        add(
                header,
                BorderLayout.NORTH
        );


        JPanel content =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                15,
                                15
                        )
                );

        content.setBackground(BG);

        content.setBorder(
                new EmptyBorder(
                        20,
                        0,
                        0,
                        0
                )
        );


        content.add(
                createProgressCard()
        );

        content.add(
                createTopicCard()
        );

        content.add(
                createCompletedCard()
        );

        content.add(
                createPendingCard()
        );


        add(
                content,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // OVERALL PROGRESS
    // =========================================================

    private JPanel createProgressCard() {

        JPanel card =
                createCard();

        card.setLayout(
                new GridBagLayout()
        );


        int total =
                getTotalTopics();

        int completed =
                getCompletedTopics();

        int percentage =
                calculatePercentage(
                        total,
                        completed
                );


        JPanel inside =
                new JPanel();

        inside.setBackground(CARD);

        inside.setLayout(
                new BoxLayout(
                        inside,
                        BoxLayout.Y_AXIS
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
                        15
                )
        );

        title.setForeground(TEXT);

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel percent =
                new JLabel(
                        percentage + "%"
                );

        percent.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        26
                )
        );

        percent.setForeground(PRIMARY);

        percent.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JProgressBar progress =
                new JProgressBar(
                        0,
                        100
                );

        progress.setValue(
                percentage
        );

        progress.setForeground(
                GREEN
        );

        progress.setBackground(
                new Color(
                        235,
                        237,
                        245
                )
        );

        progress.setPreferredSize(
                new Dimension(
                        250,
                        18
                )
        );

        progress.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        inside.add(title);

        inside.add(
                Box.createVerticalStrut(18)
        );

        inside.add(percent);

        inside.add(
                Box.createVerticalStrut(12)
        );

        inside.add(progress);


        card.add(inside);

        return card;
    }


    // =========================================================
    // TOTAL TOPICS
    // =========================================================

    private JPanel createTopicCard() {

        return numberCard(
                "Total Topics",
                String.valueOf(
                        getTotalTopics()
                ),
                PRIMARY
        );
    }


    // =========================================================
    // COMPLETED TOPICS
    // =========================================================

    private JPanel createCompletedCard() {

        return numberCard(
                "Completed Topics",
                String.valueOf(
                        getCompletedTopics()
                ),
                GREEN
        );
    }


    // =========================================================
    // PENDING TOPICS
    // =========================================================

    private JPanel createPendingCard() {

        int pending =
                getTotalTopics()
                        - getCompletedTopics();

        return numberCard(
                "Pending Topics",
                String.valueOf(pending),
                WARNING
        );
    }


    // =========================================================
    // NUMBER CARD
    // =========================================================

    private JPanel numberCard(
            String title,
            String number,
            Color color
    ) {

        JPanel card =
                createCard();

        card.setLayout(
                new GridBagLayout()
        );


        JPanel inside =
                new JPanel();

        inside.setBackground(CARD);

        inside.setLayout(
                new BoxLayout(
                        inside,
                        BoxLayout.Y_AXIS
                )
        );


        JLabel titleLabel =
                new JLabel(title);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        titleLabel.setForeground(TEXT);

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel numberLabel =
                new JLabel(number);

        numberLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        34
                )
        );

        numberLabel.setForeground(color);

        numberLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        inside.add(titleLabel);

        inside.add(
                Box.createVerticalStrut(15)
        );

        inside.add(numberLabel);


        card.add(inside);

        return card;
    }


    // =========================================================
    // CALCULATE PERCENTAGE
    // =========================================================

    private int calculatePercentage(
            int total,
            int completed
    ) {

        if (total <= 0) {
            return 0;
        }

        return Math.round(
                completed * 100f / total
        );
    }


    // =========================================================
    // TOTAL TOPICS
    // =========================================================

    private int getTotalTopics() {

        int count = 0;

        if (user.subjects == null) {
            return 0;
        }


        for (Subject subject : user.subjects) {

            if (subject.topics == null) {
                continue;
            }

            count +=
                    subject.topics.size();
        }


        return count;
    }


    // =========================================================
    // COMPLETED TOPICS
    // =========================================================

    private int getCompletedTopics() {

        int count = 0;

        if (user.subjects == null) {
            return 0;
        }


        for (Subject subject : user.subjects) {

            if (subject.topics == null) {
                continue;
            }


            for (Topic topic : subject.topics) {

                if (topic.completed) {
                    count++;
                }
            }
        }


        return count;
    }


    // =========================================================
    // REFRESH PROGRESS
    // =========================================================

    public void refreshProgress() {

        removeAll();

        createUI();

        revalidate();

        repaint();
    }


    // =========================================================
    // CARD
    // =========================================================

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