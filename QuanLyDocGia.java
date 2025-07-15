package poly.quanlythuvien.ui;

import poly.quanlythuvien.dao.DocGiaDAO;
import poly.quanlythuvien.daoimpl.DocGiaDAOImpl;
import poly.quanlythuvien.entity.DocGia;
import poly.quanlythuvien.util.XDate;
import poly.quanlythuvien.util.XDialog;
import poly.quanlythuvien.util.TimeRange;
import javax.swing.ButtonGroup;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.util.List;

public class QuanLyDocGia extends javax.swing.JDialog {
    private DefaultTableModel tableModel;
    private DocGiaDAO dao;
    private List<DocGia> docGiaList; // Danh sách độc giả để điều hướng
    private int currentIndex = -1; // Chỉ số bản ghi hiện tại

    public QuanLyDocGia(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        init();
    }

    private void init() {
        setTitle("Quản Lý Độc Giả");
        tableModel = (DefaultTableModel) tblClient.getModel();
        tableModel.setRowCount(0);
        dao = new DocGiaDAOImpl();

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(rdoActive);
        statusGroup.add(rdoPause);
        statusGroup.add(rdoPause1);

        cboTimeRanges.removeAllItems();
        cboTimeRanges.addItem("Hôm nay");
        cboTimeRanges.addItem("Tuần này");
        cboTimeRanges.addItem("Tháng này");
        cboTimeRanges.addItem("Quý này");
        cboTimeRanges.addItem("Năm nay");

        btnfilter.addActionListener(evt -> filter());
        btnCreate.addActionListener(evt -> create());
        btnUpdate.addActionListener(evt -> update());
        btnDelete.addActionListener(evt -> delete());
        btnClear.addActionListener(evt -> clear());
        tblClient.getSelectionModel().addListSelectionListener(evt -> selectRow());

        loadTable();
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        try {
            docGiaList = dao.findAllDocGia(); // Lưu danh sách để điều hướng
            List<DocGia> list = dao.findAllDocGia();
            for (DocGia dg : list) {
                tableModel.addRow(new Object[]{
                    dg.getMaDocGia(),
                    dg.getHoTen(),
                    dg.getNgaySinh() != null ? XDate.format(dg.getNgaySinh(), "dd/MM/yyyy") : "",
                    dg.getDiaChi(),
                    dg.getSoDienThoai(),
                    dg.getNgayCapThe() != null ? XDate.format(dg.getNgayCapThe(), "dd/MM/yyyy") : "",
                    dg.getNgayHetHan() != null ? XDate.format(dg.getNgayHetHan(), "dd/MM/yyyy") : "",
                    dg.getTrangThai()
                });
            }
        } catch (Exception e) {
            XDialog.alert(this, "Không thể tải danh sách độc giả: " + e.getMessage(), "Lỗi");
        }
    }

    private void filter() {
        String timeRange = (String) cboTimeRanges.getSelectedItem();
        TimeRange range;
        switch (timeRange) {
            case "Hôm nay": range = TimeRange.today(); break;
            case "Tuần này": range = TimeRange.thisWeek(); break;
            case "Tháng này": range = TimeRange.thisMonth(); break;
            case "Quý này": range = TimeRange.thisQuarter(); break;
            case "Năm nay": range = TimeRange.thisYear(); break;
            default: range = TimeRange.thisMonth();
        }
        Date begin = XDate.parse(txtBegin.getText().trim(), "dd/MM/yyyy");
        Date end = XDate.parse(txtEnd.getText().trim(), "dd/MM/yyyy");
        if (begin != null && end != null) {
            range.setBegin(begin);
            range.setEnd(end);
        } else if (begin == null || end == null) {
            XDialog.alert(this, "Định dạng ngày không hợp lệ! (dd/MM/yyyy)", "Lỗi");
            return;
        }
        try {
            List<DocGia> list = dao.findDocGiaByTimeRange(range.getBegin(), range.getEnd());
            tableModel.setRowCount(0);
            for (DocGia dg : list) {
                tableModel.addRow(new Object[]{
                    dg.getMaDocGia(),
                    dg.getHoTen(),
                    dg.getNgaySinh() != null ? XDate.format(dg.getNgaySinh(), "dd/MM/yyyy") : "",
                    dg.getDiaChi(),
                    dg.getSoDienThoai(),
                    dg.getNgayCapThe() != null ? XDate.format(dg.getNgayCapThe(), "dd/MM/yyyy") : "",
                    dg.getNgayHetHan() != null ? XDate.format(dg.getNgayHetHan(), "dd/MM/yyyy") : "",
                    dg.getTrangThai()
                });
            }
        } catch (Exception e) {
            XDialog.alert(this, "Lỗi lọc danh sách: " + e.getMessage(), "Lỗi");
        }
    }

    private void create() {
        DocGia docGia = getFormData();
        if (docGia == null) return;

        try {
            if (dao.createDocGia(docGia)) {
                XDialog.alert(this, "Thêm độc giả thành công!", "Thông báo");
                loadTable();
                clear();
            } else {
                XDialog.alert(this, "Thêm độc giả thất bại!", "Lỗi");
            }
        } catch (Exception e) {
            XDialog.alert(this, "Lỗi khi thêm độc giả: " + e.getMessage(), "Lỗi");
        }
    }

    private void update() {
        DocGia docGia = getFormData();
        if (docGia == null) return;

        try {
            if (dao.updateDocGia(docGia)) {
                XDialog.alert(this, "Cập nhật độc giả thành công!", "Thông báo");
                loadTable();
            } else {
                XDialog.alert(this, "Cập nhật độc giả thất bại!", "Lỗi");
            }
        } catch (Exception e) {
            XDialog.alert(this, "Lỗi khi cập nhật độc giả: " + e.getMessage(), "Lỗi");
        }
    }

    private void delete() {
        String maDocGia = txtMadocgia.getText().trim();
        if (maDocGia.isEmpty()) {
            XDialog.alert(this, "Vui lòng nhập mã độc giả!", "Thông báo");
            return;
        }

        if (XDialog.confirm(this, "Bạn có chắc muốn xóa độc giả này?", "Xác nhận")) {
            try {
                if (dao.deleteDocGia(maDocGia)) {
                    XDialog.alert(this, "Xóa độc giả thành công!", "Thông báo");
                    loadTable();
                    clear();
                } else {
                    XDialog.alert(this, "Xóa độc giả thất bại!", "Lỗi");
                }
            } catch (Exception e) {
                XDialog.alert(this, "Lỗi khi xóa độc giả: " + e.getMessage(), "Lỗi");
            }
        }
    }

    private void clear() {
        txtMadocgia.setText("");
        txtHoten.setText("");
        txtNgaysinh.setText("");
        txtDiachi.setText("");
        txtSdt.setText("");
        txtNgaycapthe.setText("");
        txtNgayhethan.setText("");
        rdoActive.setSelected(true);
    }

    private void selectRow() {
        int row = tblClient.getSelectedRow();
        if (row >= 0) {
            txtMadocgia.setText(tableModel.getValueAt(row, 0) != null ? tableModel.getValueAt(row, 0).toString() : "");
            txtHoten.setText(tableModel.getValueAt(row, 1) != null ? tableModel.getValueAt(row, 1).toString() : "");
            txtNgaysinh.setText(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
            txtDiachi.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
            txtSdt.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
            txtNgaycapthe.setText(tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "");
            txtNgayhethan.setText(tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "");
            String trangThai = tableModel.getValueAt(row, 7) != null ? tableModel.getValueAt(row, 7).toString() : "";
            if ("Hoạt động".equals(trangThai)) {
                rdoActive.setSelected(true);
            } else if ("Ngưng".equals(trangThai)) {
                rdoPause.setSelected(true);
            } else {
                rdoPause1.setSelected(true);
            }
        }
    }

    private DocGia getFormData() {
        String maDocGia = txtMadocgia.getText().trim();
        String hoTen = txtHoten.getText().trim();
        String ngaySinhStr = txtNgaysinh.getText().trim();
        String diaChi = txtDiachi.getText().trim();
        String soDienThoai = txtSdt.getText().trim();
        String ngayCapTheStr = txtNgaycapthe.getText().trim();
        String ngayHetHanStr = txtNgayhethan.getText().trim();

        if (maDocGia.isEmpty() || hoTen.isEmpty() || ngaySinhStr.isEmpty() ||
            diaChi.isEmpty() || soDienThoai.isEmpty() || ngayCapTheStr.isEmpty() ||
            ngayHetHanStr.isEmpty()) {
            XDialog.alert(this, "Vui lòng điền đầy đủ thông tin!", "Thông báo");
            return null;
        }

        Date ngaySinh = XDate.parse(ngaySinhStr, "dd/MM/yyyy");
        Date ngayCapThe = XDate.parse(ngayCapTheStr, "dd/MM/yyyy");
        Date ngayHetHan = XDate.parse(ngayHetHanStr, "dd/MM/yyyy");

        if (ngaySinh == null || ngayCapThe == null || ngayHetHan == null) {
            XDialog.alert(this, "Định dạng ngày không hợp lệ! (dd/MM/yyyy)", "Lỗi");
            return null;
        }
        if (ngayHetHan.before(ngayCapThe)) {
            XDialog.alert(this, "Ngày hết hạn phải sau ngày cấp thẻ!", "Lỗi");
            return null;
        }

        String trangThai = rdoActive.isSelected() ? "Hoạt động" :
                          rdoPause.isSelected() ? "Ngưng" : "Khoá tài khoản";

        return new DocGia(maDocGia, hoTen, ngaySinh, diaChi, soDienThoai,
                          ngayCapThe, ngayHetHan, trangThai);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtBegin = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        spQuanlydocgia = new javax.swing.JScrollPane();
        tblClient = new javax.swing.JTable();
        txtEnd = new javax.swing.JTextField();
        btnfilter = new javax.swing.JButton();
        cboTimeRanges = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtMadocgia = new javax.swing.JTextField();
        txtNgaysinh = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtDiachi = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtHoten = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtSdt = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnCreate = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        rdoActive = new javax.swing.JRadioButton();
        rdoPause = new javax.swing.JRadioButton();
        rdoPause1 = new javax.swing.JRadioButton();
        txtNgaycapthe = new javax.swing.JTextField();
        txtNgayhethan = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Từ ngày:");

        txtBegin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBeginActionPerformed(evt);
            }
        });

        jLabel3.setText("Đến ngày:");

        tblClient.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã độc giả	", "Họ tên	", "Ngày sinh	", "Địa chỉ	", "Số điện thoại	", "Ngày cấp thẻ	", "Ngày hết hạn	", "Trạng thái thẻ"
            }
        ));
        spQuanlydocgia.setViewportView(tblClient);

        txtEnd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEndActionPerformed(evt);
            }
        });

        btnfilter.setText("Lọc");
        btnfilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfilterActionPerformed(evt);
            }
        });

        cboTimeRanges.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hôm nay", "Tuần này", "Tháng này", "Quý này", "Năm nay" }));
        cboTimeRanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTimeRangesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(spQuanlydocgia)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnfilter, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(159, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnfilter)
                    .addComponent(cboTimeRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(spQuanlydocgia, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        jTabbedPane1.addTab("DANH SÁCH", jPanel1);

        jLabel4.setText("Mã Độc Giả:");

        txtMadocgia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMadocgiaActionPerformed(evt);
            }
        });

        txtNgaysinh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgaysinhActionPerformed(evt);
            }
        });

        jLabel5.setText("Ngày Sinh:");

        txtDiachi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDiachiActionPerformed(evt);
            }
        });

        jLabel7.setText("Địa Chỉ:");

        txtHoten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHotenActionPerformed(evt);
            }
        });

        jLabel6.setText("Họ Tên:");

        jLabel16.setText("Số Điện Thoại:");

        txtSdt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSdtActionPerformed(evt);
            }
        });

        jLabel17.setText("Trạng thái thẻ:");

        btnCreate.setText("Tạo mới");
        btnCreate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateActionPerformed(evt);
            }
        });

        btnUpdate.setText("Cập nhật");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setText("Nhập mới");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        rdoActive.setText("Hoạt động");
        rdoActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoActiveActionPerformed(evt);
            }
        });

        rdoPause.setText("Ngưng");
        rdoPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPauseActionPerformed(evt);
            }
        });

        rdoPause1.setText("Khoá tài khoản");
        rdoPause1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdoPause1ActionPerformed(evt);
            }
        });

        txtNgaycapthe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgaycaptheActionPerformed(evt);
            }
        });

        txtNgayhethan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayhethanActionPerformed(evt);
            }
        });

        jLabel12.setText("Ngày hết hạn:");

        jLabel18.setText("Ngày cấp thẻ:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMadocgia)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNgaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 85, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtHoten)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDiachi, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(134, 134, 134))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNgaycapthe, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(79, 79, 79)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtNgayhethan, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(79, 79, 79)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(rdoActive)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoPause)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(rdoPause1)))))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCreate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnUpdate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDelete)
                .addGap(11, 11, 11)
                .addComponent(btnClear)
                .addGap(221, 221, 221))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMadocgia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addGap(3, 3, 3)
                        .addComponent(txtNgaysinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtHoten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(3, 3, 3)
                        .addComponent(txtDiachi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel16)
                        .addGap(3, 3, 3)
                        .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rdoActive)
                            .addComponent(rdoPause)
                            .addComponent(rdoPause1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(3, 3, 3)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNgaycapthe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNgayhethan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreate)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnClear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("BIỂU MẪU", jPanel2);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Quản Lý Độc Giả");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBeginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBeginActionPerformed
        // TODO add your handling code here:
        txtEnd.requestFocus();
    }//GEN-LAST:event_txtBeginActionPerformed

    private void btnfilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfilterActionPerformed
        // TODO add your handling code here:
        filter();
    }//GEN-LAST:event_btnfilterActionPerformed

    private void txtMadocgiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMadocgiaActionPerformed
        // TODO add your handling code here:
        txtHoten.requestFocus();
    }//GEN-LAST:event_txtMadocgiaActionPerformed

    private void txtDiachiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDiachiActionPerformed
        // TODO add your handling code here:
        txtSdt.requestFocus();
    }//GEN-LAST:event_txtDiachiActionPerformed

    private void txtHotenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHotenActionPerformed
        // TODO add your handling code here:
        txtNgaysinh.requestFocus();
    }//GEN-LAST:event_txtHotenActionPerformed

    private void btnCreateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateActionPerformed
        // TODO add your handling code here:
        create();
    }//GEN-LAST:event_btnCreateActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtNgayhethanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayhethanActionPerformed
        // TODO add your handling code here:
        rdoActive.requestFocus();
    }//GEN-LAST:event_txtNgayhethanActionPerformed

    private void txtEndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEndActionPerformed
        // TODO add your handling code here:
        filter();
    }//GEN-LAST:event_txtEndActionPerformed

    private void cboTimeRangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTimeRangesActionPerformed
        // TODO add your handling code here:
        String timeRange = (String) cboTimeRanges.getSelectedItem();
        if (timeRange == null) return;
        TimeRange range;
        switch (timeRange) {
            case "Hôm nay": range = TimeRange.today(); break;
            case "Tuần này": range = TimeRange.thisWeek(); break;
            case "Tháng này": range = TimeRange.thisMonth(); break;
            case "Quý này": range = TimeRange.thisQuarter(); break;
            case "Năm nay": range = TimeRange.thisYear(); break;
            default: range = TimeRange.thisMonth();
        }
        txtBegin.setText(XDate.format(range.getBegin(), "dd/MM/yyyy"));
        txtEnd.setText(XDate.format(range.getEnd(), "dd/MM/yyyy"));
        filter();
    
    }//GEN-LAST:event_cboTimeRangesActionPerformed

    private void txtNgaysinhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgaysinhActionPerformed
        // TODO add your handling code here:
        txtDiachi.requestFocus();
    }//GEN-LAST:event_txtNgaysinhActionPerformed

    private void txtSdtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSdtActionPerformed
        // TODO add your handling code here:
        txtNgaycapthe.requestFocus();
    }//GEN-LAST:event_txtSdtActionPerformed

    private void rdoActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoActiveActionPerformed
        // TODO add your handling code here:
        btnCreate.requestFocus();
    }//GEN-LAST:event_rdoActiveActionPerformed

    private void rdoPauseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPauseActionPerformed
        // TODO add your handling code here:
        btnCreate.requestFocus();
    }//GEN-LAST:event_rdoPauseActionPerformed

    private void rdoPause1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdoPause1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoPause1ActionPerformed

    private void txtNgaycaptheActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgaycaptheActionPerformed
        // TODO add your handling code here:
        txtNgayhethan.requestFocus();
    }//GEN-LAST:event_txtNgaycaptheActionPerformed

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
            java.util.logging.Logger.getLogger(QuanLyDocGia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyDocGia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyDocGia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyDocGia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                QuanLyDocGia dialog = new QuanLyDocGia(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCreate;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnfilter;
    private javax.swing.JComboBox<String> cboTimeRanges;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rdoActive;
    private javax.swing.JRadioButton rdoPause;
    private javax.swing.JRadioButton rdoPause1;
    private javax.swing.JScrollPane spQuanlydocgia;
    private javax.swing.JTable tblClient;
    private javax.swing.JTextField txtBegin;
    private javax.swing.JTextField txtDiachi;
    private javax.swing.JTextField txtEnd;
    private javax.swing.JTextField txtHoten;
    private javax.swing.JTextField txtMadocgia;
    private javax.swing.JTextField txtNgaycapthe;
    private javax.swing.JTextField txtNgayhethan;
    private javax.swing.JTextField txtNgaysinh;
    private javax.swing.JTextField txtSdt;
    // End of variables declaration//GEN-END:variables
}
