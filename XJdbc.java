package poly.quanlythuvien.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

// THÊM: Import các thư viện để hỗ trợ ánh xạ dữ liệu trong getBeanList
public class XJdbc {

    public static Connection openConnection() {
        // THAY ĐỔI: Cập nhật dburl với cổng 1433, databaseName, và thông tin đăng nhập mới
        var driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        var dburl = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyThuVien;encrypt=true;trustServerCertificate=true;";
        var username = "sd1906";
        var password = "123456";
        try {
            Class.forName(driver);
            return DriverManager.getConnection(dburl, username, password);
        } catch (ClassNotFoundException e) {
            // THAY ĐỔI: Thêm thông tin chi tiết vào thông báo lỗi
            throw new RuntimeException("Driver JDBC không tìm thấy: " + e.getMessage(), e);
        } catch (SQLException e) {
            // THAY ĐỔI: Thêm thông tin chi tiết vào thông báo lỗi
            throw new RuntimeException("Lỗi mở kết nối: " + e.getMessage(), e);
        }
    }

    public static int executeUpdate(String sql, Object... values) {
        // THAY ĐỔI: Sử dụng try-with-resources để tự động đóng Connection và PreparedStatement
        try (Connection conn = openConnection(); PreparedStatement stmt = getStmt(conn, sql, values)) {
            // THÊM: In câu lệnh SQL và tham số để debug
            System.out.println("Executing SQL: " + sql + " with values: " + java.util.Arrays.toString(values));
            int rowsAffected = stmt.executeUpdate();
            // THÊM: In số hàng bị ảnh hưởng để debug
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected;
        } catch (SQLException ex) {
            // THAY ĐỔI: Thêm thông tin chi tiết vào thông báo lỗi
            System.err.println("SQL Error: " + ex.getMessage());
            throw new RuntimeException("Lỗi thực thi SQL: " + ex.getMessage(), ex);
        }
    }

    public static ResultSet executeQuery(String sql, Object... values) {
        try {
            Connection conn = openConnection();
            PreparedStatement stmt = getStmt(conn, sql, values);
            // THÊM: In câu lệnh SQL và tham số để debug
            System.out.println("Executing Query: " + sql + " with values: " + java.util.Arrays.toString(values));
            return stmt.executeQuery();
        } catch (SQLException ex) {
            // THAY ĐỔI: Thêm thông tin chi tiết vào thông báo lỗi
            System.err.println("Query Error: " + ex.getMessage());
            throw new RuntimeException("Lỗi truy vấn: " + ex.getMessage(), ex);
        }
    }

    public static <T> T getValue(String sql, Object... values) {
        // THAY ĐỔI: Sử dụng try-with-resources để tự động đóng ResultSet
        try (ResultSet rs = executeQuery(sql, values)) {
            if (rs.next()) {
                return (T) rs.getObject(1);
            }
            return null;
        } catch (SQLException ex) {
            // THAY ĐỔI: Thêm thông tin chi tiết vào thông báo lỗi
            throw new RuntimeException("Lỗi truy vấn giá trị: " + ex.getMessage(), ex);
        }
    }

    // THAY ĐỔI: Thêm tham số Connection để quản lý kết nối từ ngoài
    private static PreparedStatement getStmt(Connection conn, String sql, Object... values) throws SQLException {
        PreparedStatement stmt = sql.trim().startsWith("{") ? conn.prepareCall(sql) : conn.prepareStatement(sql);
        for (int i = 0; i < values.length; i++) {
            stmt.setObject(i + 1, values[i]);
        }
        return stmt;
    }

    // THÊM: Triển khai phương thức getBeanList để ánh xạ ResultSet sang danh sách đối tượng
    public static <T> List<T> getBeanList(Class<T> type, String sql, Object... params) {
        List<T> list = new ArrayList<>();
        try (ResultSet rs = executeQuery(sql, params)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                T bean = type.getDeclaredConstructor().newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String fieldName = columnName.substring(0, 1).toLowerCase() + columnName.substring(1);
                    try {
                        Field field = type.getDeclaredField(fieldName);
                        Class<?> fieldType = field.getType();
                        Object value = rs.getObject(i);
                        if (value instanceof java.sql.Date && fieldType == java.util.Date.class) {
                            value = new java.util.Date(((java.sql.Date) value).getTime());
                        } else if (value instanceof java.sql.Timestamp && fieldType == java.util.Date.class) {
                            value = new java.util.Date(((java.sql.Timestamp) value).getTime());
                        }
                        String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                        Method setter = type.getMethod(setterName, fieldType);
                        setter.invoke(bean, value);
                    } catch (NoSuchFieldException | NoSuchMethodException e) {
                        System.out.printf("+ Column '%s' not mapped to any setter! (Field: %s)\n", columnName, fieldName);
                    } catch (Exception e) {
                        System.out.printf("+ Error mapping column '%s': %s\n", columnName, e.getMessage());
                    }
                }
                list.add(bean);
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi ánh xạ ResultSet: " + e.getMessage(), e);
        }
        return list;
    }

    public static void main(String[] args) {
        demo1();
        demo2();
        demo3();
    }

    private static void demo1() {
        String sql = "SELECT * FROM Sach WHERE MaSach BETWEEN ? AND ?";
        // THAY ĐỔI: Sử dụng try-with-resources để tự động đóng ResultSet
        try (ResultSet rs = XJdbc.executeQuery(sql, 2, 5)) {
            while (rs.next()) {
                System.out.println(rs.getInt("MaSach") + " - " + rs.getString("TieuDe"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static void demo2() {
        String sql = "SELECT max(MaSach) FROM Sach WHERE MaSach > ?";
        var maxMaSach = XJdbc.getValue(sql, 2);
        System.out.println("Max MaSach: " + maxMaSach);
    }

    private static void demo3() {
        String sql = "DELETE FROM Sach WHERE MaSach < ?";
        var count = XJdbc.executeUpdate(sql, 2);
        System.out.println("Đã xóa " + count + " sách.");
    }
}