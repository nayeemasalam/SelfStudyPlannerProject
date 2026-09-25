import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class AIHubPanel extends JPanel {

    private final User user;

    private JTextField topicField;

    public AIHubPanel(User user) {

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
                        30,
                        40,
                        30,
                        40
                )
        );


        main.add(
                Utils.label(
                        "🤖 AI Learning Hub",
                        26,
                        true
                )
        );


        main.add(
                Box.createVerticalStrut(8)
        );


        JLabel info =
                Utils.label(
                        "Search any difficult topic and find learning resources.",
                        14,
                        false
                );

        info.setForeground(
                AppColors.MUTED
        );


        main.add(info);


        main.add(
                Box.createVerticalStrut(25)
        );


        JPanel card =
                Utils.card();


        card.setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );


        topicField =
                Utils.field(
                        "Enter topic..."
                );


        card.add(
                topicField,
                BorderLayout.CENTER
        );


        JButton search =
                Utils.button(
                        "Search",
                        true
                );


        card.add(
                search,
                BorderLayout.EAST
        );


        main.add(card);


        main.add(
                Box.createVerticalStrut(20)
        );


        JButton youtube =
                Utils.button(
                        "▶ Search YouTube Lessons",
                        false
                );


        JButton google =
                Utils.button(
                        "🌐 Search Web Explanation",
                        false
                );


        main.add(youtube);

        main.add(
                Box.createVerticalStrut(10)
        );

        main.add(google);


        search.addActionListener(
                e -> searchWeb()
        );


        google.addActionListener(
                e -> searchGoogle()
        );


        youtube.addActionListener(
                e -> searchYouTube()
        );


        add(
                new JScrollPane(main),
                BorderLayout.CENTER
        );
    }


    private void searchWeb() {

        String topic =
                topicField.getText().trim();


        if (topic.isEmpty()) {

            return;
        }


        openBrowser(
                "https://www.google.com/search?q="
                        + encode(topic
                        + " easy explanation")
        );
    }


    private void searchGoogle() {

        String topic =
                topicField.getText().trim();


        if (topic.isEmpty()) {
            return;
        }


        openBrowser(
                "https://www.google.com/search?q="
                        + encode(topic)
        );
    }


    private void searchYouTube() {

        String topic =
                topicField.getText().trim();


        if (topic.isEmpty()) {
            return;
        }


        openBrowser(
                "https://www.youtube.com/results?search_query="
                        + encode(topic)
        );
    }


    private String encode(
            String text
    ) {

        return text.replace(
                " ",
                "+"
        );
    }


    private void openBrowser(
            String url
    ) {

        try {

            Desktop.getDesktop().browse(
                    new URI(url)
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not open browser."
            );
        }
    }
}
