package poly.quanlythuvien.dao;

import poly.quanlythuvien.entity.UC4;
import java.util.Date;
import java.util.List;

public interface UC4DAO {
    List<UC4> findAll();
    List<UC4> findByDocGia(String maDocGia);
    List<UC4> findBySach(String maSach);
    List<UC4> findByTimeRange(Date begin, Date end);
    List<UC4> findByDocGiaAndTimeRange(String maDocGia, Date begin, Date end);
    UC4 findById(String id);
    UC4 create(UC4 entity);
    UC4 findDangMuonBySach(String maSach);
}