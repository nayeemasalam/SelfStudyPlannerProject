import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StudyNowPanel extends JPanel {

    private final User user;

    public StudyNowPanel(User user) {

        this.user = user;

        setLayout(new BorderLayout());
        setBackground(AppColors.BG);

        setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );

        buildUI();
    }


    // ==========================================
    // BUILD UI
    // ==========================================

    private void buildUI() {

        JPanel card = Utils.card();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );


        // ==========================================
        // TITLE
        // ==========================================

        JLabel title =
                Utils.label(
                        "⚡ Study Now",
                        20,
                        true
                );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(title);

        card.add(
                Box.createVerticalStrut(12)
        );


        // ==========================================
        // FIND MOST IMPORTANT TOPIC
        // ==========================================

        Topic selectedTopic = null;
        Subject selectedSubject = null;


        for (Subject subject : user.subjects) {

            for (Topic topic : subject.topics) {

                // Already completed হলে বাদ
                if (topic.completed) {
                    continue;
                }


                // প্রথম pending topic
                if (selectedTopic == null) {

                    selectedTopic = topic;
                    selectedSubject = subject;

                } else {

                    int currentPriority =
                            getPriority(
                                    subject,
                                    topic
                            );

                    int selectedPriority =
                            getPriority(
                                    selectedSubject,
                                    selectedTopic
                            );


                    // বেশি priority হলে select করবে
                    if (
                            currentPriority >
                                    selectedPriority
                    ) {

                        selectedTopic = topic;
                        selectedSubject = subject;
                    }
                }
            }
        }


        // ==========================================
        // NO PENDING TOPIC
        // ==========================================

        if (selectedTopic == null) {

            JLabel done =
                    Utils.label(
                            "🎉 All topics are completed!",
                            16,
                            true
                    );

            done.setForeground(
                    AppColors.SUCCESS
            );

            done.setAlignmentX(
                    Component.LEFT_ALIGNMENT
            );

            card.add(done);

            add(
                    card,
                    BorderLayout.CENTER
            );

            return;
        }


        // ==========================================
        // FINAL VARIABLES
        // ==========================================
        // Lambda-এর জন্য এগুলো final রাখা হয়েছে।

        final String selectedTopicName =
                selectedTopic.name;

        final String selectedSubjectName =
                selectedSubject.name;

        final Difficulty selectedDifficulty =
                selectedTopic.difficulty;

        final String selectedExamDate =
                selectedSubject.examDate;


        // ==========================================
        // TOPIC
        // ==========================================

        JLabel topicLabel =
                Utils.label(
                        selectedTopicName,
                        24,
                        true
                );

        topicLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(topicLabel);


        card.add(
                Box.createVerticalStrut(8)
        );


        // ==========================================
        // SUBJECT
        // ==========================================

        JLabel subjectLabel =
                Utils.label(
                        "Subject: " +
                                selectedSubjectName,
                        14,
                        false
                );

        subjectLabel.setForeground(
                AppColors.MUTED
        );

        subjectLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(subjectLabel);


        card.add(
                Box.createVerticalStrut(6)
        );


        // ==========================================
        // DIFFICULTY
        // ==========================================

        String difficultyText;


        if (
                selectedDifficulty ==
                        Difficulty.HARD
        ) {

            difficultyText = "Hard";

        } else if (
                selectedDifficulty ==
                        Difficulty.MEDIUM
        ) {

            difficultyText = "Medium";

        } else {

            difficultyText = "Easy";
        }


        JLabel difficultyLabel =
                Utils.label(
                        "Difficulty: " +
                                difficultyText,
                        14,
                        true
                );

        difficultyLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(difficultyLabel);


        card.add(
                Box.createVerticalStrut(6)
        );


        // ==========================================
        // EXAM DATE
        // ==========================================

        JLabel examLabel =
                Utils.label(
                        "Exam Date: " +
                                selectedExamDate,
                        14,
                        false
                );

        examLabel.setForeground(
                AppColors.MUTED
        );

        examLabel.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        card.add(examLabel);


        card.add(
                Box.createVerticalStrut(18)
        );


        // ==========================================
        // STUDY NOW BUTTON
        // ==========================================

        JButton studyButton =
                Utils.button(
                        "📖 Study Now",
                        true
                );

        studyButton.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );


        studyButton.addActionListener(
                e -> openSearch(
                        selectedTopicName
                )
        );


        card.add(studyButton);


        // ==========================================
        // ADD CARD
        // ==========================================

        add(
                card,
                BorderLayout.CENTER
        );
    }


    // ==========================================
    // PRIORITY CALCULATION
    // ==========================================

    private int getPriority(
            Subject subject,
            Topic topic
    ) {

        int score = 0;


        // ==========================================
        // DIFFICULTY SCORE
        // ==========================================

        if (
                topic.difficulty ==
                        Difficulty.HARD
        ) {

            score += 30;

        } else if (
                topic.difficulty ==
                        Difficulty.MEDIUM
        ) {

            score += 20;

        } else {

            score += 10;
        }


        // ==========================================
        // EXAM DATE SCORE
        // ==========================================

        try {

            java.time.LocalDate examDate =
                    java.time.LocalDate.parse(
                            subject.examDate
                    );


            java.time.LocalDate today =
                    java.time.LocalDate.now();


            long days =
                    java.time.temporal.ChronoUnit
                            .DAYS
                            .between(
                                    today,
                                    examDate
                            );


            if (days <= 1) {

                score += 100;

            } else if (days <= 3) {

                score += 70;

            } else if (days <= 7) {

                score += 40;

            } else {

                score += 10;
            }


        } catch (Exception e) {

            // Invalid date হলে
            // minimum priority
            score += 5;
        }


        return score;
    }


    // ==========================================
    // OPEN GOOGLE SEARCH
    // ==========================================

    private void openSearch(
            String topic
    ) {

        try {

            String encodedTopic =
                    URLEncoder.encode(
                            topic +
                                    " easy explanation",
                            StandardCharsets.UTF_8
                    );


            String url =
                    "https://www.google.com/search?q="
                            + encodedTopic;


            Desktop.getDesktop()
                    .browse(
                            new URI(url)
                    );


        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not open browser.",
                    "Browser Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}