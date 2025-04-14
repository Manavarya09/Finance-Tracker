import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    // Method to insert a transaction (as before)
    @SuppressWarnings("CallToPrintStackTrace")
    public void insertTransaction(Transaction transaction) throws Exception {
        String sql = "INSERT INTO transactions(id, type, amount, category, description, date) VALUES(?,?,?,?,?,?)";
        
        try (Connection conn = Database.connect(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, transaction.getId());
            pstmt.setString(2, transaction.getType());
            pstmt.setDouble(3, transaction.getAmount());
            pstmt.setString(4, transaction.getCategory());
            pstmt.setString(5, transaction.getDescription());
            pstmt.setString(6, transaction.getDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all transactions from the database
    @SuppressWarnings("CallToPrintStackTrace")
    public List<Transaction> getAllTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
    
        try (Connection conn = Database.connect(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getString("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("date")
                );
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return transactions;
    }
    

    // Method to retrieve transactions by type (Income or Expense)
    @SuppressWarnings("CallToPrintStackTrace")
    public List<Transaction> getTransactionsByType(String type) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE type = ?";
        
        try (Connection conn = Database.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, type);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(
                            rs.getString("id"),
                            rs.getString("type"),
                            rs.getDouble("amount"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getString("date")
                    );
                    transactions.add(transaction);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }
}