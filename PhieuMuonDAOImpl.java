
package poly.quanlythuvien.daoimpl;

import poly.quanlythuvien.util.XJdbc;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import poly.quanlythuvien.dao.PhieuMuonDAO;

public class PhieuMuonDAOImpl implements PhieuMuonDAO {

    @Override
    public List<Object[]> findPhieuMuonByMa(String maPhieuMuon) {
        List<Object[]> result = new ArrayList<>();
        String sql = "SELECT pm.MaPhieuMuon, s.TenSach, s.TacGia, pm.NgayMuon, pm.NgayTraDuKien, pm.TrangThai " +
                     "FROM PhieuMuon pm JOIN Sach s ON pm.MaSach = s.MaSach WHERE pm.MaPhieuMuon = ?";
        try (ResultSet rs = XJdbc.executeQuery(sql, maPhieuMuon)) {
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getString("MaPhieuMuon"),
                    rs.getString("TenSach"),
                    rs.getString("TacGia"),
                    rs.getDate("NgayMuon"), 
                    rs.getDate("NgayTraDuKien"),
                    rs.getString("TrangThai")
                };
                result.add(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean updateNgayTraDuKien(String maPhieuMuon, Date newNgayTraDuKien) {
        String sql = "UPDATE PhieuMuon SET NgayTraDuKien = ? WHERE MaPhieuMuon = ?";
        int rowsAffected = XJdbc.executeUpdate(sql, newNgayTraDuKien, maPhieuMuon);
        return rowsAffected > 0;
    }
}
