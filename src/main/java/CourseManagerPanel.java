import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CourseManagerPanel extends JPanel {

    private final User user;
    private final List<User> users;
    private final Runnable refresh;

    private JLabel courseInfo;


    public CourseManagerPanel(
            User user,
            List<User> users,
            Runnable refresh
    ) {

        this.user = user;
        this.users = users;
        this.refresh = refresh;

        setLayout(new BorderLayout());
        setBackground(AppColors.BG);

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
                        25,
                        30,
                        25,
                        30
                )
        );


        JLabel title =
                Utils.label(
                        "Course Manager",
                        24,
                        true
                );

        main.add(title);


        main.add(
                Box.createVerticalStrut(10)
        );


        JLabel subtitle =
                Utils.label(
                        "Start a new course without creating a new account.",
                        14,
                        false
                );

        subtitle.setForeground(
                AppColors.MUTED
        );

        main.add(subtitle);


        main.add(
                Box.createVerticalStrut(25)
        );


        JPanel card =
                Utils.card();

        card.setLayout(
                new BoxLayout(
                        card,
                        BoxLayout.Y_AXIS
                )
        );


        courseInfo =
                Utils.label(
                        getCourseInfo(),
                        16,
                        true
                );

        card.add(courseInfo);


        card.add(
                Box.createVerticalStrut(20)
        );


        JButton newCourse =
                Utils.button(
                        "Start New Course",
                        true
                );


        JButton reset =
                Utils.button(
                        "Reset Current Course",
                        false
                );


        card.add(newCourse);

        card.add(
                Box.createVerticalStrut(10)
        );

        card.add(reset);


        main.add(card);


        // ======================================
        // NEW COURSE
        // ======================================

        newCourse.addActionListener(
                e -> createNewCourse()
        );


        // ======================================
        // RESET
        // ======================================

        reset.addActionListener(
                e -> resetCourse()
        );


        add(
                new JScrollPane(main),
                BorderLayout.CENTER
        );
    }


    // ==========================================
    // COURSE INFO
    // ==========================================

    private String getCourseInfo() {

        int subjects =
                user.subjects == null
                        ? 0
                        : user.subjects.size();


        int topics = 0;


        if (user.subjects != null) {

            for (Subject subject :
                    user.subjects) {

                if (subject.topics != null) {

                    topics +=
                            subject.topics.size();
                }
            }
        }


        return
                "Current Course  •  "
                        + subjects
                        + " Subjects  •  "
                        + topics
                        + " Topics";
    }


    // ==========================================
    // NEW COURSE
    // ==========================================

    private void createNewCourse() {

        String name =
                JOptionPane.showInputDialog(
                        this,
                        "Enter new course name:",
                        "New Course",
                        JOptionPane.PLAIN_MESSAGE
                );


        if (
                name == null
                        ||
                        name.trim().isEmpty()
        ) {

            return;
        }


        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Start a new course?\n\n"
                                + "Current subjects and tasks "
                                + "will be cleared.",
                        "Confirm New Course",
                        JOptionPane.YES_NO_OPTION
                );


        if (
                answer !=
                        JOptionPane.YES_OPTION
        ) {

            return;
        }


        user.subjects.clear();
        user.tasks.clear();


        DataStore.saveUser(user);


        courseInfo.setText(
                "Current Course: "
                        + name
                        + "  •  0 Subjects  •  0 Topics"
        );


        JOptionPane.showMessageDialog(
                this,
                "New course started successfully!"
        );


        if (refresh != null) {
            refresh.run();
        }
    }


    // ==========================================
    // RESET COURSE
    // ==========================================

    private void resetCourse() {

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to reset "
                                + "the current course?",
                        "Reset Course",
                        JOptionPane.YES_NO_OPTION
                );


        if (
                answer !=
                        JOptionPane.YES_OPTION
        ) {

            return;
        }


        user.subjects.clear();
        user.tasks.clear();


        DataStore.saveUser(user);


        courseInfo.setText(
                getCourseInfo()
        );


        JOptionPane.showMessageDialog(
                this,
                "Course has been reset."
        );


        if (refresh != null) {
            refresh.run();
        }
    }
}