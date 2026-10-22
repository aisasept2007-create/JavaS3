import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class OperatorDashboard extends JFrame {

    DefaultTableModel products, cart;
    JTable productTable, cartTable;
    JTextField search;
    JLabel totalLabel;

    public OperatorDashboard() {
        setTitle("CampusKart Billing");
        setSize(750, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1, 10, 10));

        // Product table
        products = new DefaultTableModel(
            new String[]{"Code", "Product", "Price"}, 0);

        products.addRow(new Object[]{"P01", "Notebook", 50.0});
        products.addRow(new Object[]{"P02", "Pen", 10.0});

        productTable = new JTable(products);

        search = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton addButton = new JButton("Add to Cart");

        JPanel top = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();

        searchPanel.add(search);
        searchPanel.add(searchButton);
        searchPanel.add(addButton);

        top.add(searchPanel, "North");
        top.add(new JScrollPane(productTable), "Center");

        add(top);

        // Cart table
        cart = new DefaultTableModel(
            new String[]{"Product", "Price"}, 0);

        cartTable = new JTable(cart);

        JButton removeButton = new JButton("Remove Item");
        JButton checkoutButton = new JButton("Checkout");
        totalLabel = new JLabel("Total: ₹0.00");

        JPanel bottomButtons = new JPanel();
        bottomButtons.add(removeButton);
        bottomButtons.add(totalLabel);
        bottomButtons.add(checkoutButton);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(new JScrollPane(cartTable), "Center");
        bottom.add(bottomButtons, "South");

        add(bottom);

        // Search products by name or code
        searchButton.addActionListener(e -> {
            String key = search.getText().toLowerCase();
            products.setRowCount(0);

            String[][] data = {
                {"P01", "Notebook", "50.0"},
                {"P02", "Pen", "10.0"},
            };

            for (String[] p : data) {
                if (p[0].toLowerCase().contains(key)
                    || p[1].toLowerCase().contains(key)) {
                    products.addRow(p);
                }
            }
        });

        // Add product to cart
        addButton.addActionListener(e -> {
            int row = productTable.getSelectedRow();

            if (row >= 0) {
                cart.addRow(new Object[]{
                    products.getValueAt(row, 1),
                    products.getValueAt(row, 2)
                });
                calculateTotal();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Please select a product");
            }
        });

        // Remove product from cart
        removeButton.addActionListener(e -> {
            int row = cartTable.getSelectedRow();

            if (row >= 0) {
                cart.removeRow(row);
                calculateTotal();
            }
        });

        // Checkout and show receipt
        checkoutButton.addActionListener(e -> {
            if (cart.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this,
                    "Cart is empty");
                return;
            }

            StringBuilder receipt = new StringBuilder();
            double total = 0;

            receipt.append("CAMPUSKART RECEIPT\n");
            receipt.append("----------------------\n");

            for (int i = 0; i < cart.getRowCount(); i++) {
                String name = cart.getValueAt(i, 0).toString();
                double price = Double.parseDouble(
                    cart.getValueAt(i, 1).toString());

                receipt.append(name).append(" - ₹")
                    .append(price).append("\n");

                total += price;
            }

            double tax = total * 0.05;

            receipt.append("----------------------\n");
            receipt.append(String.format(
                "Subtotal: ₹%.2f\n", total));
            receipt.append(String.format(
                "Tax (5%%): ₹%.2f\n", tax));
            receipt.append(String.format(
                "Grand Total: ₹%.2f\n", total + tax));

            JOptionPane.showMessageDialog(this, receipt.toString(),
                "CampusKart Receipt", JOptionPane.INFORMATION_MESSAGE);

            cart.setRowCount(0);
            calculateTotal();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Calculate total amount
    void calculateTotal() {
        double total = 0;

        for (int i = 0; i < cart.getRowCount(); i++) {
            total += Double.parseDouble(
                cart.getValueAt(i, 1).toString());
        }

        totalLabel.setText(String.format(
            "Total: ₹%.2f", total));
    }

    public static void main(String[] args) {
        new OperatorDashboard();
    }
}