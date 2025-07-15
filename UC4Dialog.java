package poly.quanlythuvien.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import poly.quanlythuvien.controller.UC4Controller;
import poly.quanlythuvien.dao.UC4DAO;
import poly.quanlythuvien.dao.impl.UC4DAOImpl;
import poly.quanlythuvien.entity.UC4;
import poly.quanlythuvien.util.XJdbc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class UC4Dialog extends javax.swing.JDialog implements UC4Controller {
    private UC4DAO dao;
    private UC4 currentUC4;

    public UC4Dialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        dao = new UC4DAOImpl();
        setupTableListener();
        fillBookTable();
        initializeForm();
    }

    @Override
    public void setUC4(UC4 uc4) {
        this.currentUC4 = uc4;
        if (uc4 != null) {
            txtMaPhieuMuon.setText(uc4.getMaPhieuMuon());
            txtMaDocGia.setText(uc4.getMaDocGia());
            txtMaNhanVien.setText(uc4.getMaNhanVien());
            txtMaSach.setText(uc4.getMaSach());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            txtNgayMuon.setText(sdf.format(uc4.getNgayMuon()));
            txtNgayTraDuKien.setText(sdf.format(uc4.getNgayTraDuKien()));
            if (uc4.getTrangThai().equals("Đang mượn")) {
                rdoDangMuon.setSelected(true);
            } else if (uc4.getTrangThai().equals("Đã trả")) {
                rdoDaTra.setSelected(true);
            } else {
                rdoQuaHan.setSelected(true);
            }
        }
    }

    @Override
    public void update(UC4 entity) {
        try {
            String sql = "UPDATE PhieuMuon SET MaDocGia = ?, MaNhanVien = ?, MaSach = ?, NgayMuon = ?, NgayTraDuKien = ?, " +
                         "NgayTraThucTe = ?, TrangThai = ?, TienPhat = ? WHERE MaPhieuMuon = ?";
            XJdbc.executeUpdate(sql,
                    entity.getMaDocGia(),
                    entity.getMaNhanVien(),
                    entity.getMaSach(),
                    entity.getNgayMuon(),
                    entity.getNgayTraDuKien(),
                    entity.getNgayTraThucTe(),
                    entity.getTrangThai(),
                    entity.getTienPhat(),
                    entity.getMaPhieuMuon());
            JOptionPane.showMessageDialog(this, "Cập nhật phiếu mượn thành công!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @Override
    public void deleteById(String id) {
        try {
            String sql = "DELETE FROM PhieuMuon WHERE MaPhieuMuon = ?";
            XJdbc.executeUpdate(sql, id);
            XJdbc.executeUpdate("UPDATE Sach SET DangMuon = DangMuon - 1, ConLai = ConLai + 1 WHERE MaSach = ?",
                    currentUC4.getMaSach());
            JOptionPane.showMessageDialog(this, "Xóa phiếu mượn thành công!");
            clearForm();
            fillBookTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa: " + e.getMessage());
        }
    }

    private void initializeForm() {
        txtMaPhieuMuon.setText(""); // Không tự động tạo mã phiếu mượn
        rdoDangMuon.setSelected(true);
        txtNgayMuon.setEditable(false); // NgayMuon is not editable
        setDefaultDates();
    }

    private void setupTableListener() {
        tab.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 5) {
                    int row = e.getFirstRow();
                    DefaultTableModel model = (DefaultTableModel) tab.getModel();
                    if ((Boolean) model.getValueAt(row, 5)) {
                        String maSach = (String) model.getValueAt(row, 0);
                        txtMaSach.setText(maSach);
                        // Uncheck other rows
                        for (int i = 0; i < model.getRowCount(); i++) {
                            if (i != row) {
                                model.setValueAt(false, i, 5);
                            }
                        }
                        // Switch to Biểu mẫu tab
                        DanhSach.setSelectedIndex(1);
                    }
                }
            }
        });
    }

    @Override
    public void open() {
        setVisible(true);
    }

    @Override
    public void close() {
        dispose();
    }

    @Override
    public void confirmBorrow() {
        try {
            String maPhieuMuon = txtMaPhieuMuon.getText();
            String maDocGia = txtMaDocGia.getText();
            String maNhanVien = txtMaNhanVien.getText();
            String maSach = txtMaSach.getText();
            String ngayMuonStr = txtNgayMuon.getText();
            String ngayTraDuKienStr = txtNgayTraDuKien.getText();
            String trangThai = rdoDangMuon.isSelected() ? "Đang mượn" : 
                             rdoDaTra.isSelected() ? "Đã trả" : "Quá hạn";

            if (maPhieuMuon.isEmpty() || maDocGia.isEmpty() || maNhanVien.isEmpty() || maSach.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date ngayMuon = sdf.parse(ngayMuonStr);
            Date ngayTraDuKien = sdf.parse(ngayTraDuKienStr);

            UC4 uc4 = UC4.builder()
                    .maPhieuMuon(maPhieuMuon)
                    .maDocGia(maDocGia)
                    .maNhanVien(maNhanVien)
                    .maSach(maSach)
                    .ngayMuon(ngayMuon)
                    .ngayTraDuKien(ngayTraDuKien)
                    .trangThai(trangThai)
                    .tienPhat(0.0)
                    .build();

            dao.create(uc4);
            JOptionPane.showMessageDialog(this, "Đã xác nhận mượn sách!");
            clearForm();
            fillBookTable(); // Refresh table to reflect updated book availability
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xác nhận mượn: " + e.getMessage());
        }
    }

    @Override
    public void fillBookTable() {
        DefaultTableModel model = (DefaultTableModel) tab.getModel();
        model.setRowCount(0);
        try {
            String sql = "SELECT s.MaSach, s.TenSach, s.TacGia, t.TenTheLoai, s.NXB, s.ConLai " +
                         "FROM Sach s JOIN TheLoai t ON s.MaTheLoai = t.MaTheLoai WHERE s.ConLai > 0";
            ResultSet rs = XJdbc.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {
                    rs.getString("MaSach"),
                    rs.getString("TenSach"),
                    rs.getString("TacGia"),
                    rs.getString("TenTheLoai"),
                    rs.getString("NXB"),
                    false
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setDefaultDates() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date today = new Date();
        txtNgayMuon.setText(sdf.format(today));
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DAY_OF_MONTH, 7); // Default return date is 7 days later
        txtNgayTraDuKien.setText(sdf.format(cal.getTime()));
    }

    private void clearForm() {
        txtMaPhieuMuon.setText(""); // Không tự động tạo mã phiếu mượn
        txtMaDocGia.setText("");
        txtMaNhanVien.setText("");
        txtMaSach.setText("");
        setDefaultDates();
        rdoDangMuon.setSelected(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TrangThai = new javax.swing.ButtonGroup();
        DanhSach = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tab = new javax.swing.JTable();
        BieuMau = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtMaPhieuMuon = new javax.swing.JTextField();
        btnXacNhanMuon = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        btnThoat = new javax.swing.JButton();
        txtMaDocGia = new javax.swing.JTextField();
        txtMaSach = new javax.swing.JTextField();
        txtMaNhanVien = new javax.swing.JTextField();
        txtNgayMuon = new javax.swing.JTextField();
        txtNgayTraDuKien = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        rdoQuaHan = new javax.swing.JRadioButton();
        rdoDangMuon = new javax.swing.JRadioButton();
        rdoDaTra = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Mượn Sách");

        tab.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sách", "Tên sách", "Tác giả", "Thể loại", "NXB", "Chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tab);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        DanhSach.addTab("Danh sách", jPanel1);

        jLabel7.setText("Mã độc giả:");

        jLabel8.setText("Ngày mượn:");

        jLabel10.setText("Mã sách:");

        jLabel9.setText("Mã nhân viên:");

        btnXacNhanMuon.setText("Xác nhận mượn");
        btnXacNhanMuon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanMuonActionPerformed(evt);
            }
        });

        jLabel12.setText("Mã phiếu mượn:");

        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        jLabel13.setText("Ngày trả dự kiến:");

        jLabel11.setText("Trang Thái:");

        TrangThai.add(rdoQuaHan);
        rdoQuaHan.setText("Quá Hạn");

        TrangThai.add(rdoDangMuon);
        rdoDangMuon.setText("Đang Mượn");

        TrangThai.add(rdoDaTra);
        rdoDaTra.setText("Đã Trả");

        javax.swing.GroupLayout BieuMauLayout = new javax.swing.GroupLayout(BieuMau);
        BieuMau.setLayout(BieuMauLayout);
        BieuMauLayout.setHorizontalGroup(
            BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BieuMauLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(BieuMauLayout.createSequentialGroup()
                        .addComponent(btnThoat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXacNhanMuon))
                    .addGroup(BieuMauLayout.createSequentialGroup()
                        .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(24, 24, 24)
                        .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BieuMauLayout.createSequentialGroup()
                                .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtMaDocGia, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtNgayMuon, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)
                                .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BieuMauLayout.createSequentialGroup()
                                        .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(BieuMauLayout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BieuMauLayout.createSequentialGroup()
                                                .addGap(5, 5, 5)
                                                .addComponent(txtNgayTraDuKien, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BieuMauLayout.createSequentialGroup()
                                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtMaPhieuMuon, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(BieuMauLayout.createSequentialGroup()
                                .addComponent(rdoDangMuon)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdoQuaHan, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rdoDaTra, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        BieuMauLayout.setVerticalGroup(
            BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BieuMauLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMaPhieuMuon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(txtMaDocGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaSach, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNgayTraDuKien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNgayMuon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(rdoQuaHan)
                    .addComponent(rdoDangMuon)
                    .addComponent(rdoDaTra))
                .addGap(36, 36, 36)
                .addGroup(BieuMauLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXacNhanMuon)
                    .addComponent(btnThoat))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        DanhSach.addTab("Biểu mẫu", BieuMau);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Mượn Sách");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(DanhSach, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(DanhSach))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addGap(0, 280, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXacNhanMuonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanMuonActionPerformed
        // TODO add your handling code here:
        confirmBorrow();
    }//GEN-LAST:event_btnXacNhanMuonActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        close();
    }//GEN-LAST:event_btnThoatActionPerformed


    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UC4Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UC4Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UC4Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UC4Dialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>


        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UC4Dialog dialog = new UC4Dialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BieuMau;
    private javax.swing.JTabbedPane DanhSach;
    private javax.swing.ButtonGroup TrangThai;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton btnXacNhanMuon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdoDaTra;
    private javax.swing.JRadioButton rdoDangMuon;
    private javax.swing.JRadioButton rdoQuaHan;
    private javax.swing.JTable tab;
    private javax.swing.JTextField txtMaDocGia;
    private javax.swing.JTextField txtMaNhanVien;
    private javax.swing.JTextField txtMaPhieuMuon;
    private javax.swing.JTextField txtMaSach;
    private javax.swing.JTextField txtNgayMuon;
    private javax.swing.JTextField txtNgayTraDuKien;
    // End of variables declaration//GEN-END:variables
}
