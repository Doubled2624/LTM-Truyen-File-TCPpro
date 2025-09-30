package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;

public class FileTransferPanel extends JPanel {
    private final JLabel lblConn = Utils.meta("Disconnected");
    private final JLabel lblPeer = Utils.meta("-");
    private final JLabel lblTime = Utils.meta("-");
    private final JLabel lblSpeed= Utils.meta("-");
    private final JProgressBar bar = new JProgressBar(0, 1000);

    private File selected;
    private TransferSession session;
    private final JLabel lblSel  = new JLabel("Chưa chọn file/thư mục");
    private final JLabel lblSize = Utils.meta("-");
    private final JLabel preview = new JLabel("Kéo & thả file/thư mục vào đây", SwingConstants.CENTER);

    private final JButton btnPickFile   = Utils.ghost("Chọn file…");
    private final JButton btnPickFolder = Utils.ghost("Chọn thư mục…");
    private final JButton btnClear      = Utils.ghost("Bỏ chọn");
    private final JButton btnSend       = Utils.primary("Gửi");

    private final DefaultTableModel historyModel = new DefaultTableModel(
            new Object[]{"Thời gian", "Tên", "Kích thước", "Hành động", "Thời gian", "Tốc độ", "Trạng thái"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable historyTable = new JTable(historyModel);

    public FileTransferPanel(boolean isClient) {
        setOpaque(false);
        setLayout(new BorderLayout(16,16));
        setBorder(new EmptyBorder(10,10,10,10));

        // ===== Header =====
        JPanel header = Utils.card();
        JPanel grid = new JPanel(new GridLayout(2,2,10,6));
        grid.setOpaque(false);

        JLabel lblStatusTitle = new JLabel("Kết nối:");
        lblStatusTitle.setFont(lblStatusTitle.getFont().deriveFont(Font.BOLD, 14f));
        lblStatusTitle.setForeground(Utils.ACCENT);
        grid.add(lblStatusTitle); grid.add(lblConn);

        JLabel lblPeerTitle = new JLabel("Đối tác:");
        lblPeerTitle.setFont(lblPeerTitle.getFont().deriveFont(Font.BOLD, 14f));
        lblPeerTitle.setForeground(Utils.ACCENT);
        grid.add(lblPeerTitle); grid.add(lblPeer);

        header.add(Utils.title("Thông tin kết nối"), BorderLayout.NORTH);
        header.add(grid, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // ===== Center =====
        if (isClient) {
            JPanel center = new JPanel(new GridLayout(1,2,16,16));
            center.setOpaque(false);

            JPanel left = Utils.card();
            preview.setForeground(Utils.TEXT_SUB);
            preview.setHorizontalAlignment(SwingConstants.CENTER);
            preview.setPreferredSize(new Dimension(320,240));
            left.add(Utils.title("Xem trước / Ghi chú"), BorderLayout.NORTH);
            left.add(preview, BorderLayout.CENTER);

            JPanel right = Utils.card();
            JPanel info = new JPanel(new GridLayout(4,1,6,6));
            info.setOpaque(false);
            info.add(row("Tên:", lblSel));
            info.add(row("Kích thước:", lblSize));
            info.add(row("Thời gian gửi:", lblTime));
            info.add(row("Tốc độ:", lblSpeed));

            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
            actions.setOpaque(false);
            actions.add(btnPickFile);
            actions.add(btnPickFolder);
            actions.add(btnClear);
            actions.add(btnSend);

            Utils.beautifyProgress(bar);
            JPanel pg = new JPanel(new BorderLayout());
            pg.setOpaque(false);
            pg.add(bar, BorderLayout.CENTER);

            right.add(info, BorderLayout.NORTH);
            right.add(pg, BorderLayout.CENTER);
            right.add(actions, BorderLayout.SOUTH);

            center.add(left);
            center.add(right);
            add(center, BorderLayout.CENTER);

            btnPickFile.addActionListener(e -> pickFile());
            btnPickFolder.addActionListener(e -> pickFolder());
            btnClear.addActionListener(e -> clearSelection());
            btnSend.addActionListener(e -> { if (session!=null && selected!=null) sendEntryAsync(); });

            // Drag & drop file/thư mục
            setTransferHandler(new TransferHandler() {
                @SuppressWarnings("unchecked")
                public boolean importData(TransferSupport support) {
                    try {
                        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) return false;
                        List<File> files = (List<File>) support.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                        if (!files.isEmpty()) setSelectedEntry(files.get(0));
                        return true;
                    } catch (Exception ex) { return false; }
                }
                public boolean canImport(TransferSupport support) {
                    return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
                }
            });
        } else {
            JPanel center = Utils.card();
            Utils.beautifyProgress(bar);
            center.add(Utils.title("Tiến trình nhận"), BorderLayout.NORTH);
            center.add(bar, BorderLayout.CENTER);
            add(center, BorderLayout.CENTER);
        }

        // ===== History =====
        JPanel historyPanel = Utils.card();
        historyPanel.add(Utils.title("Lịch sử truyền"), BorderLayout.NORTH);
        historyTable.setFillsViewportHeight(true);
        historyTable.setRowHeight(22);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.getTableHeader().setReorderingAllowed(false);

        // Renderer: toàn bộ chữ đen để dễ nhìn
        historyTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setForeground(Color.BLACK);
                return c;
            }
        });

        JScrollPane sp = new JScrollPane(historyTable);
        sp.setBorder(BorderFactory.createEmptyBorder());
        historyPanel.add(sp, BorderLayout.CENTER);

        add(historyPanel, BorderLayout.SOUTH);
    }

    private JPanel row(String k, JComponent v) {
        JPanel r = new JPanel(new BorderLayout());
        r.setOpaque(false);
        JLabel l = new JLabel(k); l.setForeground(Utils.TEXT_SUB);
        if (v instanceof JLabel vv) vv.setForeground(Utils.TEXT_MAIN);
        r.add(l, BorderLayout.WEST); r.add(v, BorderLayout.CENTER);
        return r;
    }

    private void pickFile() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) setSelectedEntry(fc.getSelectedFile());
    }

    private void pickFolder() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) setSelectedEntry(fc.getSelectedFile());
    }

    private void clearSelection() {
        selected = null;
        lblSel.setText("Chưa chọn file/thư mục");
        lblSize.setText("-");
        preview.setIcon(null);
        preview.setText("Kéo & thả file/thư mục vào đây");
        bar.setValue(0);
        lblTime.setText("-");
        lblSpeed.setText("-");
        btnSend.setEnabled(false);
    }

    private void setSelectedEntry(File entry) {
        this.selected = entry;
        lblSel.setText(entry.getName());
        if (entry.isDirectory()) {
            long bytes = Utils.folderSize(entry);
            lblSize.setText(Utils.humanMB(bytes) + " (sẽ nén ZIP trước khi gửi)");
            preview.setIcon(null);
            preview.setText("Thư mục sẽ được nén ZIP để giữ cấu trúc & tối ưu dung lượng khi gửi.");
        } else {
            lblSize.setText(Utils.humanMB(entry.length()));
            if (Protocol.isImage(entry.getName())) {
                preview.setText(""); preview.setIcon(Utils.makeThumbnail(entry, 320, 240));
            } else {
                preview.setIcon(null); preview.setText("Không có preview cho loại này.");
            }
        }
        btnSend.setEnabled(session != null && session.getSocket().isConnected());
        bar.setValue(0);
        lblTime.setText("-");
        lblSpeed.setText("-");
    }

    public void setSession(TransferSession s) {
        this.session = s;
        boolean ok = (s != null && s.getSocket().isConnected());
        lblConn.setText(ok ? "Connected" : "Disconnected");
        lblConn.setForeground(ok ? new Color(0x5BE49B) : Utils.TEXT_SUB);
        lblPeer.setText(ok && s.getSocket().getInetAddress()!=null
                ? s.getSocket().getInetAddress().getHostAddress()+":"+s.getSocket().getPort()
                : "-");
        btnSend.setEnabled(ok && selected != null);
    }

    /** Client: gửi file/thư mục (thư mục sẽ nén ZIP tạm) */
    private void sendEntryAsync() {
        File entry = selected;
        bar.setValue(0);
        long t0 = System.nanoTime();

        new Thread(() -> {
            File toSend = entry;
            File tempZip = null;
            String displayName = entry.getName();
            long sendBytes;

            try {
                if (entry.isDirectory()) {
                    tempZip = Utils.zipFolderToTemp(entry);
                    toSend = tempZip;
                    displayName = entry.getName() + " (ZIP)";
                }
                sendBytes = toSend.length();

                boolean ok = session.sendFile(toSend, (done,total)->{
                    int v = (int)Math.min(1000, Math.round((done*1000.0)/Math.max(1,total)));
                    SwingUtilities.invokeLater(() -> bar.setValue(v));
                });

                long t1 = System.nanoTime();
                double sec = (t1 - t0)/1e9;
                double mb  = sendBytes/(1024.0*1024.0);
                double mbs = mb/Math.max(0.001, sec);

                final String finalName = displayName;
                SwingUtilities.invokeLater(() -> {
                    lblTime.setText(String.format("%.2f s", sec));
                    lblSpeed.setText(String.format("%.2f MB/s", mbs));
                    addHistoryRow(finalName, sendBytes, true, sec, mbs, ok);
                    JOptionPane.showMessageDialog(this,
                            ok ? "Gửi thành công!" : "Server từ chối hoặc lỗi.",
                            ok ? "Thành công" : "Thất bại",
                            ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                });
            } catch (Exception ex) {
                final String finalName = displayName;
                SwingUtilities.invokeLater(() -> {
                    addHistoryRow(finalName, 0, true, 0, 0, false);
                    JOptionPane.showMessageDialog(this, "Lỗi gửi: " + ex.getMessage(),
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                });
            } finally {
                if (tempZip != null) tempZip.delete();
            }
        },"SendThread").start();
    }

    public void addHistoryRow(String name, long size, boolean sent, double sec, double mbs, boolean success) {
        SwingUtilities.invokeLater(() -> {
            String time = java.time.LocalTime.now().withNano(0).toString();
            String status = success ? "Thành công" : "Thất bại";
            historyModel.addRow(new Object[]{
                    time,
                    name,
                    size > 0 ? Utils.humanMB(size) : "-",
                    sent ? "Đã gửi" : "Đã nhận",
                    sec > 0 ? String.format("%.2f s", sec) : "-",
                    mbs > 0 ? String.format("%.2f MB/s", mbs) : "-",
                    status
            });
            int last = historyModel.getRowCount() - 1;
            if (last >= 0) historyTable.scrollRectToVisible(historyTable.getCellRect(last,0,true));
        });
    }

    public void updateProgress(int v) {
        SwingUtilities.invokeLater(() -> bar.setValue(v));
    }
}
