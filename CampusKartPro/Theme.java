package store;


import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Theme {

    // CampusKart colour palette
    public static final Color PRIMARY =
        new Color(82, 121, 111);       // #52796F

    public static final Color BACKGROUND =
        new Color(234, 242, 237);      // #EAF2ED

    public static final Color WHITE = Color.WHITE;

    public static final Color TEXT =
        new Color(45, 65, 58);

    public static final Color BORDER =
        new Color(205, 220, 211);

    // Apply style to a button
    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY);
        button.setForeground(WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    }

    // Apply style to a table
    public static void styleTable(JTable table) {
        table.setBackground(WHITE);
        table.setForeground(TEXT);
        table.setSelectionBackground(BACKGROUND);
        table.setSelectionForeground(TEXT);
        table.setGridColor(BORDER);
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setBackground(PRIMARY);
        header.setForeground(WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setOpaque(true);
    }

    // Apply style to a text field
    public static void styleField(JTextField field) {
        field.setBackground(WHITE);
        field.setForeground(TEXT);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    // Apply style to a heading
    public static void styleHeading(JLabel label) {
        label.setForeground(PRIMARY);
        label.setFont(new Font("Arial", Font.BOLD, 22));
    }
}

