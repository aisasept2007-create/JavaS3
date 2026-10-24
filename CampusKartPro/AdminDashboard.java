package store;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    private final User admin;
    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{"ID", "Code", "Name", "Category", "Price", "Stock"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final JTextField code = new JTextField(10);
    private final JTextField name = new JTextField(10);
    private final JTextField category = new JTextField(10);
    private final JTextField price = new JTextField(8);
    private final JTextField stock = new JTextField(5);
    private final JTextArea history = new JTextArea();

    public AdminDashboard(User admin) {
        super("CampusKart - Admin Dashboard (" + admin.getUsername() + ")");
        this.admin = admin;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 640);
        setLocationRelativeTo(null);
        JTabbedPane tabs = new JTabbedPane();
        JPanel inventory = new JPanel(new BorderLayout(6, 6));
        inventory.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel form = new JPanel(new FlowLayout());
        form.add(new JLabel("Code")); form.add(code);
        form.add(new JLabel("Name")); form.add(name);
        form.add(new JLabel("Category")); form.add(category);
        form.add(new JLabel("Price")); form.add(price);
        form.add(new JLabel("Stock")); form.add(stock);
        JPanel buttons = new JPanel();
        JButton add = new JButton("Add Product");
        JButton update = new JButton("Update Selected");
        JButton delete = new JButton("Delete Selected");
        JButton refresh = new JButton("Refresh");
        JButton billing = new JButton("Open Billing");
        JButton logout = new JButton("Logout");
        Theme.styleTable(table);
        Theme.styleButton(add);
        Theme.styleButton(update);
        Theme.styleButton(delete);
        buttons.add(add); buttons.add(update); buttons.add(delete);
        buttons.add(refresh); buttons.add(billing); buttons.add(logout);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(form, BorderLayout.NORTH); bottom.add(buttons, BorderLayout.SOUTH);
        inventory.add(bottom, BorderLayout.SOUTH);
        tabs.addTab("Manage Products", inventory);
        history.setEditable(false);
        history.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JPanel logs = new JPanel(new BorderLayout());
        JButton reloadLogs = new JButton("Refresh Sales History");
        logs.add(reloadLogs, BorderLayout.NORTH);
        logs.add(new JScrollPane(history), BorderLayout.CENTER);
        tabs.addTab("Sales History", logs);
        add(tabs);
        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if (!e.getValueIsAdjusting() && r >= 0) {
                code.setText(model.getValueAt(r, 1).toString());
                name.setText(model.getValueAt(r, 2).toString());
                category.setText(model.getValueAt(r, 3).toString());
                price.setText(model.getValueAt(r, 4).toString());
                stock.setText(model.getValueAt(r, 5).toString());
            }
        });
        add.addActionListener(e -> save(false));
        update.addActionListener(e -> save(true));
        delete.addActionListener(e -> deleteProduct());
        refresh.addActionListener(e -> loadProducts());
        reloadLogs.addActionListener(e -> loadHistory());
        billing.addActionListener(e -> new OperatorDashboard(this.admin).setVisible(true));
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        loadProducts(); loadHistory();
    }
    
    private void loadProducts() {
        model.setRowCount(0);
        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement("SELECT * FROM products ORDER BY Code");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) model.addRow(new Object[]{rs.getInt("id"), rs.getString("code"),
                rs.getString("name"), rs.getString("category"), rs.getDouble("price"), rs.getInt("stock")});
        } catch (SQLException ex) { error(ex); }
    }

    private void save(boolean updating) {
        int selected = table.getSelectedRow();
        if (updating && selected < 0) { message("Select a product to update."); return; }
        String productCode = code.getText().trim();
        String productName = name.getText().trim();
        String productCategory = category.getText().trim();
        if (productCode.isEmpty() || productName.isEmpty() || productCategory.isEmpty()) {
            message("Code, name, and category are required."); return;
        }
        try {
            double p = Double.parseDouble(price.getText().trim());
            int s = Integer.parseInt(stock.getText().trim());
            if (!Double.isFinite(p) || p < 0 || s < 0) { message("Price and stock must not be negative."); return; }
            String sql = updating
                ? "UPDATE products SET code=?,name=?,category=?,price=?,stock=? WHERE id=?"
                : "INSERT INTO products(code,name,category,price,stock) VALUES(?,?,?,?,?)";
            try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, productCode); ps.setString(2, productName);
                ps.setString(3, productCategory); ps.setDouble(4, p); ps.setInt(5, s);
                if (updating) ps.setInt(6, (Integer) model.getValueAt(selected, 0));
                ps.executeUpdate();
                message(updating ? "Product updated." : "Product added.");
                loadProducts();
            }
        } catch (NumberFormatException ex) { message("Enter a valid price and whole-number stock."); }
        catch (SQLException ex) { error(ex); }
    }

    private void deleteProduct() {
        int row = table.getSelectedRow();
        if (row < 0) { message("Select a product to delete."); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete this product?", "Confirm",
            JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement("DELETE FROM products WHERE id=?")) {
            ps.setInt(1, (Integer) model.getValueAt(row, 0));
            ps.executeUpdate(); loadProducts();
        } catch (SQLException ex) { error(ex); }
    }

    private void loadHistory() {
        StringBuilder output = new StringBuilder();
        String sql = "SELECT s.id,s.created_at,u.username,s.total FROM sales s " +
            "JOIN users u ON s.user_id=u.id ORDER BY s.id DESC LIMIT 100";
        try (Connection c = Database.connect(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            output.append(String.format("%-8s %-22s %-18s %s%n", "Sale ID", "Date", "Operator", "Total"));
            output.append("--------------------------------------------------------------\n");
            while (rs.next()) {
                output.append(String.format("%-8d %-22s %-18s Rs. %.2f%n", rs.getInt(1),
                    rs.getTimestamp(2), rs.getString(3), rs.getDouble(4)));
            }
            history.setText(output.toString());
        } catch (SQLException ex) { error(ex); }
    }
    private void message(String msg) { JOptionPane.showMessageDialog(this, msg); }
    private void error(SQLException ex) { message("Database error: " + ex.getMessage()); }
}

