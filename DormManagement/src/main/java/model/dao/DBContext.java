package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ADMIN
 */
public class DBContext {
    private static DBContext instance;
    private Connection connection;

    private DBContext() {
        // Không khởi tạo kết nối ngay, chỉ khi cần
    }

    public static synchronized DBContext getInstance() {
        if (instance == null) {
            instance = new DBContext();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                String user = "sa";
                String password = "123456789";
                String url = "jdbc:sqlserver://localhost:1433;databaseName=dorm_management;encrypt=true;trustServerCertificate=true";
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connection = DriverManager.getConnection(url, user, password);
                System.out.println("DBContext: Đã tạo kết nối mới thành công.");
            } catch (ClassNotFoundException e) {
                System.err.println("DBContext: Lỗi tải driver JDBC: " + e.getMessage());
                e.printStackTrace();
                throw new SQLException("Không thể tải driver JDBC", e);
            } catch (SQLException e) {
                System.err.println("DBContext: Lỗi kết nối database: " + e.getMessage());
                e.printStackTrace();
                throw e; // Ném ngoại lệ để xử lý ở cấp cao hơn
            }
        }
        return connection;
    }

    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("DBContext: Đã đóng kết nối.");
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DBContext.getInstance().getConnection();
            System.out.println("Kết nối thành công: " + conn);
            conn.close();
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra kết nối: " + e.getMessage());
            e.printStackTrace();
        }
    }
}