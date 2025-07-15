package poly.quanlythuvien.dao;

import java.util.Date;
import java.util.List;

public interface PhieuMuonDAO {
    List<Object[]> findPhieuMuonByMa(String maPhieuMuon);
    boolean updateNgayTraDuKien(String maPhieuMuon, Date newNgayTraDuKien);
}