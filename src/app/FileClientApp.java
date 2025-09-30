package app;

import javax.swing.*;
import java.awt.*;
import java.net.Socket;

public class FileClientApp extends JFrame {
    private final JTextField txtHost = new JTextField("127.0.0.1", 12);
    private final JTextField txtPort = new JTextField("5555", 6);
    private final JButton btnConn = Utils.primary("Connect");
    private final JLabel lblStatus = Utils.meta("Not connected");

    private final FileTransferPanel panel = new FileTransferPanel(true); // client mode
    private TransferSession session;

    public FileClientApp() {
        super("TCP File Client");
        Utils.installGlobalFonts();

        setContentPane(Utils.gradientBackground());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(16,16));

        JPanel topBar = Utils.card();
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        left.setOpaque(false);
        JLabel title = Utils.title("TCP File Client");
        left.add(title);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        right.setOpaque(false);
        stylizeField(txtHost);
        stylizeField(txtPort);
        right.add(Utils.meta("Host:")); right.add(txtHost);
        right.add(Utils.meta("Port:")); right.add(txtPort);
        right.add(btnConn);
        right.add(Utils.meta("Status:")); right.add(lblStatus);

        topBar.add(left, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);

        btnConn.addActionListener(e -> {
            if (session == null) connect();
            else disconnect();
        });
    }

    private void stylizeField(JTextField tf){
        tf.setBackground(new Color(255,255,255,25));
        tf.setForeground(Utils.TEXT_MAIN);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
    }

    private void connect() {
        String host = txtHost.getText().trim();
        int port = Integer.parseInt(txtPort.getText().trim());
        try {
            Socket s = new Socket(host, port);
            session = new TransferSession(s);
            lblStatus.setText("Connected to " + host + ":" + port);
            btnConn.setText("Disconnect");
            panel.setSession(session);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không kết nối được: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            session = null;
        }
    }

    private void disconnect() {
        try { if (session != null) session.close(); } catch (Exception ignore) {}
        session = null;
        lblStatus.setText("Not connected");
        btnConn.setText("Connect");
        panel.setSession(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileClientApp().setVisible(true));
    }
}
