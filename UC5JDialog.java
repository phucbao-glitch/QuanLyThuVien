package poly.quanlythuvien.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import poly.quanlythuvien.dao.UC5DAO;
import poly.quanlythuvien.dao.impl.UC5DAOImpl;
import poly.quanlythuvien.entity.UC5;

public class UC5JDialog extends javax.swing.JDialog {

    private UC5DAO dao = new UC5DAOImpl();

    public UC5JDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        cboTrangThai.setEnabled(true);
        List<UC5> list = dao.findByMaPhieuMuon(null);
        fillBookTable(list);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnThoat = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMaPhieuMuon = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSach = new javax.swing.JTable();
        btnTimKiem = new javax.swing.JButton();
        btnXacNhanTra = new javax.swing.JButton();
        btnQuaHan = new javax.swing.JButton();
        btnDangMuon = new javax.swing.JButton();
        cboTrangThai = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Trả Sách");

        btnThoat.setText("Thoát");
        btnThoat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThoatActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Trả Sách");

        jLabel2.setText("Mã phiếu mượn:");

        txtMaPhieuMuon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaPhieuMuonActionPerformed(evt);
            }
        });

        tblSach.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Phiếu Mượn", "Mã Độc Giả", "Mã Nhân Viên", "Mã Sách", "Ngày Mượn", "Ngày Trả Dự Kiến", "Ngày Trả Thực Tế", "Trạng Thái", "Tiền Phạt", "Chọn"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblSach);

        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        btnXacNhanTra.setText("Xác Nhận Trả");
        btnXacNhanTra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanTraActionPerformed(evt);
            }
        });

        btnQuaHan.setText("Quá Hạn");
        btnQuaHan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuaHanActionPerformed(evt);
            }
        });

        btnDangMuon.setText("Đang Mượn");
        btnDangMuon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangMuonActionPerformed(evt);
            }
        });

        cboTrangThai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất Cả", "Đang Mượn", "Quá Hạn", "Đã Trả" }));
        cboTrangThai.setToolTipText("");
        cboTrangThai.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cboTrangThai.setEnabled(false);
        cboTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTrangThaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 45, Short.MAX_VALUE)
                .addComponent(txtMaPhieuMuon, javax.swing.GroupLayout.PREFERRED_SIZE, 329, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 51, Short.MAX_VALUE)
                .addComponent(btnTimKiem)
                .addGap(18, 51, Short.MAX_VALUE)
                .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnThoat)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnXacNhanTra)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnDangMuon)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnQuaHan)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtMaPhieuMuon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem)
                    .addComponent(cboTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXacNhanTra)
                    .addComponent(btnThoat)
                    .addComponent(btnQuaHan)
                    .addComponent(btnDangMuon))
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaPhieuMuonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaPhieuMuonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaPhieuMuonActionPerformed
    
    private void fillBookTable(List<UC5> list) {
        DefaultTableModel model = (DefaultTableModel) tblSach.getModel();
        model.setRowCount(0); // clear
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();

        for (UC5 uc5 : list) {
            String trangThaiStr = "";
            if (uc5.getTrangThai() == UC5.TrangThai.DangMuon) {
                if (uc5.getNgayTraDuKien() != null && uc5.getNgayTraDuKien().before(now)) {
                    trangThaiStr = "Quá Hạn";
                } else {
                    trangThaiStr = "Đang Mượn";
                }
            } else if (uc5.getTrangThai() == UC5.TrangThai.DaTra) {
                trangThaiStr = "Đã Trả";
            } else if (uc5.getTrangThai() == UC5.TrangThai.QuaHan) {
                trangThaiStr = "Quá Hạn";
            }

            model.addRow(new Object[]{
                uc5.getMaPhieuMuon(),
                uc5.getMaDocGia(),
                uc5.getMaNhanVien(),
                uc5.getMaSach(),
                uc5.getNgayMuon() != null ? sdf.format(uc5.getNgayMuon()) : "",
                uc5.getNgayTraDuKien() != null ? sdf.format(uc5.getNgayTraDuKien()) : "",
                uc5.getNgayTraThucTe() != null ? sdf.format(uc5.getNgayTraThucTe()) : "",
                trangThaiStr,
                uc5.getTienPhat(),
                false
            });
        }
    }

    private void btnXacNhanTraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanTraActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblSach.getModel();
        boolean daChon = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isChecked = (Boolean) model.getValueAt(i, 9); // cột "Chọn"
            if (isChecked != null && isChecked) {
                daChon = true;
                String maPhieu = (String) model.getValueAt(i, 0);
                String maSach = (String) model.getValueAt(i, 3);
                String ngayTraDuKienStr = (String) model.getValueAt(i, 5);
                double tienPhat = 0;
                Date ngayTraThucTe = new Date();

                try {
                    Date ngayTraDuKien = new SimpleDateFormat("dd/MM/yyyy").parse(ngayTraDuKienStr);
                    long diff = ngayTraThucTe.getTime() - ngayTraDuKien.getTime();
                    if (diff > 0) {
                        long daysOverdue = diff / (1000 * 60 * 60 * 24);
                        tienPhat = daysOverdue * 2000;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Truyền "Đã Trả" thay vì UC5.TrangThai.DaTra.name()
                dao.updateTrangThaiAndNgayTra(maPhieu, maSach, "Đã Trả", ngayTraThucTe, tienPhat);
            }
        }

        if (!daChon) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một sách để xác nhận trả!");
        } else {
            JOptionPane.showMessageDialog(this, "Xác nhận trả sách thành công!");
            List<UC5> list = dao.findByMaPhieuMuon(null);
            fillBookTable(list);
        }
    }//GEN-LAST:event_btnXacNhanTraActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        String maPhieu = txtMaPhieuMuon.getText().trim();
        if (maPhieu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phiếu mượn!");
            return;
        }
        List<UC5> list = dao.findByMaPhieuMuon(maPhieu);
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sách theo mã phiếu mượn!");
        }
        fillBookTable(list);
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnThoatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThoatActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btnThoatActionPerformed

    private void btnQuaHanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuaHanActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblSach.getModel();
        boolean daChon = false;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date now = new Date();

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isChecked = (Boolean) model.getValueAt(i, 9); // Cột "Chọn"
            if (isChecked != null && isChecked) {
                daChon = true;

                String maPhieu = (String) model.getValueAt(i, 0);
                String maSach = (String) model.getValueAt(i, 3);
                String ngayTraDuKienStr = (String) model.getValueAt(i, 5);

                double tienPhat = 0;

                try {
                    Date ngayTraDuKien = sdf.parse(ngayTraDuKienStr);
                    long diff = now.getTime() - ngayTraDuKien.getTime();
                    if (diff > 0) {
                        long daysOverdue = diff / (1000 * 60 * 60 * 24);
                        tienPhat = daysOverdue * 2000;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Cập nhật trạng thái + tiền phạt, NgayTraThucTe = NULL
                dao.updateTrangThaiAndNgayTra(maPhieu, maSach, "Quá Hạn", null, tienPhat);
            }
        }

        if (!daChon) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một sách để chuyển thành Quá Hạn!");
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật trạng thái Quá Hạn và tiền phạt thành công!");
            List<UC5> list = dao.findByMaPhieuMuon(null);
            fillBookTable(list);
        }
    }//GEN-LAST:event_btnQuaHanActionPerformed

    private void btnDangMuonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangMuonActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblSach.getModel();
        boolean daChon = false;

        for (int i = 0; i < model.getRowCount(); i++) {
            Boolean isChecked = (Boolean) model.getValueAt(i, 9); // Cột "Chọn"
            if (isChecked != null && isChecked) {
                daChon = true;
                String maPhieu = (String) model.getValueAt(i, 0);
                String maSach = (String) model.getValueAt(i, 3);

                // Cập nhật trạng thái trong DB
                dao.updateTrangThai(maPhieu, maSach, "Đang Mượn");
            }
        }

        if (!daChon) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một sách để chuyển thành Đang Mượn!");
        } else {
            JOptionPane.showMessageDialog(this, "Chuyển trạng thái thành Đang Mượn thành công!");
            List<UC5> list = dao.findByMaPhieuMuon(null);
            fillBookTable(list);
        }
    }//GEN-LAST:event_btnDangMuonActionPerformed

    private void cboTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTrangThaiActionPerformed
        // TODO add your handling code here:
        String trangThaiChon = (String) cboTrangThai.getSelectedItem();
        String maPhieu = txtMaPhieuMuon.getText().trim();

        List<UC5> list = dao.findByMaPhieuMuon(maPhieu.isEmpty() ? null : maPhieu);

        if (!"Tất Cả".equalsIgnoreCase(trangThaiChon)) {
            list.removeIf(uc5 -> {
                String tt = "";
                if (uc5.getTrangThai() == UC5.TrangThai.DaTra) tt = "Đã Trả";
                else if (uc5.getTrangThai() == UC5.TrangThai.DangMuon) {
                    if (uc5.getNgayTraDuKien() != null && uc5.getNgayTraDuKien().before(new Date())) {
                        tt = "Quá Hạn";
                    } else {
                        tt = "Đang Mượn";
                    }
                } else if (uc5.getTrangThai() == UC5.TrangThai.QuaHan) tt = "Quá Hạn";
                return !tt.equalsIgnoreCase(trangThaiChon);
            });
        }

        fillBookTable(list);
    }//GEN-LAST:event_cboTrangThaiActionPerformed


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
            java.util.logging.Logger.getLogger(UC5JDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UC5JDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UC5JDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UC5JDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                UC5JDialog dialog = new UC5JDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnDangMuon;
    private javax.swing.JButton btnQuaHan;
    private javax.swing.JButton btnThoat;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXacNhanTra;
    private javax.swing.JComboBox<String> cboTrangThai;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblSach;
    private javax.swing.JTextField txtMaPhieuMuon;
    // End of variables declaration//GEN-END:variables
}
