package poly.quanlythuvien.entity;

import java.util.Date;

public class PhieuMuon {
    private String maPhieuMuon;
    private String maSach;
    private String maDocGia;
    private Date ngayMuon;
    private Date ngayTraDuKien;
    private String trangThai;

    // Constructor
    public PhieuMuon() {}

    public PhieuMuon(String maPhieuMuon, String maSach, String maDocGia, Date ngayMuon, 
                    Date ngayTraDuKien, String trangThai) {
        this.maPhieuMuon = maPhieuMuon;
        this.maSach = maSach;
        this.maDocGia = maDocGia;
        this.ngayMuon = ngayMuon;
        this.ngayTraDuKien = ngayTraDuKien;
        this.trangThai = trangThai;
    }

    // Getters and Setters
    public String getMaPhieuMuon() { return maPhieuMuon; }
    public void setMaPhieuMuon(String maPhieuMuon) { this.maPhieuMuon = maPhieuMuon; }
    public String getMaSach() { return maSach; }
    public void setMaSach(String maSach) { this.maSach = maSach; }
    public String getMaDocGia() { return maDocGia; }
    public void setMaDocGia(String maDocGia) { this.maDocGia = maDocGia; }
    public Date getNgayMuon() { return ngayMuon; }
    public void setNgayMuon(Date ngayMuon) { this.ngayMuon = ngayMuon; }
    public Date getNgayTraDuKien() { return ngayTraDuKien; }
    public void setNgayTraDuKien(Date ngayTraDuKien) { this.ngayTraDuKien = ngayTraDuKien; }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}