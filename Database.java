import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:finance_tracker.db";

    public static Connection connect() throws Exception {
        return DriverManager.getConnection(URL);
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static void createTable() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS transactions (" +
                         "id TEXT PRIMARY KEY, " +
                         "type TEXT, " +
                         "amount REAL, " +
                         "category TEXT, " +
                         "description TEXT, " +
                         "date TEXT)";
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}