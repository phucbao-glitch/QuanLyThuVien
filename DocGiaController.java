package poly.quanlythuvien.controller;

import poly.quanlythuvien.entity.DocGia;
import java.util.Date;
import java.util.List;

public interface DocGiaController extends CrudController<DocGia> {
    List<DocGia> findDocGiaByTimeRange(Date begin, Date end);
}