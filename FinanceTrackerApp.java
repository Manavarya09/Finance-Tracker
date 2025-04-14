import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FinanceTrackerApp {

    private JFrame frame;
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> typeComboBox;
    private JComboBox<String> categoryComboBox;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    private JLabel lblIncomeTotal;
    private JLabel lblExpenseTotal;

    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                FinanceTrackerApp window = new FinanceTrackerApp();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public FinanceTrackerApp() {
        initialize();
    }

    @SuppressWarnings("Convert2Lambda")
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblAmount = new JLabel("Amount:");
        lblAmount.setBounds(10, 10, 80, 25);
        frame.getContentPane().add(lblAmount);

        amountField = new JTextField();
        amountField.setBounds(100, 10, 150, 25);
        frame.getContentPane().add(amountField);
        amountField.setColumns(10);

        JLabel lblDescription = new JLabel("Description:");
        lblDescription.setBounds(10, 45, 80, 25);
        frame.getContentPane().add(lblDescription);

        descriptionField = new JTextField();
        descriptionField.setBounds(100, 45, 150, 25);
        frame.getContentPane().add(descriptionField);
        descriptionField.setColumns(10);

        JLabel lblType = new JLabel("Type:");
        lblType.setBounds(10, 80, 80, 25);
        frame.getContentPane().add(lblType);

        typeComboBox = new JComboBox<>(new String[]{"Income", "Expense"});
        typeComboBox.setBounds(100, 80, 150, 25);
        frame.getContentPane().add(typeComboBox);

        JLabel lblCategory = new JLabel("Category:");
        lblCategory.setBounds(10, 115, 80, 25);
        frame.getContentPane().add(lblCategory);

        categoryComboBox = new JComboBox<>(new String[]{"Food", "Transport", "Salary", "Other"});
        categoryComboBox.setBounds(100, 115, 150, 25);
        frame.getContentPane().add(categoryComboBox);

        JButton btnAddTransaction = new JButton("Add Transaction");
        btnAddTransaction.setBounds(10, 150, 240, 25);
        frame.getContentPane().add(btnAddTransaction);

        btnAddTransaction.addActionListener(new ActionListener() {
            @Override
            @SuppressWarnings("CallToPrintStackTrace")
            public void actionPerformed(ActionEvent e) {
                // Get inputs
                String amountText = amountField.getText();
                String description = descriptionField.getText();
                String type = (String) typeComboBox.getSelectedItem();
                String category = (String) categoryComboBox.getSelectedItem();
        
                // Validate amount input
                if (amountText.isEmpty() || !amountText.matches("\\d+(\\.\\d{1,2})?")) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
                    return;
                }
        
                // Parse the amount
                double amount = Double.parseDouble(amountText);
        
                // Get the current date (e.g., using LocalDate)
                String date = java.time.LocalDate.now().toString();
        
                // Create a transaction
                Transaction transaction = new Transaction(
                        "T" + System.currentTimeMillis(),
                        type,
                        amount,
                        category,
                        description,
                        date
                );
        
                // Try to insert the transaction into the database
                try {
                    TransactionDAO transactionDAO = new TransactionDAO();
                    transactionDAO.insertTransaction(transaction);
                    JOptionPane.showMessageDialog(null, "Transaction added successfully.");
                    
                    // Refresh the table to show the new transaction
                    refreshTransactionTable();
                    
                    // Optionally, clear the form fields after the transaction is added
                    amountField.setText("");
                    descriptionField.setText("");
                    typeComboBox.setSelectedIndex(0);  // Reset to the first item in the dropdown
                    categoryComboBox.setSelectedIndex(0);  // Reset to the first item in the dropdown
        
                } catch (Exception ex) {
                    // Log the exception or show a message
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error adding transaction: " + ex.getMessage());
                }
            }
        });
        
        

        // Transaction Table Setup
        String[] columns = {"ID", "Type", "Amount", "Category", "Description", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        scrollPane.setBounds(10, 200, 550, 100);
        frame.getContentPane().add(scrollPane);

        // Income and Expense Totals
        lblIncomeTotal = new JLabel("Total Income: $0.00");
        lblIncomeTotal.setBounds(300, 10, 200, 25);
        frame.getContentPane().add(lblIncomeTotal);

        lblExpenseTotal = new JLabel("Total Expenses: $0.00");
        lblExpenseTotal.setBounds(300, 45, 200, 25);
        frame.getContentPane().add(lblExpenseTotal);

        // Refresh transaction table and totals on app launch
        refreshTransactionTable();
        updateTotals();
    }

    @SuppressWarnings("CallToPrintStackTrace")
    private void refreshTransactionTable() {
        // Clear existing rows
        tableModel.setRowCount(0);
    
        List<Transaction> transactions;
                // Get all transactions from the database
                try {
                    transactions = new TransactionDAO().getAllTransactions();
        } catch (Exception ex) {
            ex.printStackTrace();
            transactions = new ArrayList<>(); // Ensure transactions is not null
            JOptionPane.showMessageDialog(
                null,
                "Error retrieving transactions: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
    
        // Iterate over all transactions and populate the table
        for (Transaction t : transactions) {
            tableModel.addRow(new Object[]{
                    t.getId(),
                    t.getType(),
                    t.getAmount(),
                    t.getCategory(),
                    t.getDescription(),
                    t.getDate()
            });
        }
    }
    

    private void updateTotals() {
        // Variables to hold totals
        double incomeTotal = 0;
        double expenseTotal = 0;
    
        // Fetch transactions
        List<Transaction> transactions;
        try {
            transactions = new TransactionDAO().getAllTransactions();
            if (transactions == null) {
                transactions = new ArrayList<>();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error retrieving transactions: " + ex.getMessage());
            return;
        }
    
        // Calculate totals
        for (Transaction t : transactions) {
            if ("Income".equalsIgnoreCase(t.getType())) {
                incomeTotal += t.getAmount();
            } else if ("Expense".equalsIgnoreCase(t.getType())) {
                expenseTotal += t.getAmount();
            }
        }
    
        // Use final variables for lambda expression
        final double finalIncomeTotal = incomeTotal;
        final double finalExpenseTotal = expenseTotal;
    
        // Update labels on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            lblIncomeTotal.setText(String.format("Total Income: $%.2f", finalIncomeTotal));
            lblExpenseTotal.setText(String.format("Total Expenses: $%.2f", finalExpenseTotal));
        });
    }
}