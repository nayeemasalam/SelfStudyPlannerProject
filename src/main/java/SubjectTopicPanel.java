import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class SubjectTopicPanel extends JPanel {

    private final User user;
    private final List<User> users;

    private JComboBox<String> subjectCombo;
    private DefaultTableModel tableModel;
    private JTable topicTable;

    private JLabel progressLabel;
    private JProgressBar progressBar;

    public SubjectTopicPanel(User user, List<User> users) {

        this.user = user;
        this.users = users;

        setLayout(new BorderLayout());
        setBackground(AppColors.BG);
        setBorder(new EmptyBorder(24, 28, 24, 28));

        buildUI();
        refreshSubjects();
    }


    // =========================================================
    // MAIN UI
    // =========================================================

    private void buildUI() {

        JPanel main = new JPanel(new BorderLayout(0, 18));
        main.setBackground(AppColors.BG);


        // -----------------------------------------------------
        // HEADER
        // -----------------------------------------------------

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );
        titlePanel.setOpaque(false);

        JLabel title =
                Utils.label(
                        "Subject & Topic Management",
                        25,
                        true
                );

        JLabel subtitle =
                Utils.label(
                        "Add topics, analyze difficulty and mark your completed study.",
                        13,
                        false
                );

        subtitle.setForeground(AppColors.MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);


        JButton addSubjectButton =
                Utils.button("+ Add Subject", true);

        addSubjectButton.setPreferredSize(
                new Dimension(145, 42)
        );

        addSubjectButton.addActionListener(
                e -> addSubject()
        );

        header.add(
                titlePanel,
                BorderLayout.WEST
        );

        header.add(
                addSubjectButton,
                BorderLayout.EAST
        );

        main.add(
                header,
                BorderLayout.NORTH
        );


        // -----------------------------------------------------
        // CENTER
        // -----------------------------------------------------

        JPanel center =
                new JPanel(
                        new BorderLayout(0, 15)
                );

        center.setOpaque(false);


        // -----------------------------------------------------
        // SUBJECT SELECTOR
        // -----------------------------------------------------

        JPanel subjectCard = Utils.card();

        subjectCard.setLayout(
                new BorderLayout(12, 0)
        );

        JLabel subjectLabel =
                Utils.label(
                        "Select Subject",
                        14,
                        true
                );

        subjectCombo =
                new JComboBox<>();

        subjectCombo.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        subjectCombo.setPreferredSize(
                new Dimension(260, 38)
        );

        subjectCombo.addActionListener(
                e -> refreshTopics()
        );


        JButton addTopicButton =
                Utils.button("+ Add Topic", true);

        addTopicButton.setPreferredSize(
                new Dimension(125, 38)
        );

        addTopicButton.addActionListener(
                e -> addTopic()
        );


        JPanel subjectLeft =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                0
                        )
                );

        subjectLeft.setOpaque(false);

        subjectLeft.add(subjectLabel);
        subjectLeft.add(subjectCombo);

        subjectCard.add(
                subjectLeft,
                BorderLayout.WEST
        );

        subjectCard.add(
                addTopicButton,
                BorderLayout.EAST
        );

        center.add(
                subjectCard,
                BorderLayout.NORTH
        );


        // -----------------------------------------------------
        // TABLE
        // -----------------------------------------------------

        JPanel tableCard = Utils.card();

        tableCard.setLayout(
                new BorderLayout(0, 12)
        );

        JLabel tableTitle =
                Utils.label(
                        "Topics",
                        17,
                        true
                );

        tableCard.add(
                tableTitle,
                BorderLayout.NORTH
        );


        String[] columns = {
                "Topic Name",
                "AI Difficulty",
                "Status",
                "Priority",
                "Action"
        };


        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {

                        return column == 4;
                    }


                    @Override
                    public Class<?> getColumnClass(
                            int column
                    ) {

                        if (column == 4) {
                            return JButton.class;
                        }

                        return String.class;
                    }
                };


        topicTable =
                new JTable(tableModel);

        topicTable.setRowHeight(52);

        topicTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        topicTable.getTableHeader().setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        topicTable.getTableHeader().setBackground(
                AppColors.PRIMARY_LIGHT
        );

        topicTable.getTableHeader().setForeground(
                AppColors.TEXT
        );

        topicTable.setGridColor(
                AppColors.BORDER
        );

        topicTable.setSelectionBackground(
                AppColors.PRIMARY_LIGHT
        );

        topicTable.setSelectionForeground(
                AppColors.TEXT
        );

        topicTable.setShowVerticalLines(false);


        // -----------------------------------------------------
        // COLUMN WIDTHS
        // -----------------------------------------------------

        topicTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(260);

        topicTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(120);

        topicTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(130);

        topicTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(100);

        topicTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(120);


        // -----------------------------------------------------
        // DIFFICULTY RENDERER
        // -----------------------------------------------------

        topicTable.getColumnModel()
                .getColumn(1)
                .setCellRenderer(
                        new DifficultyRenderer()
                );


        // -----------------------------------------------------
        // STATUS RENDERER
        // -----------------------------------------------------

        topicTable.getColumnModel()
                .getColumn(2)
                .setCellRenderer(
                        new StatusRenderer()
                );


        // -----------------------------------------------------
        // PRIORITY RENDERER
        // -----------------------------------------------------

        topicTable.getColumnModel()
                .getColumn(3)
                .setCellRenderer(
                        new PriorityRenderer()
                );


        // -----------------------------------------------------
        // ACTION RENDERER / EDITOR
        // -----------------------------------------------------

        topicTable.getColumnModel()
                .getColumn(4)
                .setCellRenderer(
                        new ActionButtonRenderer()
                );

        topicTable.getColumnModel()
                .getColumn(4)
                .setCellEditor(
                        new ActionButtonEditor(
                                new JCheckBox()
                        )
                );


        JScrollPane scrollPane =
                new JScrollPane(topicTable);

        scrollPane.setBorder(
                BorderFactory.createEmptyBorder()
        );

        scrollPane.getViewport().setBackground(
                Color.WHITE
        );

        tableCard.add(
                scrollPane,
                BorderLayout.CENTER
        );


        // -----------------------------------------------------
        // BOTTOM AREA
        // -----------------------------------------------------

        JPanel bottom =
                new JPanel(
                        new BorderLayout(15, 0)
                );

        bottom.setOpaque(false);


        // -----------------------------------------------------
        // PROGRESS CARD
        // -----------------------------------------------------

        JPanel progressCard =
                Utils.card();

        progressCard.setLayout(
                new BorderLayout(10, 5)
        );

        JPanel progressTop =
                new JPanel(
                        new BorderLayout()
                );

        progressTop.setOpaque(false);

        JLabel progressTitle =
                Utils.label(
                        "Study Progress",
                        14,
                        true
                );

        progressLabel =
                Utils.label(
                        "0%",
                        14,
                        true
                );

        progressLabel.setForeground(
                AppColors.PRIMARY
        );

        progressTop.add(
                progressTitle,
                BorderLayout.WEST
        );

        progressTop.add(
                progressLabel,
                BorderLayout.EAST
        );


        progressBar =
                new JProgressBar(
                        0,
                        100
                );

        progressBar.setValue(0);
        progressBar.setStringPainted(false);

        progressBar.setPreferredSize(
                new Dimension(250, 10)
        );

        progressCard.add(
                progressTop,
                BorderLayout.NORTH
        );

        progressCard.add(
                progressBar,
                BorderLayout.CENTER
        );


        // -----------------------------------------------------
        // DELETE BUTTON
        // -----------------------------------------------------

        JButton deleteButton =
                Utils.button(
                        "Delete Selected Topic",
                        false
                );

        deleteButton.setForeground(
                AppColors.DANGER
        );

        deleteButton.addActionListener(
                e -> deleteTopic()
        );


        // -----------------------------------------------------
        // RESET BUTTON
        // -----------------------------------------------------

        JButton resetButton =
                Utils.button(
                        "Reset Course",
                        false
                );

        resetButton.setForeground(
                AppColors.DANGER
        );

        resetButton.addActionListener(
                e -> resetCourse()
        );


        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                0
                        )
                );

        buttons.setOpaque(false);

        buttons.add(deleteButton);
        buttons.add(resetButton);


        bottom.add(
                progressCard,
                BorderLayout.WEST
        );

        bottom.add(
                buttons,
                BorderLayout.EAST
        );


        center.add(
                tableCard,
                BorderLayout.CENTER
        );

        center.add(
                bottom,
                BorderLayout.SOUTH
        );


        main.add(
                center,
                BorderLayout.CENTER
        );


        add(
                main,
                BorderLayout.CENTER
        );
    }


    // =========================================================
    // SUBJECT
    // =========================================================

    private void refreshSubjects() {

        if (subjectCombo == null) {
            return;
        }

        subjectCombo.removeAllItems();

        for (Subject subject : user.subjects) {

            subjectCombo.addItem(
                    subject.name
            );
        }

        if (subjectCombo.getItemCount() > 0) {

            subjectCombo.setSelectedIndex(0);
        }

        refreshTopics();
    }


    // =========================================================
    // ADD SUBJECT
    // =========================================================

    private void addSubject() {

        JTextField subjectField =
                Utils.field(
                        "Enter subject name"
                );


        // NEW: CLASS DATE
        JTextField classField =
                Utils.field(
                        "Class date (yyyy-MM-dd)"
                );


        // EXISTING: EXAM DATE
        JTextField examField =
                Utils.field(
                        "Exam date (yyyy-MM-dd)"
                );


        JPanel panel =
                new JPanel(
                        new GridLayout(
                                6,
                                1,
                                8,
                                8
                        )
                );

        panel.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );


        // Subject Name

        panel.add(
                Utils.label(
                        "Subject Name",
                        13,
                        true
                )
        );

        panel.add(subjectField);


        // Class Date

        panel.add(
                Utils.label(
                        "Class Date",
                        13,
                        true
                )
        );

        panel.add(classField);


        // Exam Date

        panel.add(
                Utils.label(
                        "Exam Date",
                        13,
                        true
                )
        );

        panel.add(examField);


        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Add Subject",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );


        if (result != JOptionPane.OK_OPTION) {
            return;
        }


        String name =
                subjectField
                        .getText()
                        .trim();


        String classDate =
                classField
                        .getText()
                        .trim();


        String examDate =
                examField
                        .getText()
                        .trim();


        if (name.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a subject name.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // -----------------------------------------------------
        // DATE VALIDATION
        // -----------------------------------------------------

        if (!classDate.isEmpty()
                && !isValidDate(classDate)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Class Date.\n\n"
                            + "Use format: yyyy-MM-dd\n"
                            + "Example: 2026-09-25",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (!examDate.isEmpty()
                && !isValidDate(examDate)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid Exam Date.\n\n"
                            + "Use format: yyyy-MM-dd\n"
                            + "Example: 2026-10-10",
                    "Invalid Date",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // -----------------------------------------------------
        // DUPLICATE SUBJECT CHECK
        // -----------------------------------------------------

        for (Subject subject : user.subjects) {

            if (subject.name.equalsIgnoreCase(name)) {

                JOptionPane.showMessageDialog(
                        this,
                        "This subject already exists.",
                        "Duplicate Subject",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }


        // -----------------------------------------------------
        // CREATE SUBJECT
        // -----------------------------------------------------

        user.subjects.add(
                new Subject(
                        name,
                        classDate,
                        examDate
                )
        );


        // -----------------------------------------------------
        // SAVE
        // -----------------------------------------------------

        DataStore.saveUser(user);

        refreshSubjects();


        JOptionPane.showMessageDialog(
                this,
                "Subject added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =========================================================
    // DATE VALIDATION
    // =========================================================

    private boolean isValidDate(
            String date
    ) {

        if (!date.matches(
                "\\d{4}-\\d{2}-\\d{2}"
        )) {

            return false;
        }

        try {

            java.text.SimpleDateFormat format =
                    new java.text.SimpleDateFormat(
                            "yyyy-MM-dd"
                    );

            format.setLenient(false);

            format.parse(date);

            return true;

        } catch (Exception e) {

            return false;
        }
    }


    // =========================================================
    // TOPIC
    // =========================================================

    private void addTopic() {

        Subject subject =
                getSelectedSubject();

        if (subject == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please add/select a subject first.",
                    "No Subject",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        JTextField topicField =
                Utils.field(
                        "Enter topic name"
                );


        JPanel panel =
                new JPanel(
                        new BorderLayout(0, 8)
                );

        panel.setBorder(
                new EmptyBorder(
                        10,
                        10,
                        10,
                        10
                )
        );


        panel.add(
                Utils.label(
                        "Topic Name",
                        13,
                        true
                ),
                BorderLayout.NORTH
        );


        panel.add(
                topicField,
                BorderLayout.CENTER
        );


        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Add Topic",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );


        if (result != JOptionPane.OK_OPTION) {
            return;
        }


        String topicName =
                topicField
                        .getText()
                        .trim();


        if (topicName.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a topic name.",
                    "Missing Topic",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // -----------------------------------------------------
        // DUPLICATE TOPIC CHECK
        // -----------------------------------------------------

        for (Topic topic : subject.topics) {

            if (topic.name.equalsIgnoreCase(topicName)) {

                JOptionPane.showMessageDialog(
                        this,
                        "This topic already exists.",
                        "Duplicate Topic",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }


        // -----------------------------------------------------
        // AI ANALYSIS
        // -----------------------------------------------------

        JDialog loading =
                createLoadingDialog();


        SwingWorker<Difficulty, Void> worker =
                new SwingWorker<Difficulty, Void>() {

                    @Override
                    protected Difficulty doInBackground()
                            throws Exception {

                        return TopicAnalyzer.analyze(
                                topicName
                        );
                    }


                    @Override
                    protected void done() {

                        loading.dispose();

                        try {

                            Difficulty difficulty =
                                    get();


                            Topic topic =
                                    new Topic(
                                            topicName
                                    );


                            topic.difficulty =
                                    difficulty;

                            topic.completed =
                                    false;


                            subject.topics.add(
                                    topic
                            );


                            DataStore.saveUser(user);

                            refreshTopics();


                            JOptionPane.showMessageDialog(
                                    SubjectTopicPanel.this,
                                    "Topic added successfully!\n\n"
                                            + "AI Difficulty: "
                                            + TopicAnalyzer
                                            .getDifficultyText(
                                                    difficulty
                                            ),
                                    "Topic Analysis Complete",
                                    JOptionPane.INFORMATION_MESSAGE
                            );


                        } catch (Exception ex) {

                            JOptionPane.showMessageDialog(
                                    SubjectTopicPanel.this,
                                    "Topic analysis failed.\n\n"
                                            + ex.getMessage(),
                                    "AI Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                };


        worker.execute();

        loading.setVisible(true);
    }


    // =========================================================
    // REFRESH TOPICS
    // =========================================================

    private void refreshTopics() {

        if (tableModel == null) {
            return;
        }

        tableModel.setRowCount(0);


        Subject subject =
                getSelectedSubject();


        if (subject == null) {

            updateProgress(null);

            return;
        }


        for (int i = 0;
             i < subject.topics.size();
             i++) {

            Topic topic =
                    subject.topics.get(i);


            String status =
                    topic.completed
                            ? "Completed"
                            : "Not Started";


            String priority =
                    calculatePriority(
                            topic
                    );


            tableModel.addRow(
                    new Object[]{
                            topic.name,

                            TopicAnalyzer
                                    .getDifficultyText(
                                    topic.difficulty
                            ),

                            status,

                            priority,

                            topic.completed
                                    ? "☑ Complete"
                                    : "☐ Complete"
                    }
            );
        }


        updateProgress(subject);
    }


    // =========================================================
    // COMPLETE TOPIC
    // =========================================================

    private void toggleTopicComplete(
            int row
    ) {

        Subject subject =
                getSelectedSubject();


        if (subject == null) {
            return;
        }


        if (row < 0
                || row >= subject.topics.size()) {

            return;
        }


        Topic topic =
                subject.topics.get(row);


        topic.completed =
                !topic.completed;


        DataStore.saveUser(user);

        refreshTopics();
    }


    // =========================================================
    // PROGRESS
    // =========================================================

    private void updateProgress(
            Subject subject
    ) {

        if (subject == null
                || subject.topics.isEmpty()) {

            progressBar.setValue(0);

            progressLabel.setText("0%");

            return;
        }


        int completed = 0;


        for (Topic topic : subject.topics) {

            if (topic.completed) {
                completed++;
            }
        }


        int percentage =
                (int) Math.round(
                        completed * 100.0
                                / subject.topics.size()
                );


        progressBar.setValue(
                percentage
        );


        progressLabel.setText(
                percentage + "%"
        );
    }


    // =========================================================
    // DELETE TOPIC
    // =========================================================

    private void deleteTopic() {

        int row =
                topicTable.getSelectedRow();


        if (row < 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a topic first.",
                    "No Topic Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        Subject subject =
                getSelectedSubject();


        if (subject == null) {
            return;
        }


        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Delete this topic?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );


        if (answer != JOptionPane.YES_OPTION) {
            return;
        }


        subject.topics.remove(row);

        DataStore.saveUser(user);

        refreshTopics();
    }


    // =========================================================
    // RESET COURSE
    // =========================================================

    private void resetCourse() {

        Subject subject =
                getSelectedSubject();


        if (subject == null) {
            return;
        }


        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "This will remove all topics from:\n\n"
                                + subject.name
                                + "\n\nContinue?",
                        "Reset Course",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );


        if (answer != JOptionPane.YES_OPTION) {
            return;
        }


        subject.topics.clear();

        DataStore.saveUser(user);

        refreshTopics();


        JOptionPane.showMessageDialog(
                this,
                "Course topics have been reset.",
                "Reset Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // =========================================================
    // GET SELECTED SUBJECT
    // =========================================================

    private Subject getSelectedSubject() {

        if (subjectCombo == null
                || subjectCombo.getSelectedItem() == null) {

            return null;
        }


        String name =
                subjectCombo
                        .getSelectedItem()
                        .toString();


        for (Subject subject : user.subjects) {

            if (subject.name.equals(name)) {

                return subject;
            }
        }


        return null;
    }


    // =========================================================
    // PRIORITY
    // =========================================================

    private String calculatePriority(
            Topic topic
    ) {

        if (topic.difficulty
                == Difficulty.HARD) {

            return "High";

        } else if (
                topic.difficulty
                        == Difficulty.MEDIUM
        ) {

            return "Medium";

        } else {

            return "Low";
        }
    }


    // =========================================================
    // LOADING DIALOG
    // =========================================================

    private JDialog createLoadingDialog() {

        JDialog dialog =
                new JDialog(
                        SwingUtilities
                                .getWindowAncestor(this),
                        "Analyzing Topic",
                        Dialog.ModalityType.APPLICATION_MODAL
                );


        JPanel panel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        panel.setBackground(
                Color.WHITE
        );


        panel.setBorder(
                new EmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );


        JLabel label =
                Utils.label(
                        "AI is analyzing the topic...",
                        14,
                        true
                );


        JProgressBar progress =
                new JProgressBar();

        progress.setIndeterminate(true);


        panel.add(
                label,
                BorderLayout.NORTH
        );


        panel.add(
                progress,
                BorderLayout.CENTER
        );


        dialog.setContentPane(panel);

        dialog.setSize(
                330,
                130
        );

        dialog.setLocationRelativeTo(this);

        return dialog;
    }


    // =========================================================
    // DIFFICULTY RENDERER
    // =========================================================

    private static class DifficultyRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super
                            .getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );


            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );


            if (!isSelected) {

                if ("Easy".equals(value)) {

                    label.setForeground(
                            AppColors.SUCCESS
                    );

                } else if ("Difficult"
                        .equals(value)) {

                    label.setForeground(
                            AppColors.DANGER
                    );

                } else {

                    label.setForeground(
                            AppColors.WARNING
                    );
                }
            }


            return label;
        }
    }


    // =========================================================
    // STATUS RENDERER
    // =========================================================

    private static class StatusRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super
                            .getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );


            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );


            if (!isSelected) {

                if ("Completed".equals(value)) {

                    label.setForeground(
                            AppColors.SUCCESS
                    );

                } else {

                    label.setForeground(
                            AppColors.MUTED
                    );
                }
            }


            return label;
        }
    }


    // =========================================================
    // PRIORITY RENDERER
    // =========================================================

    private static class PriorityRenderer
            extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            JLabel label =
                    (JLabel) super
                            .getTableCellRendererComponent(
                                    table,
                                    value,
                                    isSelected,
                                    hasFocus,
                                    row,
                                    column
                            );


            label.setHorizontalAlignment(
                    SwingConstants.CENTER
            );


            if (!isSelected) {

                if ("High".equals(value)) {

                    label.setForeground(
                            AppColors.DANGER
                    );

                } else if ("Medium"
                        .equals(value)) {

                    label.setForeground(
                            AppColors.WARNING
                    );

                } else {

                    label.setForeground(
                            AppColors.SUCCESS
                    );
                }
            }


            return label;
        }
    }


    // =========================================================
    // ACTION BUTTON RENDERER
    // =========================================================

    private class ActionButtonRenderer
            extends JButton
            implements javax.swing.table.TableCellRenderer {

        public ActionButtonRenderer() {

            setFocusPainted(false);

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            12
                    )
            );
        }


        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean selected,
                boolean focused,
                int row,
                int column
        ) {

            setText(
                    value == null
                            ? "☐ Complete"
                            : value.toString()
            );


            if (value != null
                    && value.toString()
                    .startsWith("☑")) {

                setForeground(
                        AppColors.SUCCESS
                );

            } else {

                setForeground(
                        AppColors.PRIMARY
                );
            }


            setBackground(
                    Color.WHITE
            );


            setBorder(
                    BorderFactory.createLineBorder(
                            AppColors.BORDER
                    )
            );


            return this;
        }
    }


    // =========================================================
    // ACTION BUTTON EDITOR
    // =========================================================

    private class ActionButtonEditor
            extends DefaultCellEditor {

        private final JButton button;

        private int clickedRow;


        public ActionButtonEditor(
                JCheckBox checkBox
        ) {

            super(checkBox);


            button =
                    new JButton();


            button.setFocusPainted(false);


            button.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            12
                    )
            );


            button.addActionListener(
                    this::buttonClicked
            );
        }


        private void buttonClicked(
                ActionEvent event
        ) {

            fireEditingStopped();


            toggleTopicComplete(
                    clickedRow
            );
        }


        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean selected,
                int row,
                int column
        ) {

            clickedRow = row;


            button.setText(
                    value == null
                            ? "☐ Complete"
                            : value.toString()
            );


            if (value != null
                    && value.toString()
                    .startsWith("☑")) {

                button.setForeground(
                        AppColors.SUCCESS
                );

            } else {

                button.setForeground(
                        AppColors.PRIMARY
                );
            }


            button.setBackground(
                    Color.WHITE
            );


            button.setBorder(
                    BorderFactory.createLineBorder(
                            AppColors.BORDER
                    )
            );


            return button;
        }


        @Override
        public Object getCellEditorValue() {

            return button.getText();
        }
    }
}