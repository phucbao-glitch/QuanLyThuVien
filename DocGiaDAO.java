/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package poly.quanlythuvien.dao;
import poly.quanlythuvien.entity.DocGia;
import java.util.Date;
import java.util.List;
/**
 *
 * @author MAI QUOC TAM
 */
public interface DocGiaDAO {
    List<DocGia> findAllDocGia();
    List<DocGia> findDocGiaByTimeRange(Date begin, Date end);
    boolean createDocGia(DocGia docGia);
    boolean updateDocGia(DocGia docGia);
    boolean deleteDocGia(String maDocGia);

}

