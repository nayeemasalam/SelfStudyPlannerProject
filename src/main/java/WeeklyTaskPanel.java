import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class WeeklyTaskPanel extends JPanel {

    private final User user;
    private final List<User> users;
    private final Runnable refresh;

    private JTable table;
    private DefaultTableModel model;


    public WeeklyTaskPanel(
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
        loadTasks();
    }


    private void buildUI() {

        JPanel top =
                new JPanel(
                        new BorderLayout()
                );

        top.setBackground(
                AppColors.BG
        );

        top.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        25,
                        10,
                        25
                )
        );


        JLabel title =
                Utils.label(
                        "Weekly Tasks",
                        24,
                        true
                );

        top.add(
                title,
                BorderLayout.WEST
        );


        JButton generateButton =
                Utils.button(
                        "Generate Smart Plan",
                        true
                );

        top.add(
                generateButton,
                BorderLayout.EAST
        );


        add(
                top,
                BorderLayout.NORTH
        );


        model =
                new DefaultTableModel(
                        new Object[]{
                                "Date",
                                "Subject",
                                "Topic",
                                "Hours",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return false;
                    }
                };


        table =
                new JTable(model);

        table.setRowHeight(35);

        table.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );


        table.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                13
                        )
                );


        table.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );


        // ======================================
        // STATUS COLOR
        // ======================================

        table.setDefaultRenderer(
                Object.class,
                new DefaultTableCellRenderer() {

                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table,
                            Object value,
                            boolean isSelected,
                            boolean hasFocus,
                            int row,
                            int column
                    ) {

                        Component component =
                                super.getTableCellRendererComponent(
                                        table,
                                        value,
                                        isSelected,
                                        hasFocus,
                                        row,
                                        column
                                );


                        String status =
                                String.valueOf(
                                        table.getValueAt(
                                                row,
                                                4
                                        )
                                );


                        if (
                                status.equals("MISSED")
                        ) {

                            component.setForeground(
                                    AppColors.DANGER
                            );

                        } else if (
                                status.equals("COMPLETED")
                        ) {

                            component.setForeground(
                                    AppColors.SUCCESS
                            );

                        } else {

                            component.setForeground(
                                    AppColors.TEXT
                            );
                        }


                        return component;
                    }
                }
        );


        JScrollPane scrollPane =
                new JScrollPane(table);


        add(
                scrollPane,
                BorderLayout.CENTER
        );


        // ======================================
        // BUTTON PANEL
        // ======================================

        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        bottom.setBackground(
                AppColors.BG
        );


        JButton completeButton =
                Utils.button(
                        "✓ Complete",
                        true
                );


        JButton missedButton =
                Utils.button(
                        "Mark Missed",
                        false
                );


        bottom.add(
                completeButton
        );

        bottom.add(
                missedButton
        );


        add(
                bottom,
                BorderLayout.SOUTH
        );


        // ======================================
        // ACTIONS
        // ======================================

        generateButton.addActionListener(
                e -> {

                    SmartPlanner.generateWeeklyPlan(
                            user
                    );

                    loadTasks();

                    JOptionPane.showMessageDialog(
                            this,
                            "7-day study plan generated successfully!"
                    );
                }
        );


        completeButton.addActionListener(
                e -> markComplete()
        );


        missedButton.addActionListener(
                e -> markMissed()
        );
    }


    // ==========================================
    // LOAD TASKS
    // ==========================================

    private void loadTasks() {

        model.setRowCount(0);


        if (user.tasks == null) {
            return;
        }


        LocalDate today =
                LocalDate.now();


        for (StudyTask task : user.tasks) {

            if (!isInsideThisWeek(task.date)) {
                continue;
            }


            String status;


            // Automatically detect missed task

            try {

                LocalDate taskDate =
                        LocalDate.parse(
                                task.date
                        );


                if (
                        !task.completed
                                &&
                                taskDate.isBefore(today)
                ) {

                    task.missed = true;
                }

            } catch (Exception ignored) {
            }


            if (task.completed) {

                status = "COMPLETED";

            } else if (task.missed) {

                status = "MISSED";

            } else {

                status = "PENDING";
            }


            model.addRow(
                    new Object[]{
                            task.date,
                            task.subject,
                            task.topic,
                            task.hours,
                            status
                    }
            );
        }


        DataStore.saveUser(user);
    }


    // ==========================================
    // COMPLETE
    // ==========================================

    private void markComplete() {

        int row =
                table.getSelectedRow();


        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a task first."
            );

            return;
        }


        String topic =
                String.valueOf(
                        model.getValueAt(
                                row,
                                2
                        )
                );


        StudyTask selected =
                findTask(topic);


        if (selected == null) {
            return;
        }


        selected.completed = true;
        selected.missed = false;


        // Also mark topic completed

        markTopicCompleted(
                selected.subject,
                selected.topic
        );


        DataStore.saveUser(user);

        loadTasks();


        if (refresh != null) {
            refresh.run();
        }
    }


    // ==========================================
    // MARK MISSED
    // ==========================================

    private void markMissed() {

        int row =
                table.getSelectedRow();


        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a task first."
            );

            return;
        }


        String topic =
                String.valueOf(
                        model.getValueAt(
                                row,
                                2
                        )
                );


        StudyTask selected =
                findTask(topic);


        if (selected == null) {
            return;
        }


        selected.missed = true;
        selected.completed = false;


        DataStore.saveUser(user);

        loadTasks();
    }


    // ==========================================
    // FIND TASK
    // ==========================================

    private StudyTask findTask(
            String topic
    ) {

        for (StudyTask task : user.tasks) {

            if (
                    task.topic.equals(topic)
            ) {

                return task;
            }
        }


        return null;
    }


    // ==========================================
    // COMPLETE TOPIC
    // ==========================================

    private void markTopicCompleted(
            String subjectName,
            String topicName
    ) {

        for (Subject subject : user.subjects) {

            if (
                    subject.name.equals(
                            subjectName
                    )
            ) {

                for (Topic topic : subject.topics) {

                    if (
                            topic.name.equals(
                                    topicName
                            )
                    ) {

                        topic.completed = true;
                        return;
                    }
                }
            }
        }
    }


    // ==========================================
    // THIS WEEK
    // ==========================================

    private boolean isInsideThisWeek(
            String dateText
    ) {

        try {

            LocalDate date =
                    LocalDate.parse(
                            dateText
                    );

            LocalDate today =
                    LocalDate.now();


            LocalDate lastDay =
                    today.plusDays(6);


            return
                    !date.isBefore(today)
                            &&
                            !date.isAfter(lastDay);

        } catch (Exception e) {

            return false;
        }
    }
}