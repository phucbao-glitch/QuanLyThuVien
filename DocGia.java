package poly.quanlythuvien.entity;

import java.util.Date;

public class DocGia {
    private String maDocGia;
    private String hoTen;
    private Date ngaySinh;
    private String diaChi;
    private String soDienThoai;
    private Date ngayCapThe;
    private Date ngayHetHan;
    private String trangThai;

    public DocGia() {}

    public DocGia(String maDocGia, String hoTen, Date ngaySinh, String diaChi, 
                  String soDienThoai, Date ngayCapThe, Date ngayHetHan, String trangThai) {
        this.maDocGia = maDocGia;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.diaChi = diaChi;
        this.soDienThoai = soDienThoai;
        this.ngayCapThe = ngayCapThe;
        this.ngayHetHan = ngayHetHan;
        this.trangThai = trangThai;
    }

    public String getMaDocGia() {
        return maDocGia;
    }

    public void setMaDocGia(String maDocGia) {
        this.maDocGia = maDocGia;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public Date getNgayCapThe() {
        return ngayCapThe;
    }

    public void setNgayCapThe(Date ngayCapThe) {
        this.ngayCapThe = ngayCapThe;
    }

    public Date getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(Date ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}