package poly.quanlythuvien.entity;

import lombok.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UC4 {
    public enum TrangThai { DangMuon, DaTra, TreHan }

    private String maPhieuMuon, maDocGia, maNhanVien, maSach, trangThai;
    private Date ngayMuon, ngayTraDuKien, ngayTraThucTe;
    private Double tienPhat;
}