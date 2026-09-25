import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class RegisterFrame extends JFrame {

    private JTextField fullNameField;
    private JTextField usernameField;
    private JTextField emailField;

    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    private final List<User> users;


    // =========================================
    // COLORS
    // =========================================

    private static final Color BG =
            new Color(244, 246, 255);

    private static final Color CARD =
            Color.WHITE;

    private static final Color FIELD_BG =
            new Color(249, 250, 254);

    private static final Color PRIMARY =
            new Color(91, 78, 220);

    private static final Color TEXT =
            new Color(40, 45, 70);

    private static final Color MUTED =
            new Color(125, 130, 150);

    private static final Color BORDER =
            new Color(220, 223, 238);


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public RegisterFrame() {

        users = DataStore.loadUsers();

        setTitle("Smart Study Planner - Register");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setSize(430, 650);

        // EXACT CENTER
        setLocationRelativeTo(null);

        setResizable(false);

        createUI();

        setVisible(true);
    }


    // =========================================
    // CREATE UI
    // =========================================

    private void createUI() {

        // =====================================
        // OUTER BACKGROUND
        // =====================================

        JPanel background =
                new JPanel(
                        new GridBagLayout()
                );

        background.setBackground(BG);


        // =====================================
        // WHITE CARD
        // =====================================

        JPanel card =
                new JPanel(
                        new GridBagLayout()
                );

        card.setBackground(CARD);

        card.setPreferredSize(
                new Dimension(320, 570)
        );

        card.setMinimumSize(
                new Dimension(320, 570)
        );

        card.setMaximumSize(
                new Dimension(320, 570)
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                18,
                                25,
                                18,
                                25
                        )
                )
        );


        // =====================================
        // GRID CONSTRAINTS
        // =====================================

        GridBagConstraints c =
                new GridBagConstraints();

        c.gridx = 0;

        c.weightx = 1.0;

        c.fill =
                GridBagConstraints.HORIZONTAL;

        c.anchor =
                GridBagConstraints.CENTER;


        // =====================================
        // LOGO
        // =====================================

        JLabel logo =
                new JLabel("🎓");

        logo.setFont(
                new Font(
                        "Segoe UI Emoji",
                        Font.PLAIN,
                        32
                )
        );

        logo.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        c.gridy = 0;

        c.insets =
                new Insets(
                        0,
                        0,
                        3,
                        0
                );

        card.add(logo, c);


        // =====================================
        // TITLE
        // =====================================

        JLabel title =
                new JLabel(
                        "Smart Study Planner"
                );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        19
                )
        );

        title.setForeground(TEXT);

        title.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        2,
                        0
                );

        card.add(title, c);


        // =====================================
        // SUBTITLE
        // =====================================

        JLabel subtitle =
                new JLabel(
                        "Create your account"
                );

        subtitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        subtitle.setForeground(MUTED);

        subtitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        14,
                        0
                );

        card.add(subtitle, c);


        // =====================================
        // FULL NAME
        // =====================================

        JLabel fullNameLabel =
                createLabel("Full Name");

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        4,
                        0
                );

        card.add(fullNameLabel, c);


        fullNameField =
                new JTextField();

        styleField(fullNameField);

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        8,
                        0
                );

        card.add(fullNameField, c);


        // =====================================
        // USERNAME
        // =====================================

        JLabel usernameLabel =
                createLabel("Username");

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        4,
                        0
                );

        card.add(usernameLabel, c);


        usernameField =
                new JTextField();

        styleField(usernameField);

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        8,
                        0
                );

        card.add(usernameField, c);


        // =====================================
        // EMAIL
        // =====================================

        JLabel emailLabel =
                createLabel("Email");

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        4,
                        0
                );

        card.add(emailLabel, c);


        emailField =
                new JTextField();

        styleField(emailField);

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        8,
                        0
                );

        card.add(emailField, c);


        // =====================================
        // PASSWORD
        // =====================================

        JLabel passwordLabel =
                createLabel("Password");

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        4,
                        0
                );

        card.add(passwordLabel, c);


        passwordField =
                new JPasswordField();

        styleField(passwordField);

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        8,
                        0
                );

        card.add(passwordField, c);


        // =====================================
        // CONFIRM PASSWORD
        // =====================================

        JLabel confirmLabel =
                createLabel("Confirm Password");

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        4,
                        0
                );

        card.add(confirmLabel, c);


        confirmPasswordField =
                new JPasswordField();

        styleField(confirmPasswordField);

        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        13,
                        0
                );

        card.add(confirmPasswordField, c);


        // =====================================
        // REGISTER BUTTON
        // =====================================

        JButton registerButton =
                new JButton("Register");

        registerButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        // IMPORTANT:
        // White text + purple background
        registerButton.setForeground(
                Color.WHITE
        );

        registerButton.setBackground(
                PRIMARY
        );

        registerButton.setOpaque(true);

        registerButton.setContentAreaFilled(true);

        registerButton.setBorderPainted(false);

        registerButton.setFocusPainted(false);

        registerButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        registerButton.setPreferredSize(
                new Dimension(
                        270,
                        34
                )
        );

        registerButton.setMinimumSize(
                new Dimension(
                        270,
                        34
                )
        );

        registerButton.setMaximumSize(
                new Dimension(
                        270,
                        34
                )
        );

        registerButton.addActionListener(
                e -> registerUser()
        );


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        12,
                        0
                );

        card.add(
                registerButton,
                c
        );


        // =====================================
        // ALREADY HAVE ACCOUNT
        // =====================================

        JLabel alreadyText =
                new JLabel(
                        "Already have an account?"
                );

        alreadyText.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        alreadyText.setForeground(MUTED);

        alreadyText.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        2,
                        0
                );

        card.add(
                alreadyText,
                c
        );


        // =====================================
        // LOGIN LINK
        // =====================================

        JLabel loginLink =
                new JLabel("Login");

        loginLink.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        loginLink.setForeground(
                PRIMARY
        );

        loginLink.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        loginLink.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        loginLink.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        dispose();

                        new LoginFrame();
                    }
                }
        );


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        0,
                        0
                );

        card.add(
                loginLink,
                c
        );


        // =====================================
        // CENTER CARD
        // =====================================

        GridBagConstraints outer =
                new GridBagConstraints();

        outer.gridx = 0;

        outer.gridy = 0;

        outer.weightx = 1.0;

        outer.weighty = 1.0;

        outer.anchor =
                GridBagConstraints.CENTER;

        outer.fill =
                GridBagConstraints.NONE;


        background.add(
                card,
                outer
        );


        setContentPane(background);
    }


    // =========================================
    // LABEL STYLE
    // =========================================

    private JLabel createLabel(
            String text
    ) {

        JLabel label =
                new JLabel(text);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        label.setForeground(TEXT);

        return label;
    }


    // =========================================
    // FIELD STYLE
    // =========================================

    private void styleField(
            JTextField field
    ) {

        field.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        10
                )
        );

        field.setForeground(TEXT);

        field.setBackground(
                FIELD_BG
        );

        field.setCaretColor(
                PRIMARY
        );

        field.setPreferredSize(
                new Dimension(
                        270,
                        29
                )
        );

        field.setMinimumSize(
                new Dimension(
                        270,
                        29
                )
        );

        field.setMaximumSize(
                new Dimension(
                        270,
                        29
                )
        );

        field.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                5,
                                8,
                                5,
                                8
                        )
                )
        );
    }


    // =========================================
    // REGISTER USER
    // =========================================

    private void registerUser() {

        String fullName =
                fullNameField
                        .getText()
                        .trim();

        String username =
                usernameField
                        .getText()
                        .trim();

        String email =
                emailField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField
                                .getPassword()
                );


        // =====================================
        // EMPTY CHECK
        // =====================================

        if (fullName.isEmpty()
                || username.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all fields.",
                    "Registration",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================
        // PASSWORD CHECK
        // =====================================

        if (!password.equals(confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Registration",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }


        // =====================================
        // USERNAME CHECK
        // =====================================

        if (DataStore.usernameExists(
                users,
                username
        )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Username already exists.\nPlease choose another username.",
                    "Registration",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // =====================================
        // CREATE NEW USER
        // =====================================

        User newUser =
                new User(
                        fullName,
                        username,
                        email,
                        password
                );


        // Add to existing users
        users.add(newUser);


        // Save ALL users
        DataStore.saveUsers(users);


        // =====================================
        // SUCCESS
        // =====================================

        JOptionPane.showMessageDialog(
                this,
                "Registration successful!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
        );


        // Go back to Login
        dispose();

        new LoginFrame();
    }
}