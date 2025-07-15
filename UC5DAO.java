package poly.quanlythuvien.dao;

import java.util.Date;
import java.util.List;
import poly.quanlythuvien.entity.UC5;

public interface UC5DAO {
    List<UC5> findByMaPhieuMuon(String maPhieuMuon);
    void updateTrangThai(String maPhieu, String maSach, String trangThai);
    void updateTrangThaiAndNgayTra(String maPhieuMuon, String maSach, String trangThai, Date ngayTra, double tienPhat);
}

