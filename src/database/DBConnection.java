package database;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
public class DBConnection {
    private static Connection connection;
    public static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            InputStream input = DBConnection.class.getResourceAsStream("/database/dbinfo.txt");
            if (input == null) {
                throw new RuntimeException("❌ Could not find dbinfo.txt file!");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String url = reader.readLine().trim();
            String user = reader.readLine().trim();
            String pass = reader.readLine().trim();
            reader.close();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connected to MySQL successfully!");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed!");
        }
    }
}