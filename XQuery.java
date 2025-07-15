package poly.quanlythuvien.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import poly.quanlythuvien.entity.TaiKhoan;

/**
 * Lớp tiện ích hỗ trợ truy vấn và chuyển đổi sang đối tượng
 *
 * @author NghiemN
 * @version 1.0
 */
public class XQuery {

    /**
     * Truy vấn 1 đối tượng
     */
    public static <B> B getSingleBean(Class<B> beanClass, String sql, Object... values) {
        List<B> list = getBeanList(beanClass, sql, values);
        return !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * Truy vấn nhiều đối tượng
     *
     * @param <B>       kiểu của đối tượng cần chuyển đổi
     * @param beanClass lớp của đối tượng kết quả
     * @param sql       câu lệnh truy vấn
     * @param values    các giá trị cung cấp cho các tham số của SQL
     * @return danh sách kết quả truy vấn
     * @throws RuntimeException lỗi truy vấn
     * @note Người dùng phải tự đóng ResultSet nếu không sử dụng trong try-with-resources
     */
    // SỬA: Sử dụng try-with-resources để tự động đóng ResultSet và cải thiện thông báo lỗi
    public static <B> List<B> getBeanList(Class<B> beanClass, String sql, Object... values) {
        List<B> list = new ArrayList<>();
        try (ResultSet rs = XJdbc.executeQuery(sql, values)) {
            while (rs.next()) {
                list.add(readBean(rs, beanClass));
            }
        } catch (Exception ex) {
            // SỬA: Thêm thông tin chi tiết vào thông báo lỗi
            throw new RuntimeException("Lỗi truy vấn danh sách: " + ex.getMessage(), ex);
        }
        return list;
    }

    /**
     * Tạo bean với dữ liệu đọc từ bản ghi hiện tại
     */
    // SỬA: Sử dụng ResultSetMetaData để ánh xạ cột linh hoạt hơn, xử lý Date
    private static <B> B readBean(ResultSet resultSet, Class<B> beanClass) throws Exception {
        B bean = beanClass.getDeclaredConstructor().newInstance();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        // SỬA: Chuẩn hóa tên cột thành chữ thường để ánh xạ chính xác
        for (int i = 1; i <= columnCount; i++) {
            String columnName = metaData.getColumnName(i).toLowerCase();
            String setterName = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
            try {
                // SỬA: Tìm setter với tham số Object.class để linh hoạt hơn
                Method method = beanClass.getMethod(setterName, Object.class);
                Object value = resultSet.getObject(columnName);
                // THÊM: Chuyển java.sql.Date thành java.util.Date
                if (value instanceof java.sql.Date) {
                    value = new java.util.Date(((java.sql.Date) value).getTime());
                }
                method.invoke(bean, value);
            } catch (NoSuchMethodException | SQLException e) {
                // SỬA: Đơn giản hóa thông báo lỗi, bỏ các ngoại lệ không cần thiết
                System.out.printf("+ Column '%s' not mapped!\n", columnName);
            }
        }
        return bean;
    }

    /**
     * Truy vấn danh sách đối tượng (trả về null nếu rỗng)
     */
    public static <B> List<B> getEntityList(Class<B> aClass, String findAllSql, Object... values) {
        List<B> list = getBeanList(aClass, findAllSql, values);
        return list.isEmpty() ? null : list;
    }

    public static void main(String[] args) {
        demo1();
        demo2();
    }

    // THÊM: In kết quả để hỗ trợ debug
    private static void demo1() {
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap=? AND MatKhau=?";
        TaiKhoan user = XQuery.getSingleBean(TaiKhoan.class, sql, "NghiemN", "123456");
        System.out.println("User: " + (user != null ? user.toString() : "null"));
    }

    // THÊM: In kích thước danh sách để hỗ trợ debug
    private static void demo2() {
        String sql = "SELECT * FROM TaiKhoan WHERE TenDangNhap LIKE ?";
        List<TaiKhoan> list = XQuery.getBeanList(TaiKhoan.class, sql, "%Nguyễn %");
        System.out.println("List size: " + list.size());
    }
}