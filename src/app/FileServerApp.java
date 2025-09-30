package app;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServerApp extends JFrame {
    private final JTextField txtPort = new JTextField("5555", 6);
    private final JButton btnStart = Utils.primary("Start Server");
    private final JLabel lblStatus = Utils.meta("Idle");
    private final FileTransferPanel panel = new FileTransferPanel(false); // server mode

    private ServerSocket serverSocket;
    private volatile boolean running = false;

    public FileServerApp() {
        super("TCP File Server");
        Utils.installGlobalFonts();

        setContentPane(Utils.gradientBackground());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(16,16));

        // Top bar
        JPanel topBar = Utils.card();
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        left.setOpaque(false);
        JLabel title = Utils.title("TCP File Server");
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        right.setOpaque(false);
        styleField(txtPort);
        right.add(Utils.meta("Port:"));
        right.add(txtPort);
        right.add(btnStart);
        right.add(Utils.meta("Status:"));
        right.add(lblStatus);

        topBar.add(left, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        btnStart.addActionListener(e -> {
            if (!running) startServer();
            else stopServer();
        });
    }

    private void styleField(JTextField tf){
        tf.setColumns(6);
        tf.setBackground(new Color(255,255,255,25));
        tf.setForeground(Utils.TEXT_MAIN);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
    }

    private void startServer() {
        int port = Integer.parseInt(txtPort.getText().trim());
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            lblStatus.setText("Listening on " + port);
            btnStart.setText("Stop Server");

            new Thread(() -> {
                while (running) {
                    try {
                        Socket s = serverSocket.accept();
                        handleClient(s);
                    } catch (IOException e) {
                        if (running) e.printStackTrace();
                    }
                }
            }, "AcceptThread").start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Không mở được port: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopServer() {
        running = false;
        btnStart.setText("Start Server");
        lblStatus.setText("Stopped");
        try { if (serverSocket != null) serverSocket.close(); } catch (Exception ignore) {}
    }

    private void handleClient(Socket socket) {
        new Thread(() -> {
            TransferSession session = null;
            try {
                session = new TransferSession(socket);
                panel.setSession(session);

                String ctrl;
                while ((ctrl = session.readControlOrNull()) != null) {
                    if (ctrl.startsWith(Protocol.MSG_REQ + Protocol.SEP)) {
                        String[] parts = ctrl.split("\\|", 3);
                        String fname = parts[1];
                        long size   = Long.parseLong(parts[2]);

                        // HỘP THOẠI XÁC NHẬN NHẬN FILE
                        int choice = JOptionPane.showConfirmDialog(this,
                                "Client muốn gửi:\n- Tên: " + fname + "\n- Kích thước: " + Utils.humanMB(size) +
                                        "\n\nBạn có CHẤP NHẬN nhận file này không?",
                                "Xác nhận nhận file", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                        boolean accept = (choice == JOptionPane.YES_OPTION);
                        session.replyAccept(accept);

                        if (!accept) {
                            // Ghi lịch sử thất bại (bên server từ chối)
                            panel.addHistoryRow(fname, size, false, 0, 0, false);
                            continue;
                        }

                        // Lưu vào ./received
                        File dir = new File("received");
                        if (!dir.exists()) dir.mkdirs();
                        File save = new File(dir, fname);

                        long t0 = System.nanoTime();
                        session.receiveFile(size, save, (done,total)->{
                            int v = (int)Math.min(1000, Math.round((done*1000.0)/Math.max(1,total)));
                            SwingUtilities.invokeLater(() -> panel.updateProgress(v));
                        });
                        String doneMsg = session.readUTF(); // expect DONE
                        long t1 = System.nanoTime();

                        double sec = (t1 - t0)/1e9;
                        double mb  = size/(1024.0*1024.0);
                        double mbs = mb/Math.max(0.001, sec);

                        // Nếu là .zip -> giải nén ra received/<basename>/
                        try {
                            if (fname.toLowerCase().endsWith(".zip")) {
                                String base = fname.substring(0, fname.length()-4);
                                File dest = new File(dir, base);
                                Utils.unzipTo(save, dest);
                            }
                        } catch (Exception unzipErr) {
                            System.out.println("Unzip error: " + unzipErr.getMessage());
                        }

                        panel.addHistoryRow(save.getName(), size, false, sec, mbs, true);
                    }
                }
            } catch (Exception e) {
                System.out.println("Client disconnected: " + e.getMessage());
            } finally {
                try { if (session != null) session.close(); } catch (Exception ignore) {}
                panel.setSession(null);
            }
        }, "ClientHandler").start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileServerApp().setVisible(true));
    }
}
