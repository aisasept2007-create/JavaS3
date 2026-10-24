package store;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private final JTextField username = new JTextField(18);
    private final JPasswordField password = new JPasswordField(18);

    public LoginFrame() {
        super("CampusKart - Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(390, 230);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND);
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 10));
        form.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        form.add(new JLabel("Username:")); form.add(username);
        form.add(new JLabel("Password:")); form.add(password);
        JButton login = new JButton("Login");
        JLabel titleLabel = new JLabel("CampusKart Store");
        Theme.styleHeading(titleLabel);
        Theme.styleField(username);
        Theme.styleButton(login);
        password.setBackground(Theme.WHITE);
        password.setForeground(Theme.TEXT);
        password.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        form.add(titleLabel); form.add(login);
        add(form);
        login.addActionListener(e -> login());
        getRootPane().setDefaultButton(login);
    }

    private void login() {
        String name = username.getText().trim();
        char[] chars = password.getPassword();
        String pass = new String(chars);
        java.util.Arrays.fill(chars, '\0');
        if (name.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password.");
            return;
        }
        String sql = "SELECT id, username, role FROM users WHERE username=? AND password_hash=SHA2(?,256)";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, name); ps.setString(2, pass);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, "Incorrect username or password.");
                    return;
                }
                User user = "ADMIN".equals(rs.getString("role"))
                    ? new AdminUser(rs.getInt("id"), rs.getString("username"))
                    : new StaffUser(rs.getInt("id"), rs.getString("username"));
                dispose();
                if (user instanceof AdminUser) new AdminDashboard(user).setVisible(true);
                else new OperatorDashboard(user).setVisible(true);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
