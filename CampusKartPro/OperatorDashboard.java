package store;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OperatorDashboard extends JFrame {
    private final User user;
    private final DefaultTableModel products = new DefaultTableModel(
        new String[]{"ID", "Code", "Product", "Category", "Price", "Stock"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final DefaultTableModel cart = new DefaultTableModel(
        new String[]{"ID", "Product", "Qty", "Unit Price", "Amount"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable productTable = new JTable(products);
    private final JTable cartTable = new JTable(cart);
    private final JTextField search = new JTextField(18);
    private final JTextField quantity = new JTextField("1", 4);
    private final JLabel totalLabel = new JLabel("Subtotal: Rs. 0.00    Tax (0%): Rs. 0.00    Grand Total: Rs. 0.00");
    private final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(products);
    private static final double TAX_RATE = 0.0; // Set according to project requirements.

    public OperatorDashboard(User user) {
        super("CampusKart - Operator Dashboard (" + user.getUsername() + ")");
        this.user = user;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        JLabel title = new JLabel("CampusKart - Product Search & Billing", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 21));
        add(title, BorderLayout.NORTH);
        productTable.setRowSorter(sorter);
        JPanel lookup = new JPanel(new BorderLayout(5, 5));
        JPanel searchRow = new JPanel();
        searchRow.add(new JLabel("Search code/name:")); searchRow.add(search);
        JButton refresh = new JButton("Refresh Stock"); searchRow.add(refresh);
        lookup.add(searchRow, BorderLayout.NORTH);
        lookup.add(new JScrollPane(productTable), BorderLayout.CENTER);
        JPanel cartPanel = new JPanel(new BorderLayout());
        cartPanel.add(new JLabel("Shopping Cart"), BorderLayout.NORTH);
        cartPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, lookup, cartPanel);
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);
        JPanel bottom = new JPanel(new BorderLayout());
        JPanel actions = new JPanel();
        JButton add = new JButton("Add to Cart");
        JButton remove = new JButton("Remove Selected");
        JButton checkout = new JButton("Checkout");
        JButton logout = new JButton("Logout");
        actions.add(new JLabel("Quantity:")); actions.add(quantity);
        actions.add(add); actions.add(remove); actions.add(checkout); actions.add(logout);
        bottom.add(actions, BorderLayout.NORTH);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottom.add(totalLabel, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
        refresh.addActionListener(e -> loadProducts());
        add.addActionListener(e -> addToCart());
        remove.addActionListener(e -> removeFromCart());
        checkout.addActionListener(e -> checkout());
        JButton aboutButton = new JButton("About");
        aboutButton.addActionListener(e -> new AboutPage());
        searchRow.add(aboutButton);
        logout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        search.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
        loadProducts();
    }

    private void filter() {
        String value = search.getText().trim();
        sorter.setRowFilter(value.isEmpty() ? null : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(value), 1, 2));
    }

    private void loadProducts() {
        products.setRowCount(0);
        try (Connection c = Database.connect();
             PreparedStatement ps = c.prepareStatement("SELECT id,code,name,category,price,stock FROM products ORDER BY name");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                products.addRow(new Object[]{rs.getInt("id"), rs.getString("code"),
                    rs.getString("name"), rs.getString("category"),
                    rs.getDouble("price"), rs.getInt("stock")});
            }
        } catch (SQLException ex) { error(ex); }
    }

    private void addToCart() {
        int selected = productTable.getSelectedRow();
        if (selected < 0) { message("Select a product first."); return; }
        int row = productTable.convertRowIndexToModel(selected);
        try {
            int qty = Integer.parseInt(quantity.getText().trim());
            if (qty <= 0) { message("Quantity must be greater than zero."); return; }
            int id = (Integer) products.getValueAt(row, 0);
            int stock = (Integer) products.getValueAt(row, 5);
            int existing = -1;
            int inCart = 0;
            for (int i = 0; i < cart.getRowCount(); i++) {
                if ((Integer) cart.getValueAt(i, 0) == id) {
                    existing = i; inCart = (Integer) cart.getValueAt(i, 2); break;
                }
            }
            if (qty + inCart > stock) { message("Insufficient stock. Available: " + stock); return; }
            double price = (Double) products.getValueAt(row, 4);
            if (existing >= 0) {
                cart.setValueAt(qty + inCart, existing, 2);
                cart.setValueAt((qty + inCart) * price, existing, 4);
            } else {
                cart.addRow(new Object[]{id, products.getValueAt(row, 2), qty, price, qty * price});
            }
            updateTotal();
        } catch (NumberFormatException ex) { message("Enter a valid whole-number quantity."); }
    }

    private void removeFromCart() {
        int row = cartTable.getSelectedRow();
        if (row < 0) { message("Select a cart item first."); return; }
        cart.removeRow(row); updateTotal();
    }

    private double subtotal() {
        double amount = 0;
        for (int i = 0; i < cart.getRowCount(); i++) amount += (Double) cart.getValueAt(i, 4);
        return amount;
    }
    private void updateTotal() {
        double sub = subtotal();
        totalLabel.setText(String.format("Subtotal: Rs. %.2f    Tax (%.0f%%): Rs. %.2f    Grand Total: Rs. %.2f",
            sub, TAX_RATE * 100, sub * TAX_RATE, sub * (1 + TAX_RATE)));
    }

    private void checkout() {
        if (cart.getRowCount() == 0) { message("Cart is empty."); return; }
        if (JOptionPane.showConfirmDialog(this, "Complete this sale?", "Confirm Checkout",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        double sub = subtotal();
        double tax = Math.round(sub * TAX_RATE * 100.0) / 100.0;
        double grand = sub + tax;
        try (Connection c = Database.connect()) {
            c.setAutoCommit(false);
            try {
                // Validate stock inside the same transaction, locking rows until checkout finishes.
                try (PreparedStatement lock = c.prepareStatement("SELECT stock FROM products WHERE id=? FOR UPDATE")) {
                    for (int i = 0; i < cart.getRowCount(); i++) {
                        lock.setInt(1, (Integer) cart.getValueAt(i, 0));
                        try (ResultSet rs = lock.executeQuery()) {
                            int qty = (Integer) cart.getValueAt(i, 2);
                            if (!rs.next() || rs.getInt(1) < qty)
                                throw new SQLException("Not enough stock for " + cart.getValueAt(i, 1));
                        }
                    }
                }
                int saleId;
                try (PreparedStatement sale = c.prepareStatement(
                        "INSERT INTO sales(user_id,subtotal,tax,total) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                    sale.setInt(1, user.getId()); sale.setDouble(2, sub);
                    sale.setDouble(3, tax); sale.setDouble(4, grand);
                    sale.executeUpdate();
                    try (ResultSet keys = sale.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Could not create sale.");
                        saleId = keys.getInt(1);
                    }
                }
                try (PreparedStatement item = c.prepareStatement(
                        "INSERT INTO sale_items(sale_id,product_id,product_name,quantity,unit_price,amount) VALUES(?,?,?,?,?,?)");
                     PreparedStatement stock = c.prepareStatement("UPDATE products SET stock=stock-? WHERE id=?")) {
                    for (int i = 0; i < cart.getRowCount(); i++) {
                        int id = (Integer) cart.getValueAt(i, 0);
                        int qty = (Integer) cart.getValueAt(i, 2);
                        item.setInt(1, saleId); item.setInt(2, id);
                        item.setString(3, cart.getValueAt(i, 1).toString());
                        item.setInt(4, qty); item.setDouble(5, (Double) cart.getValueAt(i, 3));
                        item.setDouble(6, (Double) cart.getValueAt(i, 4));
                        item.executeUpdate();
                        stock.setInt(1, qty); stock.setInt(2, id); stock.executeUpdate();
                    }
                }
                c.commit();
                StringBuilder receipt = new StringBuilder("CAMPUSKART - DIGITAL RECEIPT\n");
                receipt.append("Sale #").append(saleId).append("\n");
                receipt.append("Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))).append("\n");
                receipt.append("Operator: ").append(user.getUsername()).append("\n");
                receipt.append("----------------------------------------\n");
                for (int i = 0; i < cart.getRowCount(); i++)
                    receipt.append(String.format("%s x%d @ Rs. %.2f = Rs. %.2f\n",
                        cart.getValueAt(i, 1), cart.getValueAt(i, 2),
                        cart.getValueAt(i, 3), cart.getValueAt(i, 4)));
                receipt.append("----------------------------------------\n");
                receipt.append(String.format("Subtotal: Rs. %.2f\nTax: Rs. %.2f\nTOTAL: Rs. %.2f\n", sub, tax, grand));
                receipt.append("Thank you for shopping!\n");
                cart.setRowCount(0); updateTotal(); loadProducts();
                new ReceiptDialog(this, receipt.toString());
            } catch (SQLException ex) {
                c.rollback(); error(ex);
            }
        } catch (SQLException ex) { error(ex); }
    }

    private void message(String msg) { JOptionPane.showMessageDialog(this, msg); }
    private void error(SQLException ex) { message("Database error: " + ex.getMessage()); }
}

