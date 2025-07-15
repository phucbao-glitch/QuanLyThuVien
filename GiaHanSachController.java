package poly.quanlythuvien.controller;

import java.util.Date;
import java.util.List;

public interface GiaHanSachController {
    List<Object[]> searchPhieuMuon(String maPhieuMuon);
    Date extendLoan(String maPhieuMuon, int days);
}