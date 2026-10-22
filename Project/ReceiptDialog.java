import javax.swing.*;

public class ReceiptDialog extends JDialog {

    public ReceiptDialog(JFrame parent, String receipt) {
        super(parent, "Digital Receipt", true);

        JTextArea area = new JTextArea(receipt);
        area.setEditable(false);

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());

        add(new JScrollPane(area), "Center");
        add(close, "South");

        setSize(350, 350);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
}
