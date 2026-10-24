package store;


import javax.swing.*;
import java.awt.*;

public class AboutPage extends JFrame {

    public AboutPage() {

        setTitle("About - CampusKart");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel title = new JLabel("CAMPUSKART");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(30, 90, 160));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Campus Store Management System");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea about = new JTextArea(
            "ABOUT THE PROJECT\n\n" +
            "CampusKart is a Java-based desktop application " +
            "developed to manage the daily activities of a " +
            "college store.\n\n" +
            "The system helps staff manage products, " +
            "track inventory, calculate bills and " +
            "generate digital receipts.\n\n" +

            "MAIN FEATURES\n\n" +
            "- Admin and Staff Login\n" +
            "- Product Management\n" +
            "- Inventory Tracking\n" +
            "- Product Searching\n" +
            "- Shopping Cart and Billing\n" +
            "- Digital Receipt Generation\n" +
            "- Sales History\n\n" +

            "TECHNOLOGIES USED\n\n" +
            "Programming Language: Java\n" +
            "User Interface: Java Swing\n" +
            "Database: MySQL\n" +
            "Concepts: Object-Oriented Programming\n\n" +

            "DEVELOPED BY - GROUP 4\n\n" +
            "Aisa Anna Johnson\n" +
            "Drea Maria Jose\n" +
            "Joel Jiby Panicker\n" +
            "Neeraj S\n\n" +

            "Course: Object-Oriented Programming\n" +
            "Version: 1.0\n" +
            "Year: 2026"
        );

        about.setFont(new Font("Arial", Font.PLAIN, 14));
        about.setEditable(false);
        about.setLineWrap(true);
        about.setWrapStyleWord(true);
        about.setBackground(Color.WHITE);

        JButton closeButton = new JButton("Close");
        closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        closeButton.addActionListener(e -> dispose());

        // About page theme
        panel.setBackground(Theme.BACKGROUND);
        title.setForeground(Theme.PRIMARY);
        subtitle.setForeground(Theme.TEXT);
        about.setBackground(Theme.BACKGROUND);
        about.setForeground(Theme.TEXT);
        Theme.styleButton(closeButton);

        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(25));
        panel.add(about);
        panel.add(Box.createVerticalStrut(20));
        panel.add(closeButton);

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AboutPage());
    }
}

