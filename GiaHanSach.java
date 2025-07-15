package poly.quanlythuvien.ui;

import poly.quanlythuvien.util.XDate;
import poly.quanlythuvien.util.XDialog;
import poly.quanlythuvien.dao.PhieuMuonDAO;
import poly.quanlythuvien.daoimpl.PhieuMuonDAOImpl;
import poly.quanlythuvien.controller.GiaHanSachController;

import javax.swing.table.DefaultTableModel;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GiaHanSach extends javax.swing.JDialog implements GiaHanSachController {
    private DefaultTableModel tableModel;
    private PhieuMuonDAO dao;

    public GiaHanSach(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    private void init() {
        setTitle("Gia Hạn Sách");
        tableModel = (DefaultTableModel) tblGiaHan.getModel();
        tableModel.setRowCount(0); // Xóa dữ liệu mẫu
        dao = new PhieuMuonDAOImpl();

        // Khởi tạo JComboBox
        cboNgaytradukienmoi.removeAllItems();
        cboNgaytradukienmoi.addItem("7 ngày");
        cboNgaytradukienmoi.addItem("14 ngày");

        // Gán sự kiện cho các nút
        btnTimkiem.addActionListener(evt -> search());
        btnXacnhangiahan.addActionListener(evt -> extend());
        btnExit.addActionListener(evt -> dispose());

        // Đặt focus vào txtMaphieumuon khi khởi tạo
        txtMaphieumuon.requestFocus();
    }

    private void search() {
        String maPhieuMuon = txtMaphieumuon.getText().trim();
        try {
            List<Object[]> list = searchPhieuMuon(maPhieuMuon);
            tableModel.setRowCount(0);
            for (Object[] row : list) {
                tableModel.addRow(new Object[]{
                    row[0], // MaSach
                    row[1], // TenSach
                    row[2], // TacGia
                    XDate.format((Date) row[3], XDate.PATTERN_SHORT), // NgayMuon
                    XDate.format((Date) row[4], XDate.PATTERN_SHORT), // NgayTraDuKien
                    row[5], // TrangThai
                    false
                });
            }
            if (tableModel.getRowCount() > 0) {
                tblGiaHan.setRowSelectionInterval(0, 0);
                cboNgaytradukienmoi.requestFocus();
            } else {
                XDialog.alert(this, "Không tìm thấy phiếu mượn!", "Thông báo");
            }
        } catch (IllegalArgumentException e) {
            XDialog.alert(this, e.getMessage(), "Thông báo");
        } catch (RuntimeException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Lỗi truy vấn dữ liệu không xác định!";
            XDialog.alert(this, message, "Lỗi");
        }
    }

    private void extend() {
        int selectedRow = tblGiaHan.getSelectedRow();
        if (selectedRow == -1) {
            XDialog.alert(this, "Vui lòng chọn một phiếu mượn từ bảng!", "Thông báo");
            return;
        }

        String maPhieuMuon = txtMaphieumuon.getText().trim();
        String extendOption = (String) cboNgaytradukienmoi.getSelectedItem();
        if (extendOption == null) {
            XDialog.alert(this, "Vui lòng chọn số ngày gia hạn!", "Thông báo");
            return;
        }
        int days = extendOption.equals("7 ngày") ? 7 : 14;

        try {
            Date newNgayTraDuKien = extendLoan(maPhieuMuon, days);
            if (newNgayTraDuKien != null) {
                XDialog.alert(this, "Gia hạn thành công! Ngày trả dự kiến mới: " + 
                              XDate.format(newNgayTraDuKien, XDate.PATTERN_SHORT), "Thông báo");
                search(); // Cập nhật lại bảng
            } else {
                XDialog.alert(this, "Gia hạn thất bại!", "Lỗi");
            }
        } catch (IllegalArgumentException e) {
            XDialog.alert(this, e.getMessage(), "Thông báo");
        } catch (RuntimeException e) {
            String message = e.getMessage() != null ? e.getMessage() : "Lỗi khi gia hạn không xác định!";
            XDialog.alert(this, message, "Lỗi");
        }
    }

    @Override
    public List<Object[]> searchPhieuMuon(String maPhieuMuon) {
        if (maPhieuMuon == null || maPhieuMuon.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu mượn không được để trống!");
        }
        return dao.findPhieuMuonByMa(maPhieuMuon);
    }

    @Override
    public Date extendLoan(String maPhieuMuon, int days) {
        if (maPhieuMuon == null || maPhieuMuon.trim().isEmpty()) {
            throw new IllegalArgumentException("Mã phiếu mượn không được để trống!");
        }
        if (days != 7 && days != 14) {
            throw new IllegalArgumentException("Số ngày gia hạn phải là 7 hoặc 14!");
        }

        List<Object[]> list = dao.findPhieuMuonByMa(maPhieuMuon);
        if (list.isEmpty()) {
            throw new RuntimeException("Không tìm thấy phiếu mượn!");
        }
        String trangThai = (String) list.get(0)[5];
        if (!trangThai.equals("Đang mượn")) {
            throw new RuntimeException("Chỉ có thể gia hạn phiếu mượn có trạng thái 'Đang mượn'!");
        }

        Date ngayTraDuKien = (Date) list.get(0)[4];
        Calendar cal = Calendar.getInstance();
        cal.setTime(ngayTraDuKien);
        cal.add(Calendar.DAY_OF_MONTH, days);
        Date newNgayTraDuKien = cal.getTime();

        boolean updated = dao.updateNgayTraDuKien(maPhieuMuon, newNgayTraDuKien);
        return updated ? newNgayTraDuKien : null;
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblGiaHan = new javax.swing.JTable();
        txtMaphieumuon = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        btnTimkiem = new javax.swing.JButton();
        cboNgaytradukienmoi = new javax.swing.JComboBox<>();
        btnXacnhangiahan = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblGiaHan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã sách", "Tên sách", "Tác giả", "Ngày mượn", "Ngày trả dự kiến", "Trạng thái", ""
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblGiaHan.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblGiaHanAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jScrollPane1.setViewportView(tblGiaHan);

        txtMaphieumuon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaphieumuonActionPerformed(evt);
            }
        });

        jLabel2.setText("Mã phiếu mượn:");

        btnTimkiem.setText("Tìm kiếm");
        btnTimkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimkiemActionPerformed(evt);
            }
        });

        cboNgaytradukienmoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNgaytradukienmoiActionPerformed(evt);
            }
        });

        btnXacnhangiahan.setText("Xác nhận gia hạn");
        btnXacnhangiahan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacnhangiahanActionPerformed(evt);
            }
        });

        jLabel3.setText("Ngày trả dự kiến mới:");

        btnExit.setText("Thoát");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Gia Hạn Sách");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnExit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXacnhangiahan)
                .addContainerGap())
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtMaphieumuon, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTimkiem)
                .addGap(42, 42, 42)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboNgaytradukienmoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 679, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMaphieumuon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimkiem)
                    .addComponent(cboNgaytradukienmoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXacnhangiahan)
                    .addComponent(btnExit))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jLabel1)
                    .addGap(0, 221, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cboNgaytradukienmoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNgaytradukienmoiActionPerformed
        // TODO add your handling code here:
                btnXacnhangiahan.requestFocus();
    }//GEN-LAST:event_cboNgaytradukienmoiActionPerformed

    private void btnTimkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimkiemActionPerformed
        // TODO add your handling code here:
                search();
    }//GEN-LAST:event_btnTimkiemActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        // TODO add your handling code here:
                dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnXacnhangiahanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacnhangiahanActionPerformed
        // TODO add your handling code here:
                extend();
    }//GEN-LAST:event_btnXacnhangiahanActionPerformed

    private void tblGiaHanAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblGiaHanAncestorAdded
        // TODO add your handling code here:
                txtMaphieumuon.requestFocus();
    }//GEN-LAST:event_tblGiaHanAncestorAdded

    private void txtMaphieumuonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaphieumuonActionPerformed
        // TODO add your handling code here:
                search();
    }//GEN-LAST:event_txtMaphieumuonActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(GiaHanSach.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GiaHanSach.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GiaHanSach.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GiaHanSach.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GiaHanSach dialog = new GiaHanSach(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnTimkiem;
    private javax.swing.JButton btnXacnhangiahan;
    private javax.swing.JComboBox<String> cboNgaytradukienmoi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblGiaHan;
    private javax.swing.JTextField txtMaphieumuon;
    // End of variables declaration//GEN-END:variables
}
