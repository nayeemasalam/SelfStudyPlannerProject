import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {

    private Utils() {
    }


    // ==========================================
    // LABEL
    // ==========================================

    public static JLabel label(
            String text,
            int size,
            boolean bold
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        bold
                                ? Font.BOLD
                                : Font.PLAIN,
                        size
                )
        );

        label.setForeground(
                AppColors.TEXT
        );

        return label;
    }


    // ==========================================
    // CARD
    // ==========================================

    public static JPanel card() {

        JPanel panel =
                new JPanel();

        panel.setBackground(
                AppColors.CARD
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                AppColors.BORDER
                        ),

                        new EmptyBorder(
                                14,
                                14,
                                14,
                                14
                        )
                )
        );

        return panel;
    }


    // ==========================================
    // BUTTON
    // ==========================================

    public static JButton button(
            String text,
            boolean primary
    ) {

        JButton button =
                new JButton(text);


        // Important:
        // BasicButtonUI ব্যবহার করলে
        // text color ঠিকভাবে দেখা যাবে।

        button.setUI(
                new javax.swing.plaf.basic.BasicButtonUI()
        );


        button.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        13
                )
        );


        if (primary) {

            // Primary button
            button.setForeground(
                    Color.WHITE
            );

            button.setBackground(
                    AppColors.PRIMARY
            );

            button.setBorder(
                    BorderFactory.createCompoundBorder(

                            BorderFactory.createLineBorder(
                                    AppColors.PRIMARY
                            ),

                            new EmptyBorder(
                                    9,
                                    15,
                                    9,
                                    15
                            )
                    )
            );

        } else {

            // Normal button
            button.setForeground(
                    AppColors.PRIMARY
            );

            button.setBackground(
                    Color.WHITE
            );

            button.setBorder(
                    BorderFactory.createCompoundBorder(

                            BorderFactory.createLineBorder(
                                    AppColors.BORDER
                            ),

                            new EmptyBorder(
                                    9,
                                    15,
                                    9,
                                    15
                            )
                    )
            );
        }


        button.setOpaque(true);

        button.setContentAreaFilled(true);

        button.setFocusPainted(false);

        button.setFocusable(false);


        // Text যেন disabled/gray না হয়ে যায়
        button.setEnabled(true);


        return button;
    }


    // ==========================================
    // TEXT FIELD
    // ==========================================

    public static JTextField field(
            String placeholder
    ) {

        JTextField field =
                new JTextField();

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        field.setForeground(
                AppColors.TEXT
        );

        field.setBackground(
                Color.WHITE
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                AppColors.BORDER
                        ),

                        new EmptyBorder(
                                8,
                                10,
                                8,
                                10
                        )
                )
        );

        field.setToolTipText(
                placeholder
        );

        return field;
    }


    // ==========================================
    // TODAY
    // ==========================================

    public static String today() {

        return new SimpleDateFormat(
                "yyyy-MM-dd"
        ).format(
                new Date()
        );
    }


    // ==========================================
    // TITLED PANEL
    // ==========================================

    public static JPanel titled(
            String title
    ) {

        JPanel panel =
                card();

        panel.setLayout(
                new BorderLayout(
                        8,
                        10
                )
        );


        panel.add(
                label(
                        title,
                        16,
                        true
                ),
                BorderLayout.NORTH
        );


        return panel;
    }
}