import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class LoginFrame extends JFrame {

    private final List<User> users;

    private JTextField usernameField;
    private JPasswordField passwordField;


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

    private static final Color PRIMARY_DARK =
            new Color(71, 60, 185);

    private static final Color TEXT =
            new Color(40, 45, 70);

    private static final Color MUTED =
            new Color(125, 130, 150);

    private static final Color BORDER =
            new Color(220, 223, 238);


    // =========================================
    // CONSTRUCTOR
    // =========================================

    public LoginFrame() {

        // Load ALL registered users
        users = DataStore.loadUsers();

        setTitle("Smart Study Planner");

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        /*
         * Window size
         */
        setSize(430, 600);

        /*
         * IMPORTANT:
         * Whole window will appear
         * exactly in the center.
         */
        setLocationRelativeTo(null);

        /*
         * User cannot resize the window.
         */
        setResizable(false);

        createUI();

        setVisible(true);
    }


    // =========================================
    // CREATE USER INTERFACE
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
        // WHITE LOGIN CARD
        // =====================================

        JPanel card =
                new JPanel(
                        new GridBagLayout()
                );

        card.setBackground(CARD);

        /*
         * Size of white box
         */
        card.setPreferredSize(
                new Dimension(320, 480)
        );

        card.setMinimumSize(
                new Dimension(320, 480)
        );

        card.setMaximumSize(
                new Dimension(320, 480)
        );

        /*
         * Border + inside spacing
         */
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                BORDER,
                                1
                        ),
                        new EmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );


        // =====================================
        // CARD GRID CONSTRAINT
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
                        34
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
                        "Your Study - Our Priority"
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
                        18,
                        0
                );

        card.add(subtitle, c);


        // =====================================
        // LOGIN TITLE
        // =====================================

        JLabel loginTitle =
                new JLabel("Login");

        loginTitle.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        17
                )
        );

        loginTitle.setForeground(TEXT);

        loginTitle.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        13,
                        0
                );

        card.add(loginTitle, c);


        // =====================================
        // USERNAME LABEL
        // =====================================

        JLabel usernameLabel =
                new JLabel("Username");

        usernameLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        usernameLabel.setForeground(TEXT);


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        4,
                        0
                );

        card.add(usernameLabel, c);


        // =====================================
        // USERNAME FIELD
        // =====================================

        usernameField =
                new JTextField();

        styleField(usernameField);


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        10,
                        0
                );

        card.add(usernameField, c);


        // =====================================
        // PASSWORD LABEL
        // =====================================

        JLabel passwordLabel =
                new JLabel("Password");

        passwordLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        10
                )
        );

        passwordLabel.setForeground(TEXT);


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        4,
                        0
                );

        card.add(passwordLabel, c);


        // =====================================
        // PASSWORD FIELD
        // =====================================

        passwordField =
                new JPasswordField();

        styleField(passwordField);


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        7,
                        0
                );

        card.add(passwordField, c);


        // =====================================
        // REMEMBER ME + FORGOT PASSWORD
        // =====================================

        JPanel optionPanel =
                new JPanel(
                        new BorderLayout()
                );

        optionPanel.setBackground(CARD);


        JCheckBox remember =
                new JCheckBox(
                        "Remember me"
                );

        remember.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        remember.setForeground(MUTED);

        remember.setBackground(CARD);

        remember.setFocusPainted(false);

        remember.setBorder(null);


        JLabel forgot =
                new JLabel(
                        "Forgot password?"
                );

        forgot.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        forgot.setForeground(PRIMARY);


        optionPanel.add(
                remember,
                BorderLayout.WEST
        );

        optionPanel.add(
                forgot,
                BorderLayout.EAST
        );


        c.gridy++;

        c.insets =
                new Insets(
                        0,
                        0,
                        13,
                        0
                );

        card.add(
                optionPanel,
                c
        );


        // =====================================
        // LOGIN BUTTON
        // =====================================

        JButton loginButton =
                new JButton("Login");

        loginButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        11
                )
        );

        loginButton.setForeground(
                Color.WHITE
        );

        loginButton.setBackground(
                PRIMARY
        );

        loginButton.setFocusPainted(false);

        loginButton.setBorderPainted(false);

        loginButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        loginButton.setPreferredSize(
                new Dimension(
                        270,
                        32
                )
        );

        loginButton.setMinimumSize(
                new Dimension(
                        270,
                        32
                )
        );

        loginButton.setMaximumSize(
                new Dimension(
                        270,
                        32
                )
        );


        // Login click
        loginButton.addActionListener(
                e -> login()
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
                loginButton,
                c
        );


        // =====================================
        // REGISTER OPTION
        // =====================================

        /*
         * IMPORTANT:
         *
         * এখানে কোনো
         * if(users.isEmpty())
         * নেই.
         *
         * তাই Register ALWAYS থাকবে.
         *
         * New user -> Register
         * Existing user -> Login
         */

        JLabel register =
                new JLabel(
                        "Don't have an account? Register"
                );

        register.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        9
                )
        );

        register.setForeground(
                PRIMARY
        );

        register.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        register.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        // Register click
        register.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        dispose();

                        new RegisterFrame();
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
                register,
                c
        );


        // =====================================
        // CENTER THE WHITE CARD
        // =====================================

        GridBagConstraints outer =
                new GridBagConstraints();

        outer.gridx = 0;

        outer.gridy = 0;

        /*
         * These make the card stay
         * in the exact center.
         */
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


        // Set background as JFrame content
        setContentPane(background);
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
    // LOGIN FUNCTION
    // =========================================

    private void login() {

        String username =
                usernameField
                        .getText()
                        .trim();

        String password =
                new String(
                        passwordField
                                .getPassword()
                );


        // Empty field check
        if (username.isEmpty()
                || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter username and password.",
                    "Login",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }


        // Search user from ALL registered users
        User user =
                DataStore.login(
                        users,
                        username,
                        password
                );


        // =====================================
        // LOGIN SUCCESS
        // =====================================

        if (user != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Login successful!",
                    "Welcome",
                    JOptionPane.INFORMATION_MESSAGE
            );


            dispose();


            /*
             * This specific user is sent
             * to Dashboard.
             *
             * So each user can have
             * separate data.
             */

            new DashboardFrame(
                    user,
                    users
            );

        }

        // =====================================
        // LOGIN FAILED
        // =====================================

        else {

            JOptionPane.showMessageDialog(
                    this,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}