package poly.quanlythuvien.controller;

import poly.quanlythuvien.entity.UC4;

public interface UC4Controller {
    void setUC4(UC4 uc4);
    void update(UC4 entity);
    void deleteById(String id);
    void open();
    void close();
    void confirmBorrow();
    void fillBookTable();
}