import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton loginButton;

    public LoginFrame() {
        setTitle("Login");
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        String[] roles = {"Staff", "Administrator"};
        roleBox = new JComboBox<>(roles);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);

        add(new JLabel("Username:"));
        add(usernameField);

        add(new JLabel("Password:"));
        add(passwordField);

        add(new JLabel("Role:"));
        add(roleBox);

        add(new JLabel(""));
        add(loginButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

        String username = usernameField.getText().trim();
        String password =
            new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        // FR-09: Validate blank fields
        if (username.isEmpty() || password.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter username and password.");
            return;
        }

        // FR-10, AC-01: Open dashboard based on role
        if (role.equals("Staff")) {
            new OperatorDashboard();
            dispose();
        }
        else if (role.equals("Administrator")) {
            new AdminDashboard();
            dispose();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
