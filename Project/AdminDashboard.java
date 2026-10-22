import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final List<Product> products = new ArrayList<>();
    private int nextId = 1;

    private static final int DEFAULT_LOW_STOCK_THRESHOLD = 5;

    private JTabbedPane tabs;

    private JTable productTable;
    private JTable inventoryTable;
    private JTable restockTable;

    private DefaultTableModel productModel;
    private DefaultTableModel inventoryModel;
    private DefaultTableModel restockModel;

    private JTextField idField;
    private JTextField nameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField thresholdField;
    private JTextField searchField;
    private JTextField restockIdField;
    private JTextField restockQuantityField;

    private JLabel totalProductsLabel;
    private JLabel totalUnitsLabel;
    private JLabel lowStockLabel;

    private JTextArea alertArea;

    public AdminDashboard() {
        setTitle("Admin Product & Inventory Dashboard");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Sample products
        addSampleProduct("Keyboard", "Electronics", 799.00, 12, 5);
        addSampleProduct("Mouse", "Electronics", 399.00, 4, 5);
        addSampleProduct("Notebook", "Stationery", 50.00, 20, 5);

        buildUI();
        refreshAllTables();
    }

    private void addSampleProduct(String name, String category,
                                  double price, int quantity,
                                  int threshold) {
        products.add(new Product(nextId++, name, category,
                price, quantity, threshold));
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        JLabel heading = new JLabel(
                "ADMIN PRODUCT & INVENTORY MANAGEMENT",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 21));
        heading.setBorder(BorderFactory.createEmptyBorder(
                15, 10, 15, 10));

        add(heading, BorderLayout.NORTH);

        tabs = new JTabbedPane();
        tabs.addTab("Dashboard", createDashboardPanel());
        tabs.addTab("Products", createProductsPanel());
        tabs.addTab("Inventory", createInventoryPanel());
        tabs.addTab("Restock", createRestockPanel());

        add(tabs, BorderLayout.CENTER);
    }

    // ---------------- DASHBOARD ----------------

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(
                15, 15, 15, 15));

        JPanel stats = new JPanel(new GridLayout(1, 3, 10, 10));

        totalProductsLabel = new JLabel("Total Products: 0",
                SwingConstants.CENTER);
        totalUnitsLabel = new JLabel("Total Units: 0",
                SwingConstants.CENTER);
        lowStockLabel = new JLabel("Low Stock: 0",
                SwingConstants.CENTER);

        Font statFont = new Font("Arial", Font.BOLD, 16);
        totalProductsLabel.setFont(statFont);
        totalUnitsLabel.setFont(statFont);
        lowStockLabel.setFont(statFont);

        stats.add(totalProductsLabel);
        stats.add(totalUnitsLabel);
        stats.add(lowStockLabel);

        alertArea = new JTextArea();
        alertArea.setEditable(false);
        alertArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        alertArea.setLineWrap(true);
        alertArea.setWrapStyleWord(true);

        JPanel alertPanel = new JPanel(new BorderLayout());
        alertPanel.setBorder(BorderFactory.createTitledBorder(
                "FR-03: Low-Stock Alerts"));
        alertPanel.add(new JScrollPane(alertArea),
                BorderLayout.CENTER);

        panel.add(stats, BorderLayout.NORTH);
        panel.add(alertPanel, BorderLayout.CENTER);

        return panel;
    }

    // ---------------- PRODUCT MANAGEMENT ----------------

    private JPanel createProductsPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(
                10, 10, 10, 10));

        String[] columns = {
                "ID", "Name", "Category", "Price",
                "Quantity", "Low-Stock Threshold", "Status"
        };

        productModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(productModel);
        productTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && productTable.getSelectedRow() >= 0) {
                loadSelectedProduct();
            }
        });

        JPanel form = new JPanel(new GridLayout(3, 4, 8, 8));
        form.setBorder(BorderFactory.createTitledBorder(
                "Product Details — FR-01"));

        idField = new JTextField();
        idField.setEditable(false);
        nameField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        quantityField = new JTextField();
        thresholdField = new JTextField(
                String.valueOf(DEFAULT_LOW_STOCK_THRESHOLD));

        form.add(new JLabel("Product ID:"));
        form.add(idField);
        form.add(new JLabel("Product Name:"));
        form.add(nameField);

        form.add(new JLabel("Category:"));
        form.add(categoryField);
        form.add(new JLabel("Price:"));
        form.add(priceField);

        form.add(new JLabel("Quantity:"));
        form.add(quantityField);
        form.add(new JLabel("Low-Stock Threshold:"));
        form.add(thresholdField);

        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton clearButton = new JButton("Clear Form");

        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        clearButton.addActionListener(e -> clearForm());

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(form, BorderLayout.CENTER);
        top.add(buttons, BorderLayout.SOUTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchButton.addActionListener(e -> searchProducts());
        showAllButton.addActionListener(e -> refreshProductTable());

        searchPanel.add(new JLabel("Search by name/category:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        JPanel tableArea = new JPanel(new BorderLayout(5, 5));
        tableArea.add(searchPanel, BorderLayout.NORTH);
        tableArea.add(new JScrollPane(productTable),
                BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(tableArea, BorderLayout.CENTER);

        return panel;
    }

    private void addProduct() {
        try {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();

            if (name.isEmpty() || category.isEmpty()) {
                showError("Name and category are required.");
                return;
            }

            double price = Double.parseDouble(
                    priceField.getText().trim());
            int quantity = Integer.parseInt(
                    quantityField.getText().trim());
            int threshold = Integer.parseInt(
                    thresholdField.getText().trim());

            if (!Double.isFinite(price) || price < 0
                    || quantity < 0 || threshold < 0) {
                showError("Price, quantity and threshold "
                        + "must be non-negative.");
                return;
            }

            Product product = new Product(
                    nextId++, name, category,
                    price, quantity, threshold);

            products.add(product);

            refreshAllTables();
            clearForm();

            JOptionPane.showMessageDialog(this,
                    "Product added successfully.");

        } catch (NumberFormatException ex) {
            showError("Enter valid numeric values for price, "
                    + "quantity and threshold.");
        }
    }

    private void updateProduct() {
        Product product = getSelectedProduct();

        if (product == null) {
            showError("Select a product from the table first.");
            return;
        }

        try {
            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();

            if (name.isEmpty() || category.isEmpty()) {
                showError("Name and category are required.");
                return;
            }

            double price = Double.parseDouble(
                    priceField.getText().trim());
            int quantity = Integer.parseInt(
                    quantityField.getText().trim());
            int threshold = Integer.parseInt(
                    thresholdField.getText().trim());

            if (!Double.isFinite(price) || price < 0
                    || quantity < 0 || threshold < 0) {
                showError("Price, quantity and threshold "
                        + "must be non-negative.");
                return;
            }

            product.setName(name);
            product.setCategory(category);
            product.setPrice(price);
            product.setQuantity(quantity);
            product.setLowStockThreshold(threshold);

            refreshAllTables();
            clearForm();

            JOptionPane.showMessageDialog(this,
                    "Product updated successfully.");

        } catch (NumberFormatException ex) {
            showError("Enter valid numeric values.");
        }
    }

    private void deleteProduct() {
        Product product = getSelectedProduct();

        if (product == null) {
            showError("Select a product to delete.");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(
                this,
                "Delete product: " + product.getName() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            products.remove(product);
            refreshAllTables();
            clearForm();

            JOptionPane.showMessageDialog(this,
                    "Product deleted successfully.");
        }
    }

    private void loadSelectedProduct() {
        Product product = getSelectedProduct();

        if (product == null) {
            return;
        }

        idField.setText(String.valueOf(product.getId()));
        nameField.setText(product.getName());
        categoryField.setText(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
        thresholdField.setText(
                String.valueOf(product.getLowStockThreshold()));
    }

    private Product getSelectedProduct() {
        if (productTable == null
                || productTable.getSelectedRow() < 0) {
            return null;
        }

        int row = productTable.convertRowIndexToModel(
                productTable.getSelectedRow());

        int id = (int) productModel.getValueAt(row, 0);

        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }

        return null;
    }

    private void clearForm() {
        idField.setText("");
        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        quantityField.setText("");
        thresholdField.setText(
                String.valueOf(DEFAULT_LOW_STOCK_THRESHOLD));

        if (productTable != null) {
            productTable.clearSelection();
        }
    }

    private void searchProducts() {
        String query = searchField.getText()
                .trim().toLowerCase();

        productModel.setRowCount(0);

        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query)
                    || product.getCategory()
                    .toLowerCase().contains(query)) {
                addProductRow(productModel, product);
            }
        }
    }

    // ---------------- INVENTORY ----------------

    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(
                10, 10, 10, 10));

        String[] columns = {
                "ID", "Product", "Category",
                "Quantity", "Threshold", "Stock Status"
        };

        inventoryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        inventoryTable = new JTable(inventoryModel);
        inventoryTable.setRowHeight(25);

        JLabel info = new JLabel(
                "Live inventory view — updates after every stock change.",
                SwingConstants.CENTER);

        panel.add(info, BorderLayout.NORTH);
        panel.add(new JScrollPane(inventoryTable),
                BorderLayout.CENTER);

        return panel;
    }

    // ---------------- RESTOCK ----------------

    private JPanel createRestockPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(
                10, 10, 10, 10));

        String[] columns = {
                "ID", "Product", "Current Stock",
                "Threshold", "Status"
        };

        restockModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        restockTable = new JTable(restockModel);
        restockTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        restockTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()
                    && restockTable.getSelectedRow() >= 0) {
                int row = restockTable.getSelectedRow();
                restockIdField.setText(
                        String.valueOf(restockModel.getValueAt(row, 0)));
            }
        });

        JPanel controls = new JPanel(new GridLayout(2, 2, 8, 8));
        controls.setBorder(BorderFactory.createTitledBorder(
                "UC-04: Restock Quantity"));

        restockIdField = new JTextField();
        restockQuantityField = new JTextField();

        controls.add(new JLabel("Product ID:"));
        controls.add(restockIdField);
        controls.add(new JLabel("Quantity to Add:"));
        controls.add(restockQuantityField);

        JButton restockButton = new JButton("Add Stock");
        JButton setQuantityButton = new JButton("Set Total Stock");

        restockButton.addActionListener(e -> restockProduct());
        setQuantityButton.addActionListener(e -> setTotalStock());

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(restockButton);
        buttonPanel.add(setQuantityButton);

        JPanel top = new JPanel(new BorderLayout());
        top.add(controls, BorderLayout.CENTER);
        top.add(buttonPanel, BorderLayout.SOUTH);

        JLabel info = new JLabel(
                "Select a product or enter its ID, then adjust stock.",
                SwingConstants.CENTER);

        JPanel north = new JPanel(new BorderLayout(5, 5));
        north.add(info, BorderLayout.NORTH);
        north.add(top, BorderLayout.CENTER);

        panel.add(north, BorderLayout.NORTH);
        panel.add(new JScrollPane(restockTable),
                BorderLayout.CENTER);

        return panel;
    }

    private Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }

        return null;
    }

    private void restockProduct() {
        try {
            int id = Integer.parseInt(
                    restockIdField.getText().trim());
            int amount = Integer.parseInt(
                    restockQuantityField.getText().trim());

            if (amount <= 0) {
                showError("Restock quantity must be greater than zero.");
                return;
            }

            Product product = findProductById(id);

            if (product == null) {
                showError("Product ID not found.");
                return;
            }

            if (amount > Integer.MAX_VALUE - product.getQuantity()) {
                showError("Quantity is too large.");
                return;
            }

            product.setQuantity(product.getQuantity() + amount);

            refreshAllTables();

            JOptionPane.showMessageDialog(this,
                    "Restocked successfully. New stock: "
                            + product.getQuantity());

        } catch (NumberFormatException ex) {
            showError("Enter a valid product ID and quantity.");
        }
    }

    private void setTotalStock() {
        try {
            int id = Integer.parseInt(
                    restockIdField.getText().trim());
            int quantity = Integer.parseInt(
                    restockQuantityField.getText().trim());

            if (quantity < 0) {
                showError("Total stock cannot be negative.");
                return;
            }

            Product product = findProductById(id);

            if (product == null) {
                showError("Product ID not found.");
                return;
            }

            product.setQuantity(quantity);
            refreshAllTables();

            JOptionPane.showMessageDialog(this,
                    "Total stock updated to " + quantity);

        } catch (NumberFormatException ex) {
            showError("Enter a valid product ID and quantity.");
        }
    }

    // ---------------- TABLE REFRESH & ALERTS ----------------

    private void addProductRow(DefaultTableModel model,
                               Product product) {
        model.addRow(new Object[] {
                product.getId(),
                product.getName(),
                product.getCategory(),
                String.format("%.2f", product.getPrice()),
                product.getQuantity(),
                product.getLowStockThreshold(),
                product.isLowStock() ? "LOW STOCK" : "IN STOCK"
        });
    }

    private void refreshProductTable() {
        productModel.setRowCount(0);

        for (Product product : products) {
            addProductRow(productModel, product);
        }
    }

    private void refreshInventoryTable() {
        inventoryModel.setRowCount(0);

        for (Product product : products) {
            inventoryModel.addRow(new Object[] {
                    product.getId(),
                    product.getName(),
                    product.getCategory(),
                    product.getQuantity(),
                    product.getLowStockThreshold(),
                    product.isLowStock() ? "LOW STOCK" : "IN STOCK"
            });
        }
    }

    private void refreshRestockTable() {
        restockModel.setRowCount(0);

        for (Product product : products) {
            restockModel.addRow(new Object[] {
                    product.getId(),
                    product.getName(),
                    product.getQuantity(),
                    product.getLowStockThreshold(),
                    product.isLowStock() ? "LOW STOCK" : "IN STOCK"
            });
        }
    }

    private void refreshDashboard() {
        totalProductsLabel.setText(
                "Total Products: " + products.size());

        int totalUnits = 0;
        int lowStockCount = 0;

        StringBuilder alerts = new StringBuilder();

        for (Product product : products) {
            totalUnits += product.getQuantity();

            if (product.isLowStock()) {
                lowStockCount++;

                alerts.append("WARNING: ")
                        .append(product.getName())
                        .append(" (ID: ")
                        .append(product.getId())
                        .append(") has only ")
                        .append(product.getQuantity())
                        .append(" units. Threshold: ")
                        .append(product.getLowStockThreshold())
                        .append("\n");
            }
        }

        totalUnitsLabel.setText("Total Units: " + totalUnits);
        lowStockLabel.setText("Low Stock: " + lowStockCount);

        if (alerts.length() == 0) {
            alertArea.setText("All products have sufficient stock.");
        } else {
            alertArea.setText(alerts.toString());
        }
    }

    private void refreshAllTables() {
        if (productModel != null) {
            refreshProductTable();
        }

        if (inventoryModel != null) {
            refreshInventoryTable();
        }

        if (restockModel != null) {
            refreshRestockTable();
        }

        if (alertArea != null) {
            refreshDashboard();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private static class Product {
        private final int id;
        private String name;
        private String category;
        private double price;
        private int quantity;
        private int lowStockThreshold;

        private Product(int id, String name, String category,
                        double price, int quantity,
                        int lowStockThreshold) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
            this.quantity = quantity;
            this.lowStockThreshold = lowStockThreshold;
        }

        private int getId() { return id; }
        private String getName() { return name; }
        private void setName(String name) { this.name = name; }
        private String getCategory() { return category; }
        private void setCategory(String category) { this.category = category; }
        private double getPrice() { return price; }
        private void setPrice(double price) { this.price = price; }
        private int getQuantity() { return quantity; }
        private void setQuantity(int quantity) { this.quantity = quantity; }
        private int getLowStockThreshold() { return lowStockThreshold; }
        private void setLowStockThreshold(int threshold) {
            this.lowStockThreshold = threshold;
        }
        private boolean isLowStock() { return quantity <= lowStockThreshold; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard dashboard = new AdminDashboard();
            dashboard.setVisible(true);
        });
    }
}
