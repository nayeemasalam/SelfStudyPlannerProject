import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class StudyPlanPanel extends JPanel {

    private final User user;
    private final List<User> users;

    private final List<TimeSlot> availableSlots = new ArrayList<>();

    private JTable scheduleTable;
    private DefaultTableModel scheduleModel;

    private JLabel totalTimeLabel;
    private JLabel statusLabel;

    private JPanel slotListPanel;

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd");

    private final SimpleDateFormat timeFormat =
            new SimpleDateFormat("hh:mm a");

    public StudyPlanPanel(User user, List<User> users) {

        this.user = user;
        this.users = users;

        setLayout(new BorderLayout());
        setBackground(AppColors.BG);

        createUI();
    }


    // =========================================================
    // MAIN UI
    // =========================================================

    private void createUI() {

        JPanel main = new JPanel(new BorderLayout(18, 18));
        main.setBackground(AppColors.BG);
        main.setBorder(new EmptyBorder(20, 22, 20, 22));

        main.add(createHeader(), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 18, 0));
        center.setBackground(AppColors.BG);

        center.add(createAvailableTimeCard());
        center.add(createSmartAnalysisCard());

        main.add(center, BorderLayout.CENTER);

        main.add(createScheduleCard(), BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);
    }


    // =========================================================
    // HEADER
    // =========================================================

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppColors.BG);

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = Utils.label(
                "Smart Study Plan",
                25,
                true
        );

        JLabel subtitle = new JLabel(
                "Tell us when you are free. We will decide what you should study."
        );

        subtitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 13)
        );

        subtitle.setForeground(AppColors.MUTED);

        left.add(title);
        left.add(Box.createVerticalStrut(5));
        left.add(subtitle);

        panel.add(left, BorderLayout.WEST);

        JButton generateButton =
                Utils.button("✨ Generate Smart Plan", true);

        generateButton.addActionListener(e ->
                generateSmartPlan()
        );

        panel.add(generateButton, BorderLayout.EAST);

        return panel;
    }


    // =========================================================
    // AVAILABLE TIME CARD
    // =========================================================

    private JPanel createAvailableTimeCard() {

        JPanel card = Utils.card();

        card.setLayout(new BorderLayout(10, 12));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(
                new BoxLayout(titlePanel, BoxLayout.Y_AXIS)
        );

        JLabel title = Utils.label(
                "⏰ Your Available Time",
                18,
                true
        );

        JLabel description = new JLabel(
                "Add the times when you are free to study."
        );

        description.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        description.setForeground(AppColors.MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(3));
        titlePanel.add(description);

        header.add(titlePanel, BorderLayout.WEST);

        JButton addButton =
                Utils.button("+ Add Time", true);

        addButton.addActionListener(e ->
                addTimeSlot()
        );

        header.add(addButton, BorderLayout.EAST);

        card.add(header, BorderLayout.NORTH);

        slotListPanel = new JPanel();

        slotListPanel.setLayout(
                new BoxLayout(
                        slotListPanel,
                        BoxLayout.Y_AXIS
                )
        );

        slotListPanel.setBackground(AppColors.CARD);

        JScrollPane scrollPane =
                new JScrollPane(slotListPanel);

        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(12);

        card.add(
                scrollPane,
                BorderLayout.CENTER
        );

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        totalTimeLabel = Utils.label(
                "Total Available: 0 hours",
                13,
                true
        );

        bottom.add(
                totalTimeLabel,
                BorderLayout.WEST
        );

        statusLabel = Utils.label(
                "Add your free times first.",
                12,
                false
        );

        statusLabel.setForeground(AppColors.MUTED);

        bottom.add(
                statusLabel,
                BorderLayout.EAST
        );

        card.add(
                bottom,
                BorderLayout.SOUTH
        );

        refreshTimeSlots();

        return card;
    }


    // =========================================================
    // ADD TIME SLOT
    // =========================================================

    private void addTimeSlot() {

        JTextField startField =
                Utils.field("Example: 09:00 AM");

        JTextField endField =
                Utils.field("Example: 10:00 AM");

        JPanel panel = new JPanel(
                new GridLayout(2, 2, 10, 10)
        );

        panel.setBorder(
                new EmptyBorder(10, 5, 5, 5)
        );

        panel.add(
                Utils.label("Start Time", 13, true)
        );

        panel.add(
                Utils.label("End Time", 13, true)
        );

        panel.add(startField);
        panel.add(endField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Available Study Time",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String startText =
                startField.getText().trim();

        String endText =
                endField.getText().trim();

        if (startText.isEmpty()
                || endText.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter both start and end time.",
                    "Missing Time",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Date start =
                    timeFormat.parse(
                            normalizeTime(startText)
                    );

            Date end =
                    timeFormat.parse(
                            normalizeTime(endText)
                    );

            if (!end.after(start)) {

                JOptionPane.showMessageDialog(
                        this,
                        "End time must be after start time.",
                        "Invalid Time",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            TimeSlot slot =
                    new TimeSlot(start, end);

            availableSlots.add(slot);

            sortTimeSlots();

            refreshTimeSlots();

            statusLabel.setText(
                    "Time added successfully."
            );

        } catch (ParseException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Use time format like:\n\n09:00 AM\n02:30 PM\n08:00 PM",
                    "Invalid Time Format",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // NORMALIZE TIME
    // =========================================================

    private String normalizeTime(String value) {

        value = value.trim()
                .toUpperCase();

        if (!value.contains("AM")
                && !value.contains("PM")) {

            value = value + " AM";
        }

        return value;
    }


    // =========================================================
    // REFRESH AVAILABLE TIMES
    // =========================================================

    private void refreshTimeSlots() {

        if (slotListPanel == null) {
            return;
        }

        slotListPanel.removeAll();

        if (availableSlots.isEmpty()) {

            JLabel empty =
                    Utils.label(
                            "No available time added yet.",
                            13,
                            false
                    );

            empty.setForeground(
                    AppColors.MUTED
            );

            slotListPanel.add(
                    Box.createVerticalStrut(15)
            );

            slotListPanel.add(empty);

        } else {

            for (TimeSlot slot : availableSlots) {

                slotListPanel.add(
                        createTimeSlotRow(slot)
                );

                slotListPanel.add(
                        Box.createVerticalStrut(8)
                );
            }
        }

        updateTotalTime();

        slotListPanel.revalidate();
        slotListPanel.repaint();
    }


    // =========================================================
    // TIME SLOT ROW
    // =========================================================

    private JPanel createTimeSlotRow(TimeSlot slot) {

        JPanel row = new JPanel(
                new BorderLayout(10, 0)
        );

        row.setBackground(
                AppColors.PRIMARY_LIGHT
        );

        row.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AppColors.BORDER
                        ),
                        new EmptyBorder(
                                10, 12, 10, 12
                        )
                )
        );

        JLabel timeLabel =
                Utils.label(
                        formatTime(slot.start)
                                + "  →  "
                                + formatTime(slot.end),
                        14,
                        true
                );

        row.add(
                timeLabel,
                BorderLayout.CENTER
        );

        JLabel duration =
                Utils.label(
                        slot.getDurationText(),
                        12,
                        false
                );

        duration.setForeground(
                AppColors.PRIMARY
        );

        row.add(
                duration,
                BorderLayout.EAST
        );

        JButton delete =
                Utils.button("×", false);

        delete.setMargin(
                new Insets(2, 8, 2, 8)
        );

        delete.addActionListener(e -> {

            availableSlots.remove(slot);

            refreshTimeSlots();
        });

        row.add(
                delete,
                BorderLayout.WEST
        );

        return row;
    }


    // =========================================================
    // TOTAL AVAILABLE TIME
    // =========================================================

    private void updateTotalTime() {

        int totalMinutes = 0;

        for (TimeSlot slot : availableSlots) {
            totalMinutes += slot.getDurationMinutes();
        }

        int hours =
                totalMinutes / 60;

        int minutes =
                totalMinutes % 60;

        String text =
                "Total Available: "
                        + hours
                        + "h "
                        + minutes
                        + "m";

        totalTimeLabel.setText(text);
    }


    // =========================================================
    // SMART ANALYSIS CARD
    // =========================================================

    private JPanel createSmartAnalysisCard() {

        JPanel card = Utils.card();

        card.setLayout(
                new BorderLayout(10, 12)
        );

        JLabel title =
                Utils.label(
                        "🧠 Smart Topic Analysis",
                        18,
                        true
                );

        card.add(
                title,
                BorderLayout.NORTH
        );

        JPanel content =
                new JPanel();

        content.setOpaque(false);

        content.setLayout(
                new BoxLayout(
                        content,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel info =
                new JLabel(
                        "<html>"
                                + "The system will analyze:<br><br>"
                                + "• Exam date<br>"
                                + "• Topic difficulty<br>"
                                + "• Completion status<br>"
                                + "• Remaining study time<br>"
                                + "<br>"
                                + "Then it will decide which topic "
                                + "should be studied first."
                                + "</html>"
                );

        info.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        info.setForeground(
                AppColors.TEXT
        );

        content.add(info);

        content.add(
                Box.createVerticalStrut(18)
        );

        JLabel ruleTitle =
                Utils.label(
                        "How the planner decides",
                        14,
                        true
                );

        content.add(ruleTitle);

        content.add(
                Box.createVerticalStrut(8)
        );

        content.add(
                createRule(
                        "1",
                        "Earlier exam",
                        "Higher priority"
                )
        );

        content.add(
                createRule(
                        "2",
                        "Hard topic",
                        "Needs better focus time"
                )
        );

        content.add(
                createRule(
                        "3",
                        "Not completed",
                        "Gets preference"
                )
        );

        content.add(
                createRule(
                        "4",
                        "Easy topic",
                        "Suitable for lighter slots"
                )
        );

        card.add(
                content,
                BorderLayout.CENTER
        );

        return card;
    }


    // =========================================================
    // RULE ROW
    // =========================================================

    private JPanel createRule(
            String number,
            String title,
            String description
    ) {

        JPanel row =
                new JPanel(
                        new BorderLayout(10, 0)
                );

        row.setOpaque(false);

        JLabel numberLabel =
                new JLabel(number);

        numberLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        numberLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );

        numberLabel.setForeground(
                Color.WHITE
        );

        numberLabel.setOpaque(true);

        numberLabel.setBackground(
                AppColors.PRIMARY
        );

        numberLabel.setPreferredSize(
                new Dimension(27, 27)
        );

        row.add(
                numberLabel,
                BorderLayout.WEST
        );

        JPanel text =
                new JPanel();

        text.setOpaque(false);

        text.setLayout(
                new BoxLayout(
                        text,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel t =
                Utils.label(
                        title,
                        13,
                        true
                );

        JLabel d =
                Utils.label(
                        description,
                        11,
                        false
                );

        d.setForeground(
                AppColors.MUTED
        );

        text.add(t);
        text.add(d);

        row.add(
                text,
                BorderLayout.CENTER
        );

        row.setBorder(
                new EmptyBorder(5, 0, 5, 0)
        );

        return row;
    }


    // =========================================================
    // SCHEDULE CARD
    // =========================================================

    private JPanel createScheduleCard() {

        JPanel card = Utils.card();

        card.setLayout(
                new BorderLayout(10, 12)
        );

        JLabel title =
                Utils.label(
                        "📅 Recommended Study Schedule",
                        18,
                        true
                );

        card.add(
                title,
                BorderLayout.NORTH
        );

        String[] columns = {
                "Available Time",
                "Subject",
                "Topic",
                "Difficulty",
                "Exam",
                "Why?",
                "Status",
                "Action"
        };

        scheduleModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return column == 7;
                    }
                };

        scheduleTable =
                new JTable(scheduleModel);

        scheduleTable.setRowHeight(42);

        scheduleTable.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        12
                )
        );

        scheduleTable.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                12
                        )
                );

        scheduleTable.getTableHeader()
                .setBackground(
                        AppColors.PRIMARY_LIGHT
                );

        scheduleTable.setGridColor(
                AppColors.BORDER
        );

        scheduleTable.setSelectionBackground(
                AppColors.PRIMARY_LIGHT
        );


        // =====================================================
        // ACTION COLUMN
        // =====================================================

        scheduleTable.getColumnModel()
                .getColumn(7)
                .setPreferredWidth(100);

        scheduleTable.getColumnModel()
                .getColumn(7)
                .setMinWidth(100);

        scheduleTable.getColumnModel()
                .getColumn(7)
                .setMaxWidth(110);


        scheduleTable.getColumnModel()
                .getColumn(7)
                .setCellRenderer(
                        new DeleteButtonRenderer()
                );

        scheduleTable.getColumnModel()
                .getColumn(7)
                .setCellEditor(
                        new DeleteButtonEditor(
                                new JCheckBox()
                        )
                );


        JScrollPane scroll =
                new JScrollPane(
                        scheduleTable
                );

        scroll.setPreferredSize(
                new Dimension(
                        900,
                        250
                )
        );

        card.add(
                scroll,
                BorderLayout.CENTER
        );

        return card;
    }


    // =========================================================
    // GENERATE SMART PLAN
    // =========================================================

    private void generateSmartPlan() {

        if (availableSlots.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "First add your available study times.",
                    "No Available Time",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        List<TopicCandidate> candidates =
                collectTopics();

        if (candidates.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No pending topics found.\n"
                            + "Please add subjects and topics first.",
                    "No Topics",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        candidates.sort(
                Comparator.comparingInt(
                        (TopicCandidate c) ->
                                c.priorityScore
                ).reversed()
        );

        scheduleModel.setRowCount(0);

        int topicIndex = 0;

        for (TimeSlot slot : availableSlots) {

            int availableMinutes =
                    slot.getDurationMinutes();

            int usedMinutes = 0;

            while (
                    usedMinutes + 30
                            <= availableMinutes
                            && topicIndex < candidates.size()
            ) {

                TopicCandidate candidate =
                        candidates.get(
                                topicIndex
                        );

                int sessionMinutes =
                        chooseSessionLength(
                                candidate,
                                availableMinutes
                                        - usedMinutes
                        );

                if (sessionMinutes <= 0) {
                    break;
                }

                String why =
                        getReason(candidate);

                Date sessionStart =
                        addMinutes(
                                slot.start,
                                usedMinutes
                        );

                Date sessionEnd =
                        addMinutes(
                                slot.start,
                                usedMinutes
                                        + sessionMinutes
                        );

                scheduleModel.addRow(
                        new Object[]{
                                formatTime(sessionStart)
                                        + " - "
                                        + formatTime(sessionEnd),

                                candidate.subject.name,

                                candidate.topic.name,

                                candidate.topic.difficulty,

                                getExamText(
                                        candidate.subject
                                ),

                                why,

                                "Pending",

                                "Delete"
                        }
                );

                usedMinutes += sessionMinutes;

                topicIndex++;
            }
        }

        if (scheduleModel.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "There is not enough available time "
                            + "to create a study plan.",
                    "Not Enough Time",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        statusLabel.setText(
                "Smart plan generated successfully."
        );

        statusLabel.setForeground(
                AppColors.SUCCESS
        );
    }


    // =========================================================
    // DELETE RECOMMENDATION
    // =========================================================

    private void deleteRecommendation(int row) {

        if (row < 0
                || row >= scheduleModel.getRowCount()) {
            return;
        }

        String topic =
                String.valueOf(
                        scheduleModel.getValueAt(
                                row,
                                2
                        )
                );

        int answer =
                JOptionPane.showConfirmDialog(
                        this,
                        "Remove \"" + topic
                                + "\" from the recommended plan?",
                        "Remove Recommendation",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (answer == JOptionPane.YES_OPTION) {

            /*
             * IMPORTANT:
             * This removes ONLY the recommendation row.
             * It does NOT delete the actual Topic or Subject.
             */

            scheduleModel.removeRow(row);

            statusLabel.setText(
                    "Recommendation removed from this plan."
            );

            statusLabel.setForeground(
                    AppColors.MUTED
            );
        }
    }


    // =========================================================
    // DELETE BUTTON RENDERER
    // =========================================================

    private class DeleteButtonRenderer
            extends JButton
            implements TableCellRenderer {

        public DeleteButtonRenderer() {

            setText("Delete");

            setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            setForeground(
                    AppColors.DANGER
            );

            setBackground(
                    AppColors.DANGER_LIGHT
            );

            setFocusPainted(false);

            setBorder(
                    BorderFactory.createLineBorder(
                            AppColors.DANGER
                    )
            );
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column
        ) {

            setText("Delete");

            return this;
        }
    }


    // =========================================================
    // DELETE BUTTON EDITOR
    // =========================================================

    private class DeleteButtonEditor
            extends AbstractCellEditor
            implements TableCellEditor {

        private final JButton button;

        private int currentRow = -1;

        public DeleteButtonEditor(
                JCheckBox checkBox
        ) {

            button =
                    new JButton("Delete");

            button.setFont(
                    new Font(
                            "Segoe UI",
                            Font.BOLD,
                            11
                    )
            );

            button.setForeground(
                    AppColors.DANGER
            );

            button.setBackground(
                    AppColors.DANGER_LIGHT
            );

            button.setFocusPainted(false);

            button.setBorder(
                    BorderFactory.createLineBorder(
                            AppColors.DANGER
                    )
            );

            button.addActionListener(
                    (ActionEvent e) -> {

                        int row =
                                scheduleTable
                                        .convertRowIndexToModel(
                                                currentRow
                                        );

                        fireEditingStopped();

                        deleteRecommendation(row);
                    }
            );
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column
        ) {

            currentRow = row;

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Delete";
        }
    }


    // =========================================================
    // COLLECT TOPICS
    // =========================================================

    private List<TopicCandidate> collectTopics() {

        List<TopicCandidate> list =
                new ArrayList<>();

        for (Subject subject : user.subjects) {

            if (subject.topics == null) {
                continue;
            }

            for (Topic topic : subject.topics) {

                if (topic.completed) {
                    continue;
                }

                int score =
                        calculatePriority(
                                subject,
                                topic
                        );

                list.add(
                        new TopicCandidate(
                                subject,
                                topic,
                                score
                        )
                );
            }
        }

        return list;
    }


    // =========================================================
    // PRIORITY CALCULATION
    // =========================================================

    private int calculatePriority(
            Subject subject,
            Topic topic
    ) {

        int score = 0;

        // -----------------------------------------------------
        // EXAM URGENCY
        // -----------------------------------------------------

        int examDays =
                getDaysUntilExam(
                        subject.examDate
                );

        if (examDays <= 1) {
            score += 100;

        } else if (examDays <= 3) {
            score += 85;

        } else if (examDays <= 7) {
            score += 70;

        } else if (examDays <= 14) {
            score += 50;

        } else {
            score += 25;
        }


        // -----------------------------------------------------
        // DIFFICULTY
        // -----------------------------------------------------

        if (topic.difficulty
                == Difficulty.HARD) {

            score += 40;

        } else if (
                topic.difficulty
                        == Difficulty.MEDIUM
        ) {

            score += 25;

        } else {

            score += 10;
        }


        // -----------------------------------------------------
        // NOT COMPLETED
        // -----------------------------------------------------

        if (!topic.completed) {
            score += 20;
        }

        return score;
    }


    // =========================================================
    // SESSION LENGTH
    // =========================================================

    private int chooseSessionLength(
            TopicCandidate candidate,
            int remainingMinutes
    ) {

        int preferred;

        if (candidate.topic.difficulty
                == Difficulty.HARD) {

            preferred = 60;

        } else if (
                candidate.topic.difficulty
                        == Difficulty.MEDIUM
        ) {

            preferred = 45;

        } else {

            preferred = 30;
        }

        if (remainingMinutes >= preferred) {
            return preferred;
        }

        if (remainingMinutes >= 30) {
            return 30;
        }

        return 0;
    }


    // =========================================================
    // WHY THIS TOPIC?
    // =========================================================

    private String getReason(
            TopicCandidate candidate
    ) {

        int days =
                getDaysUntilExam(
                        candidate.subject.examDate
                );

        if (days <= 3
                && candidate.topic.difficulty
                == Difficulty.HARD) {

            return "Urgent + Hard";
        }

        if (days <= 3) {
            return "Exam very near";
        }

        if (candidate.topic.difficulty
                == Difficulty.HARD) {

            return "Hard topic";
        }

        if (candidate.topic.difficulty
                == Difficulty.MEDIUM) {

            return "Medium topic";
        }

        return "Easy topic";
    }


    // =========================================================
    // EXAM TEXT
    // =========================================================

    private String getExamText(
            Subject subject
    ) {

        if (subject.examDate == null
                || subject.examDate.trim().isEmpty()) {

            return "No date";
        }

        int days =
                getDaysUntilExam(
                        subject.examDate
                );

        if (days < 0) {
            return "Passed";
        }

        if (days == 0) {
            return "Today";
        }

        if (days == 1) {
            return "Tomorrow";
        }

        return days + " days";
    }


    // =========================================================
    // DAYS UNTIL EXAM
    // =========================================================

    private int getDaysUntilExam(
            String examDate
    ) {

        if (examDate == null
                || examDate.trim().isEmpty()) {

            return 30;
        }

        try {

            Date exam =
                    dateFormat.parse(
                            examDate
                    );

            Date today =
                    dateFormat.parse(
                            dateFormat.format(
                                    new Date()
                            )
                    );

            long difference =
                    exam.getTime()
                            - today.getTime();

            return (int) (
                    difference
                            / (1000 * 60 * 60 * 24)
            );

        } catch (Exception e) {

            return 30;
        }
    }


    // =========================================================
    // TIME HELPERS
    // =========================================================

    private String formatTime(Date date) {

        return timeFormat.format(date);
    }


    private Date addMinutes(
            Date date,
            int minutes
    ) {

        Calendar calendar =
                Calendar.getInstance();

        calendar.setTime(date);

        calendar.add(
                Calendar.MINUTE,
                minutes
        );

        return calendar.getTime();
    }


    private void sortTimeSlots() {

        availableSlots.sort(
                Comparator.comparing(
                        slot -> slot.start
                )
        );
    }


    // =========================================================
    // INNER CLASS: TIME SLOT
    // =========================================================

    private static class TimeSlot {

        Date start;
        Date end;

        TimeSlot(
                Date start,
                Date end
        ) {

            this.start = start;
            this.end = end;
        }

        int getDurationMinutes() {

            long diff =
                    end.getTime()
                            - start.getTime();

            return (int) (
                    diff
                            / (1000 * 60)
            );
        }

        String getDurationText() {

            int minutes =
                    getDurationMinutes();

            int hours =
                    minutes / 60;

            int remaining =
                    minutes % 60;

            if (hours == 0) {
                return remaining + " min";
            }

            if (remaining == 0) {

                return hours
                        + (hours == 1
                        ? " hour"
                        : " hours");
            }

            return hours
                    + "h "
                    + remaining
                    + "m";
        }
    }


    // =========================================================
    // INNER CLASS: TOPIC CANDIDATE
    // =========================================================

    private static class TopicCandidate {

        Subject subject;
        Topic topic;
        int priorityScore;

        TopicCandidate(
                Subject subject,
                Topic topic,
                int priorityScore
        ) {

            this.subject = subject;
            this.topic = topic;
            this.priorityScore =
                    priorityScore;
        }
    }
}