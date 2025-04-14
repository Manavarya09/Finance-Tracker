import java.util.List;

public class Main {  // This defines the Main class
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {  // This defines the main method, which is the entry point of the program
        // Create an instance of TransactionDAO
        TransactionDAO transactionDAO = new TransactionDAO();

        // Example of inserting a new transaction
        Transaction newTransaction = new Transaction(
                "T12345",     // ID
                "Income",     // Type (Income or Expense)
                100.50,       // Amount
                "Salary",     // Category
                "Salary for November", // Description
                "2024-11-28"  // Date
        );
        
        try {
            // Insert the transaction into the database
            transactionDAO.insertTransaction(newTransaction);
            System.out.println("Transaction inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();  // Print the stack trace of any exceptions
        }

        // Example of fetching all transactions
        try {
            List<Transaction> transactions = transactionDAO.getAllTransactions();
            System.out.println("All Transactions:");
            for (Transaction transaction : transactions) {
                System.out.println(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Print the stack trace of any exceptions
        }
    }
}
