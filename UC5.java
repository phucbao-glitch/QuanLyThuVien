package poly.quanlythuvien.entity;

import lombok.*;
import java.util.Date;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor
public class UC5 {
    public enum TrangThai { DangMuon, DaTra, QuaHan }

    private String maPhieuMuon, maDocGia, maNhanVien, maSach;
    @Builder.Default private Date ngayMuon = new Date();
    private Date ngayTraDuKien, ngayTraThucTe;
    private TrangThai trangThai;
    private Double tienPhat;
    private transient String tenSach, tacGia;
}

