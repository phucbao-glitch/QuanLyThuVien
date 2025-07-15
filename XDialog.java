package poly.quanlythuvien.util;

import javax.swing.JOptionPane;
import java.awt.Component;

public class XDialog {

    // THÊM: Đánh dấu phương thức cũ là @Deprecated để khuyến khích sử dụng phiên bản mới
    @Deprecated
    public static void alert(String message) {
        XDialog.alert(message, "Thông báo!");
    }

    // THÊM: Đánh dấu phương thức cũ là @Deprecated để khuyến khích sử dụng phiên bản mới
    @Deprecated
    public static void alert(String message, String title) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
    }

    // SỬA: Đặt phương thức này làm mặc định, ưu tiên sử dụng Component parent
    public static void alert(Component parent, String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    // THÊM: Đánh dấu phương thức cũ là @Deprecated để khuyến khích sử dụng phiên bản mới
    @Deprecated
    public static boolean confirm(String message) {
        return XDialog.confirm(message, "Xác nhận!");
    }

    // THÊM: Đánh dấu phương thức cũ là @Deprecated để khuyến khích sử dụng phiên bản mới
    @Deprecated
    public static boolean confirm(String message, String title) {
        int result = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return (result == JOptionPane.YES_OPTION);
    }

    // THÊM: Thêm phiên bản confirm với Component parent để hỗ trợ hiển thị hộp thoại trên parent cụ thể
    public static boolean confirm(Component parent, String message, String title) {
        int result = JOptionPane.showConfirmDialog(parent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return (result == JOptionPane.YES_OPTION);
    }

    // THÊM: Đánh dấu phương thức cũ là @Deprecated để khuyến khích sử dụng phiên bản mới
    @Deprecated
    public static String prompt(String message) {
        return XDialog.prompt(message, "Nhập vào!");
    }

    // THÊM: Đánh dấu phương thức cũ là @Deprecated để khuyến khích sử dụng phiên bản mới
    @Deprecated
    public static String prompt(String message, String title) {
        return JOptionPane.showInputDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    // THÊM: Thêm phiên bản prompt với Component parent để hỗ trợ hiển thị hộp thoại trên parent cụ thể
    public static String prompt(Component parent, String message, String title) {
        return JOptionPane.showInputDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}