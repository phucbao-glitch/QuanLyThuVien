package poly.quanlythuvien.daoimpl;

import poly.quanlythuvien.dao.DocGiaDAO;
import poly.quanlythuvien.entity.DocGia;
import poly.quanlythuvien.util.XJdbc;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

public class DocGiaDAOImpl implements DocGiaDAO {
    private static final Logger LOGGER = Logger.getLogger(DocGiaDAOImpl.class.getName());
    
    private static final String createSql = "INSERT INTO DocGia (MaDocGia, HoTen, NgaySinh, DiaChi, SoDienThoai, NgayCapThe, NgayHetHan, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String updateSql = "UPDATE DocGia SET HoTen = ?, NgaySinh = ?, DiaChi = ?, SoDienThoai = ?, NgayCapThe = ?, NgayHetHan = ?, TrangThai = ? WHERE MaDocGia = ?";
    private static final String deleteSql = "DELETE FROM DocGia WHERE MaDocGia = ?";
    private static final String findAllSql = "SELECT * FROM DocGia";
    private static final String findByTimeRangeSql = "SELECT * FROM DocGia WHERE NgayCapThe BETWEEN ? AND ?";
    private static final String checkDuplicateSql = "SELECT COUNT(*) FROM DocGia WHERE MaDocGia = ?";

    @Override
    public boolean createDocGia(DocGia docGia) {
        if (!validateDocGia(docGia)) {
            return false;
        }
        Object[] values = {
            docGia.getMaDocGia(),
            docGia.getHoTen(),
            docGia.getNgaySinh(),
            docGia.getDiaChi(),
            docGia.getSoDienThoai(),
            docGia.getNgayCapThe(),
            docGia.getNgayHetHan(),
            docGia.getTrangThai()
        };
        try {
            int rowsAffected = XJdbc.executeUpdate(createSql, values);
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.severe("Lỗi tạo độc giả: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateDocGia(DocGia docGia) {
        if (!validateDocGia(docGia)) {
            return false;
        }
        Object[] values = {
            docGia.getHoTen(),
            docGia.getNgaySinh(),
            docGia.getDiaChi(),
            docGia.getSoDienThoai(),
            docGia.getNgayCapThe(),
            docGia.getNgayHetHan(),
            docGia.getTrangThai(),
            docGia.getMaDocGia()
        };
        try {
            int rowsAffected = XJdbc.executeUpdate(updateSql, values);
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.severe("Lỗi cập nhật độc giả: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteDocGia(String maDocGia) {
        try {
            int rowsAffected = XJdbc.executeUpdate(deleteSql, maDocGia);
            return rowsAffected > 0;
        } catch (Exception e) {
            LOGGER.severe("Lỗi xóa độc giả: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<DocGia> findAllDocGia() {
        try {
            return XJdbc.getBeanList(DocGia.class, findAllSql);
        } catch (RuntimeException e) {
            LOGGER.severe("Lỗi truy vấn danh sách độc giả: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<DocGia> findDocGiaByTimeRange(Date begin, Date end) {
        if (begin == null || end == null) {
            LOGGER.warning("Tham số begin hoặc end là null, trả về danh sách rỗng");
            return List.of();
        }
        if (begin.after(end)) {
            LOGGER.warning("Tham số begin sau end, hoán đổi giá trị");
            Date temp = begin;
            begin = end;
            end = temp;
        }
        java.sql.Date sqlBegin = new java.sql.Date(begin.getTime());
        java.sql.Date sqlEnd = new java.sql.Date(end.getTime());
        try {
            return XJdbc.getBeanList(DocGia.class, findByTimeRangeSql, sqlBegin, sqlEnd);
        } catch (Exception e) {
            LOGGER.severe("Lỗi truy vấn danh sách độc giả theo thời gian: " + e.getMessage());
            return List.of();
        }
    }

    private boolean validateDocGia(DocGia docGia) {
        if (docGia == null) {
            LOGGER.severe("DocGia is null");
            return false;
        }
        if (docGia.getMaDocGia() == null || docGia.getMaDocGia().trim().isEmpty()) {
            LOGGER.severe("Mã độc giả không được để trống");
            return false;
        }
        if (docGia.getHoTen() == null || docGia.getHoTen().trim().isEmpty()) {
            LOGGER.severe("Họ tên không được để trống");
            return false;
        }
        if (docGia.getNgaySinh() == null) {
            LOGGER.severe("Ngày sinh không hợp lệ");
            return false;
        }
        if (docGia.getNgayCapThe() == null || docGia.getNgayHetHan() == null) {
            LOGGER.severe("Ngày cấp thẻ hoặc ngày hết hạn không hợp lệ");
            return false;
        }
        if (docGia.getNgayHetHan().before(docGia.getNgayCapThe())) {
            LOGGER.severe("Ngày hết hạn phải sau ngày cấp thẻ");
            return false;
        }
        // Kiểm tra mã độc giả trùng lặp (chỉ khi tạo mới)
        if (!isUpdate(docGia)) {
            try {
                Integer count = XJdbc.getValue(checkDuplicateSql, docGia.getMaDocGia());
                if (count != null && count > 0) {
                    LOGGER.severe("Mã độc giả đã tồn tại");
                    return false;
                }
            } catch (Exception e) {
                LOGGER.severe("Lỗi kiểm tra mã độc giả trùng lặp: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    private boolean isUpdate(DocGia docGia) {
        // Kiểm tra xem độc giả đã tồn tại trong cơ sở dữ liệu chưa
        try {
            Integer count = XJdbc.getValue(checkDuplicateSql, docGia.getMaDocGia());
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}