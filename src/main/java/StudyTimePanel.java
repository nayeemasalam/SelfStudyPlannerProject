
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudyTimePanel extends JPanel {

    private final User user;

    private final JPanel slotListPanel;
    private final JLabel totalTimeLabel;

    private final JTextField startField;
    private final JTextField endField;

    private static final File STORAGE_FILE =
            new File("study_time_" + System.getProperty("user.name") + ".dat");


    public StudyTimePanel(User user) {

        this.user = user;

        setLayout(new BorderLayout());
        setBackground(AppColors.BG);
        setBorder(new EmptyBorder(25, 30, 25, 30));


        // =====================================================
        // HEADER
        // =====================================================

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Available Study Time");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(AppColors.TEXT);

        JLabel subtitle = new JLabel(
                "Tell us when you are free. Smart Study Planner will use these slots."
        );
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(AppColors.MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);

        add(header, BorderLayout.NORTH);


        // =====================================================
        // MAIN CONTENT
        // =====================================================

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(25, 0, 0, 0));


        // =====================================================
        // ADD TIME CARD
        // =====================================================

        JPanel addCard = Utils.card();
        addCard.setLayout(new BorderLayout(15, 15));

        JLabel addTitle = Utils.label(
                "Add Your Free Time",
                17,
                true
        );

        addCard.add(addTitle, BorderLayout.NORTH);


        JPanel inputPanel = new JPanel(new FlowLayout(
                FlowLayout.LEFT,
                10,
                5
        ));
        inputPanel.setOpaque(false);


        JLabel startLabel = Utils.label(
                "Start",
                13,
                true
        );

        startField = Utils.field("Example: 09:00 AM");
        startField.setPreferredSize(new Dimension(130, 40));


        JLabel toLabel = Utils.label(
                "to",
                13,
                false
        );


        JLabel endLabel = Utils.label(
                "End",
                13,
                true
        );

        endField = Utils.field("Example: 10:00 AM");
        endField.setPreferredSize(new Dimension(130, 40));


        JButton addButton = Utils.button(
                "+ Add Time",
                true
        );

        addButton.setPreferredSize(
                new Dimension(125, 40)
        );

        addButton.addActionListener(e -> addTimeSlot());


        inputPanel.add(startLabel);
        inputPanel.add(startField);
        inputPanel.add(toLabel);
        inputPanel.add(endLabel);
        inputPanel.add(endField);
        inputPanel.add(addButton);

        addCard.add(inputPanel, BorderLayout.CENTER);


        // =====================================================
        // TOTAL TIME
        // =====================================================

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(AppColors.PRIMARY_LIGHT);
        totalPanel.setBorder(
                new EmptyBorder(12, 15, 12, 15)
        );

        JLabel totalTitle = new JLabel(
                "Total Available Study Time"
        );

        totalTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 13)
        );

        totalTitle.setForeground(AppColors.TEXT);


        totalTimeLabel = new JLabel("0 hours");

        totalTimeLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 16)
        );

        totalTimeLabel.setForeground(
                AppColors.PRIMARY
        );

        totalPanel.add(
                totalTitle,
                BorderLayout.WEST
        );

        totalPanel.add(
                totalTimeLabel,
                BorderLayout.EAST
        );


        JPanel topSection = new JPanel(
                new BorderLayout(0, 15)
        );

        topSection.setOpaque(false);

        topSection.add(
                addCard,
                BorderLayout.NORTH
        );

        topSection.add(
                totalPanel,
                BorderLayout.CENTER
        );


        // =====================================================
        // SLOT LIST CARD
        // =====================================================

        JPanel listCard = Utils.card();

        listCard.setLayout(
                new BorderLayout(10, 15)
        );


        JLabel listTitle = Utils.label(
                "Your Available Time Slots",
                17,
                true
        );

        listCard.add(
                listTitle,
                BorderLayout.NORTH
        );


        slotListPanel = new JPanel();

        slotListPanel.setLayout(
                new BoxLayout(
                        slotListPanel,
                        BoxLayout.Y_AXIS
                )
        );

        slotListPanel.setBackground(
                AppColors.CARD
        );


        JScrollPane scrollPane =
                new JScrollPane(slotListPanel);

        scrollPane.setBorder(null);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(12);

        listCard.add(
                scrollPane,
                BorderLayout.CENTER
        );


        mainPanel.add(
                topSection,
                BorderLayout.NORTH
        );

        mainPanel.add(
                listCard,
                BorderLayout.CENTER
        );


        add(
                mainPanel,
                BorderLayout.CENTER
        );


        loadSlots();
    }


    // =========================================================
    // ADD TIME SLOT
    // =========================================================

    private void addTimeSlot() {

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


        int start =
                parseTime(startText);

        int end =
                parseTime(endText);


        if (start == -1 || end == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please use a valid time format.\n\n"
                            + "Examples:\n"
                            + "09:00 AM\n"
                            + "10:30 AM\n"
                            + "2:00 PM",
                    "Invalid Time",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        if (end <= start) {

            JOptionPane.showMessageDialog(
                    this,
                    "End time must be after start time.",
                    "Invalid Time",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        List<TimeSlot> slots =
                loadSlotList();


        // Check overlapping slots
        for (TimeSlot slot : slots) {

            if (start < slot.endMinutes
                    && end > slot.startMinutes) {

                JOptionPane.showMessageDialog(
                        this,
                        "This time overlaps with another slot.",
                        "Overlapping Time",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        }


        slots.add(
                new TimeSlot(start, end)
        );


        slots.sort(
                Comparator.comparingInt(
                        s -> s.startMinutes
                )
        );


        saveSlotList(slots);


        startField.setText("");
        endField.setText("");

        refreshSlots();
    }


    // =========================================================
    // DISPLAY SLOTS
    // =========================================================

    private void refreshSlots() {

        slotListPanel.removeAll();

        List<TimeSlot> slots =
                loadSlotList();


        if (slots.isEmpty()) {

            JLabel emptyLabel =
                    Utils.label(
                            "No available time added yet.",
                            14,
                            false
                    );

            emptyLabel.setForeground(
                    AppColors.MUTED
            );

            JPanel emptyPanel =
                    new JPanel(new GridBagLayout());

            emptyPanel.setBackground(
                    AppColors.CARD
            );

            emptyPanel.add(emptyLabel);

            slotListPanel.add(emptyPanel);

        } else {

            for (int i = 0; i < slots.size(); i++) {

                TimeSlot slot = slots.get(i);

                slotListPanel.add(
                        createSlotCard(slot, i)
                );

                slotListPanel.add(
                        Box.createVerticalStrut(10)
                );
            }
        }


        updateTotalTime(slots);


        slotListPanel.revalidate();
        slotListPanel.repaint();
    }


    // =========================================================
    // SLOT CARD
    // =========================================================

    private JPanel createSlotCard(
            TimeSlot slot,
            int index
    ) {

        JPanel card = new JPanel(
                new BorderLayout(15, 5)
        );

        card.setBackground(
                AppColors.CARD
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                AppColors.BORDER
                        ),
                        new EmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
                )
        );

        card.setMaximumSize(
                new Dimension(
                        Integer.MAX_VALUE,
                        65
                )
        );


        // Left
        JPanel left = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        10,
                        0
                )
        );

        left.setOpaque(false);


        JLabel icon = new JLabel("⏰");

        icon.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        20
                )
        );


        JLabel timeLabel = new JLabel(
                formatTime(slot.startMinutes)
                        + "  -  "
                        + formatTime(slot.endMinutes)
        );

        timeLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        15
                )
        );

        timeLabel.setForeground(
                AppColors.TEXT
        );


        JLabel durationLabel =
                new JLabel(
                        "("
                                + formatDuration(
                                slot.getDurationMinutes()
                        )
                                + ")"
                );

        durationLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        durationLabel.setForeground(
                AppColors.MUTED
        );


        left.add(icon);
        left.add(timeLabel);
        left.add(durationLabel);


        // Delete
        JButton deleteButton =
                Utils.button(
                        "Delete",
                        false
                );

        deleteButton.setForeground(
                AppColors.DANGER
        );


        deleteButton.addActionListener(e -> {

            List<TimeSlot> slots =
                    loadSlotList();

            if (index >= 0
                    && index < slots.size()) {

                slots.remove(index);

                saveSlotList(slots);

                refreshSlots();
            }
        });


        card.add(
                left,
                BorderLayout.CENTER
        );

        card.add(
                deleteButton,
                BorderLayout.EAST
        );


        return card;
    }


    // =========================================================
    // TOTAL TIME
    // =========================================================

    private void updateTotalTime(
            List<TimeSlot> slots
    ) {

        int totalMinutes = 0;

        for (TimeSlot slot : slots) {

            totalMinutes +=
                    slot.getDurationMinutes();
        }


        totalTimeLabel.setText(
                formatDuration(totalMinutes)
        );
    }


    // =========================================================
    // TIME PARSER
    // =========================================================

    private int parseTime(String text) {

        try {

            text = text.trim()
                    .toUpperCase()
                    .replace(".", "");

            boolean pm =
                    text.endsWith("PM");

            boolean am =
                    text.endsWith("AM");


            if (!am && !pm) {
                return -1;
            }


            text = text
                    .replace("AM", "")
                    .replace("PM", "")
                    .trim();


            String[] parts =
                    text.split(":");


            if (parts.length != 2) {
                return -1;
            }


            int hour =
                    Integer.parseInt(
                            parts[0].trim()
                    );

            int minute =
                    Integer.parseInt(
                            parts[1].trim()
                    );


            if (hour < 1
                    || hour > 12
                    || minute < 0
                    || minute > 59) {

                return -1;
            }


            if (hour == 12) {
                hour = 0;
            }


            if (pm) {
                hour += 12;
            }


            return hour * 60 + minute;

        } catch (Exception e) {

            return -1;
        }
    }


    // =========================================================
    // FORMAT TIME
    // =========================================================

    private String formatTime(int minutes) {

        int hour =
                minutes / 60;

        int minute =
                minutes % 60;


        String period =
                hour >= 12
                        ? "PM"
                        : "AM";


        int displayHour =
                hour % 12;

        if (displayHour == 0) {
            displayHour = 12;
        }


        return String.format(
                "%d:%02d %s",
                displayHour,
                minute,
                period
        );
    }


    // =========================================================
    // FORMAT DURATION
    // =========================================================

    private String formatDuration(
            int minutes
    ) {

        int hours =
                minutes / 60;

        int mins =
                minutes % 60;


        if (hours == 0) {

            return mins + " minutes";

        }

        if (mins == 0) {

            return hours
                    + (hours == 1
                    ? " hour"
                    : " hours");
        }


        return hours
                + (hours == 1
                ? " hour "
                : " hours ")
                + mins
                + " minutes";
    }


    // =========================================================
    // LOAD
    // =========================================================

    private void loadSlots() {
        refreshSlots();
    }


    private List<TimeSlot> loadSlotList() {

        if (!STORAGE_FILE.exists()) {
            return new ArrayList<>();
        }


        try {

            ObjectInputStream input =
                    new ObjectInputStream(
                            new FileInputStream(
                                    STORAGE_FILE
                            )
                    );


            Object object =
                    input.readObject();


            input.close();


            if (object instanceof List<?>) {

                return (List<TimeSlot>) object;
            }


        } catch (Exception e) {

            System.out.println(
                    "Could not load study time."
            );
        }


        return new ArrayList<>();
    }


    // =========================================================
    // SAVE
    // =========================================================

    private void saveSlotList(
            List<TimeSlot> slots
    ) {

        try {

            ObjectOutputStream output =
                    new ObjectOutputStream(
                            new FileOutputStream(
                                    STORAGE_FILE
                            )
                    );


            output.writeObject(slots);

            output.flush();
            output.close();


        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not save study time.",
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // TIME SLOT MODEL
    // =========================================================

    static class TimeSlot
            implements Serializable {

        int startMinutes;
        int endMinutes;


        TimeSlot(
                int startMinutes,
                int endMinutes
        ) {

            this.startMinutes =
                    startMinutes;

            this.endMinutes =
                    endMinutes;
        }


        int getDurationMinutes() {

            return endMinutes
                    - startMinutes;
        }
    }
}