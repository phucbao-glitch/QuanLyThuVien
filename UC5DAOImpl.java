package poly.quanlythuvien.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import poly.quanlythuvien.dao.UC5DAO;
import poly.quanlythuvien.entity.UC5;
import poly.quanlythuvien.util.XJdbc;

public class UC5DAOImpl implements UC5DAO {

    @Override
    public List<UC5> findByMaPhieuMuon(String maPhieuMuon) {
        String sql = "SELECT * FROM PhieuMuon";
        List<Object> params = new ArrayList<>();
        if (maPhieuMuon != null && !maPhieuMuon.isEmpty()) {
            sql += " WHERE MaPhieuMuon = ?";
            params.add(maPhieuMuon);
        }

        ResultSet rs = params.isEmpty() ? XJdbc.executeQuery(sql) : XJdbc.executeQuery(sql, params.toArray());
        List<UC5> list = new ArrayList<>();
        try {
            while (rs.next()) {
                list.add(UC5.builder()
                    .maPhieuMuon(rs.getString("MaPhieuMuon"))
                    .maDocGia(rs.getString("MaDocGia"))
                    .maNhanVien(rs.getString("MaNhanVien"))
                    .maSach(rs.getString("MaSach"))
                    .ngayMuon(rs.getDate("NgayMuon"))
                    .ngayTraDuKien(rs.getDate("NgayTraDuKien"))
                    .ngayTraThucTe(rs.getDate("NgayTraThucTe"))
                    .trangThai(convertToTrangThai(rs.getString("TrangThai")))
                    .tienPhat(rs.getDouble("TienPhat"))
                    .build());
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void updateTrangThaiAndNgayTra(String maPhieuMuon, String maSach, String trangThai, Date ngayTra, double tienPhat) {
        XJdbc.executeUpdate(
            "UPDATE PhieuMuon SET TrangThai = ?, NgayTraThucTe = ?, TienPhat = ? WHERE MaPhieuMuon = ? AND MaSach = ?",
            trangThai, ngayTra, tienPhat, maPhieuMuon, maSach
        );

        // Khi sách được trả (Đã Trả), cập nhật tồn kho
        if ("Đã Trả".equalsIgnoreCase(trangThai)) {
            XJdbc.executeUpdate(
                "UPDATE Sach SET DangMuon = DangMuon - 1, ConLai = ConLai + 1 WHERE MaSach = ?",
                maSach
            );
        }
    }

    @Override
    public void updateTrangThai(String maPhieuMuon, String maSach, String trangThai) {
        if ("Đang Mượn".equalsIgnoreCase(trangThai)) {
            XJdbc.executeUpdate(
                "UPDATE PhieuMuon SET TrangThai = ?, NgayTraThucTe = NULL, TienPhat = 0 WHERE MaPhieuMuon = ? AND MaSach = ?",
                trangThai, maPhieuMuon, maSach
            );
        } else {
            XJdbc.executeUpdate(
                "UPDATE PhieuMuon SET TrangThai = ? WHERE MaPhieuMuon = ? AND MaSach = ?",
                trangThai, maPhieuMuon, maSach
            );
        }
    }


    private UC5.TrangThai convertToTrangThai(String str) {
        if ("Đã Trả".equalsIgnoreCase(str)) {
            return UC5.TrangThai.DaTra;
        } else if ("Đang Mượn".equalsIgnoreCase(str)) {
            return UC5.TrangThai.DangMuon;
        } else if ("Quá Hạn".equalsIgnoreCase(str)) {
            return UC5.TrangThai.QuaHan;
        } else {
            return UC5.TrangThai.DangMuon; // fallback mặc định
        }
    }
}
