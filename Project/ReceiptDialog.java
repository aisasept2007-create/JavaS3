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
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 300);
            frame.setVisible(true);

            String receipt = "Receipt\n\nItem 1: $10.00\nItem 2: $20.00\nTotal: $30.00";
            new ReceiptDialog(frame, receipt);
        });
    }
}