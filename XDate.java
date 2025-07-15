package poly.quanlythuvien.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XDate {
    public static final String PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";
    // THAY ĐỔI: Thay đổi định dạng ngắn từ "MM/dd/yyyy" thành "dd/MM/yyyy"
    public static final String PATTERN_SHORT = "dd/MM/yyyy";

    // THAY ĐỔI: Thay SimpleDateFormat tĩnh bằng ThreadLocal<SimpleDateFormat> để tránh xung đột đa luồng
    private static final ThreadLocal<SimpleDateFormat> threadLocalFormatter = ThreadLocal.withInitial(() -> new SimpleDateFormat());

    public static Date now() {
        return new Date();
    }

    // THÊM: Kiểm tra null cho dateTime và pattern để tránh lỗi
    public static Date parse(String dateTime, String pattern) {
        if (dateTime == null || pattern == null) return null;
        // THAY ĐỔI: Sử dụng ThreadLocal formatter thay vì formatter tĩnh
        SimpleDateFormat sdf = threadLocalFormatter.get();
        sdf.applyPattern(pattern);
        try {
            return sdf.parse(dateTime);
        } catch (ParseException e) {
            // THÊM: In thông báo lỗi để hỗ trợ debug
            System.out.println("Lỗi parse ngày tháng: " + e.getMessage());
            return null;
        }
    }

    public static Date parse(String dateTime) {
        return parse(dateTime, PATTERN_SHORT);
    }

    // THÊM: In thông báo khi dateTime là null để hỗ trợ debug
    public static String format(Date dateTime, String pattern) {
        if (dateTime == null) {
            System.out.println("DateTime is null, returning empty string");
            return "";
        }
        // THAY ĐỔI: Sử dụng ThreadLocal formatter thay vì formatter tĩnh
        SimpleDateFormat sdf = threadLocalFormatter.get();
        sdf.applyPattern(pattern);
        return sdf.format(dateTime);
    }

    public static String format(Date dateTime) {
        return format(dateTime, PATTERN_SHORT);
    }

    public static void main(String[] args) {
        Date date = XDate.parse("Jan 21, 2024", "MMM dd, yyyy");
        String text = XDate.format(date, "dd-MMM-yyyy");
        System.out.println(text); // => 21-Jan-2024
    }
}