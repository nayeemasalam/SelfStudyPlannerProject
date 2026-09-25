import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SmartPlanner {

    private SmartPlanner() {
    }


    // ==========================================
    // GENERATE 7 DAY PLAN
    // ==========================================

    public static void generateWeeklyPlan(
            User user
    ) {

        if (user.subjects == null) {

            return;
        }


        if (user.tasks == null) {

            user.tasks = new ArrayList<>();
        }


        // Remove old unfinished generated tasks
        user.tasks.removeIf(
                task -> !task.completed
        );


        double dailyHours =
                StudyTimeStorage.getHours(user);


        if (dailyHours <= 0) {

            dailyHours = 2.0;
        }


        // ======================================
        // COLLECT PENDING TOPICS
        // ======================================

        List<PlannerTopic> topics =
                new ArrayList<>();


        for (Subject subject : user.subjects) {

            if (subject.topics == null) {
                continue;
            }


            for (Topic topic : subject.topics) {

                if (!topic.completed) {

                    topics.add(
                            new PlannerTopic(
                                    subject,
                                    topic
                            )
                    );
                }
            }
        }


        // ======================================
        // SORT BY EXAM + DIFFICULTY
        // ======================================

        topics.sort(
                Comparator
                        .comparing(
                                (PlannerTopic p)
                                        -> getExamDate(
                                        p.subject
                                )
                        )
                        .thenComparing(
                                p ->
                                        difficultyValue(
                                                p.topic.difficulty
                                        )
                        )
        );


        // ======================================
        // CREATE 7 DAYS
        // ======================================

        LocalDate currentDate =
                LocalDate.now();


        double remainingHours =
                dailyHours;


        int dayOffset = 0;


        for (PlannerTopic plannerTopic : topics) {

            if (dayOffset >= 7) {
                break;
            }


            double requiredHours =
                    estimatedHours(
                            plannerTopic.topic
                                    .difficulty
                    );


            // If today's remaining time
            // is not enough, move to next day.

            if (
                    requiredHours >
                            remainingHours
            ) {

                dayOffset++;

                remainingHours =
                        dailyHours;


                if (dayOffset >= 7) {
                    break;
                }
            }


            LocalDate taskDate =
                    currentDate.plusDays(
                            dayOffset
                    );


            StudyTask task =
                    new StudyTask(
                            plannerTopic.topic.name,
                            plannerTopic.subject.name,
                            taskDate.toString(),
                            requiredHours
                    );


            user.tasks.add(task);


            remainingHours -=
                    requiredHours;


            if (remainingHours <= 0) {

                dayOffset++;

                remainingHours =
                        dailyHours;
            }
        }


        DataStore.saveUser(user);
    }


    // ==========================================
    // ESTIMATE HOURS
    // ==========================================

    public static double estimatedHours(
            Difficulty difficulty
    ) {

        if (difficulty == null) {

            return 1.0;
        }


        switch (difficulty) {

            case EASY:
                return 0.75;

            case MEDIUM:
                return 1.25;

            case HARD:
                return 1.75;

            default:
                return 1.0;
        }
    }


    // ==========================================
    // DIFFICULTY VALUE
    // ==========================================

    private static int difficultyValue(
            Difficulty difficulty
    ) {

        if (difficulty == null) {
            return 2;
        }


        switch (difficulty) {

            case HARD:
                return 1;

            case MEDIUM:
                return 2;

            case EASY:
                return 3;

            default:
                return 2;
        }
    }


    // ==========================================
    // EXAM DATE
    // ==========================================

    private static LocalDate getExamDate(
            Subject subject
    ) {

        try {

            return LocalDate.parse(
                    subject.examDate
            );

        } catch (Exception e) {

            return LocalDate.MAX;
        }
    }


    // ==========================================
    // INTERNAL PLANNER OBJECT
    // ==========================================

    private static class PlannerTopic {

        Subject subject;
        Topic topic;


        PlannerTopic(
                Subject subject,
                Topic topic
        ) {

            this.subject = subject;
            this.topic = topic;
        }
    }
}