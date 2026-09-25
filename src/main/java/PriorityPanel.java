import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriorityPanel extends JPanel {

    private final User user;

    public PriorityPanel(User user) {

        this.user = user;

        setLayout(
                new BorderLayout()
        );

        setBackground(
                AppColors.BG
        );

        buildUI();
    }


    private void buildUI() {

        JPanel main =
                new JPanel();

        main.setLayout(
                new BoxLayout(
                        main,
                        BoxLayout.Y_AXIS
                )
        );

        main.setBackground(
                AppColors.BG
        );

        main.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );


        main.add(
                Utils.label(
                        "⚡ Priority Topics",
                        20,
                        true
                )
        );


        main.add(
                Box.createVerticalStrut(15)
        );


        List<PriorityItem> items =
                new ArrayList<>();


        for (Subject subject :
                user.subjects) {

            for (Topic topic :
                    subject.topics) {

                if (topic.completed) {
                    continue;
                }


                long days =
                        daysUntilExam(
                                subject.examDate
                        );


                int score =
                        calculateScore(
                                topic.difficulty,
                                days
                        );


                items.add(
                        new PriorityItem(
                                subject,
                                topic,
                                days,
                                score
                        )
                );
            }
        }


        items.sort(
                Comparator.comparingInt(
                        item -> -item.score
                )
        );


        if (items.isEmpty()) {

            JLabel done =
                    Utils.label(
                            "✓ No pending topics!",
                            15,
                            false
                    );

            done.setForeground(
                    AppColors.SUCCESS
            );

            main.add(done);

        } else {

            int count =
                    Math.min(
                            items.size(),
                            5
                    );


            for (int i = 0; i < count; i++) {

                PriorityItem item =
                        items.get(i);


                JPanel card =
                        Utils.card();

                card.setMaximumSize(
                        new Dimension(
                                Integer.MAX_VALUE,
                                75
                        )
                );


                JPanel left =
                        new JPanel();

                left.setLayout(
                        new BoxLayout(
                                left,
                                BoxLayout.Y_AXIS
                        )
                );

                left.setBackground(
                        AppColors.CARD
                );


                left.add(
                        Utils.label(
                                item.topic.name,
                                14,
                                true
                        )
                );


                left.add(
                        Utils.label(
                                item.subject.name,
                                12,
                                false
                        )
                );


                card.add(
                        left,
                        BorderLayout.WEST
                );


                String priority =
                        getPriorityText(
                                item.score
                        );


                JLabel priorityLabel =
                        Utils.label(
                                priority,
                                13,
                                true
                        );


                priorityLabel.setForeground(
                        getPriorityColor(
                                item.score
                        )
                );


                card.add(
                        priorityLabel,
                        BorderLayout.EAST
                );


                main.add(card);


                main.add(
                        Box.createVerticalStrut(8)
                );
            }
        }


        add(
                new JScrollPane(main),
                BorderLayout.CENTER
        );
    }


    private int calculateScore(
            Difficulty difficulty,
            long days
    ) {

        int score = 0;


        if (days <= 1) {

            score += 100;

        } else if (days <= 3) {

            score += 70;

        } else if (days <= 7) {

            score += 40;

        } else {

            score += 10;
        }


        if (difficulty == Difficulty.HARD) {

            score += 40;

        } else if (
                difficulty == Difficulty.MEDIUM
        ) {

            score += 20;

        } else {

            score += 5;
        }


        return score;
    }


    private String getPriorityText(
            int score
    ) {

        if (score >= 100) {

            return "HIGH";

        } else if (score >= 50) {

            return "MEDIUM";

        } else {

            return "LOW";
        }
    }


    private Color getPriorityColor(
            int score
    ) {

        if (score >= 100) {

            return AppColors.DANGER;

        } else if (score >= 50) {

            return AppColors.WARNING;

        } else {

            return AppColors.SUCCESS;
        }
    }


    private long daysUntilExam(
            String examDate
    ) {

        try {

            return ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    LocalDate.parse(examDate)
            );

        } catch (Exception e) {

            return 999;
        }
    }


    private static class PriorityItem {

        Subject subject;
        Topic topic;
        long days;
        int score;


        PriorityItem(
                Subject subject,
                Topic topic,
                long days,
                int score
        ) {

            this.subject = subject;
            this.topic = topic;
            this.days = days;
            this.score = score;
        }
    }
}