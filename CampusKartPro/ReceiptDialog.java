package store;

import javax.swing.*;
import java.awt.*;

public class ReceiptDialog extends JDialog {
    public ReceiptDialog(JFrame parent, String receipt) {
        super(parent, "CampusKart - Digital Receipt", true);
        JTextArea area = new JTextArea(receipt, 20, 43);
        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        add(new JScrollPane(area), BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        JButton print = new JButton("Print");
        JButton close = new JButton("Close");
        print.addActionListener(e -> {
            try { area.print(); }
            catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage()); }
        });
        close.addActionListener(e -> dispose());
        buttons.add(print); buttons.add(close);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
        
// Receipt theme
getContentPane().setBackground(Theme.BACKGROUND);

area.setBackground(Theme.WHITE);
area.setForeground(Theme.TEXT);
area.setFont(
    new Font("Monospaced", Font.PLAIN, 14)
);

Theme.styleButton(close);

        setVisible(true);
    }
}
