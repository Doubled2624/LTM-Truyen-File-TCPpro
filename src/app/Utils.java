package app;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class Utils {
    public static final Color BG_GRAD_TOP    = new Color(18, 22, 38);
    public static final Color BG_GRAD_BOTTOM = new Color(9, 10, 18);
    public static final Color CARD_BG        = new Color(255, 255, 255, 18);
    public static final Color CARD_STROKE    = new Color(255, 255, 255, 36);
    public static final Color PRIMARY        = new Color(108, 99, 255);
    public static final Color PRIMARY_HOVER  = new Color(130, 121, 255);
    public static final Color TEXT_MAIN      = new Color(235, 238, 245);
    public static final Color TEXT_SUB       = new Color(170, 176, 190);
    public static final Color ACCENT         = new Color(0x00E0B8);

    private Utils() {}

    public static void installGlobalFonts() {
        Font base = new Font("Segoe UI", Font.PLAIN, 13);
        UIManager.put("Label.font", base);
        UIManager.put("Button.font", base.deriveFont(Font.BOLD));
        UIManager.put("TextField.font", base);
        UIManager.put("ProgressBar.font", base);
        UIManager.put("OptionPane.messageForeground", TEXT_MAIN);
        UIManager.put("OptionPane.background", new Color(30,30,40));
        UIManager.put("Panel.background", new Color(30,30,40));
    }

    public static JPanel gradientBackground() {
        return new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, BG_GRAD_TOP, 0, getHeight(), BG_GRAD_BOTTOM);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }

    public static JPanel card() {
        JPanel p = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(CARD_STROKE);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.setLayout(new BorderLayout(12,12));
        return p;
    }

    public static JLabel title(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_MAIN);
        l.setFont(l.getFont().deriveFont(Font.BOLD, 18f));
        return l;
    }
    public static JLabel meta(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT_SUB);
        return l;
    }

    public static JButton primary(String text) {
        JButton b = new JButton(text);
        styleButton(b, PRIMARY, PRIMARY_HOVER, Color.WHITE);
        return b;
    }
    public static JButton ghost(String text) {
        JButton b = new JButton(text);
        styleButton(b, new Color(255,255,255,30), new Color(255,255,255,60), TEXT_MAIN);
        b.setBorder(BorderFactory.createLineBorder(new Color(255,255,255,50), 1, true));
        return b;
    }
    private static void styleButton(JButton b, Color base, Color hover, Color fg) {
        b.setFocusPainted(false);
        b.setForeground(fg);
        b.setBackground(base);
        b.setBorder(BorderFactory.createEmptyBorder(10,18,10,18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 13f));
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI(){
            @Override public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Rectangle r = c.getBounds();
                Color fill = b.getModel().isRollover() ? PRIMARY_HOVER : base;
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, r.width, r.height, 16, 16);
                g2.dispose();
                super.paint(g, c);
            }
        });
    }

    public static void beautifyProgress(JProgressBar bar) {
        bar.setOpaque(false);
        bar.setForeground(ACCENT);
        bar.setBackground(new Color(255,255,255,24));
        bar.setUI(new BasicProgressBarUI() {
            @Override protected void paintDeterminate(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = c.getWidth(), h = c.getHeight();
                g2.setColor(new Color(255,255,255,30));
                g2.fillRoundRect(0, 0, w, h, 12, 12);
                int fill = (int) Math.round(((JProgressBar)c).getPercentComplete() * w);
                g2.setPaint(new GradientPaint(0,0, ACCENT, w,0, PRIMARY));
                g2.fillRoundRect(0, 0, Math.max(8, fill), h, 12, 12);
                g2.dispose();
            }
        });
        bar.setBorder(BorderFactory.createEmptyBorder(3,3,3,3));
        bar.setPreferredSize(new Dimension(0, 14));
    }

    public static String humanMB(long bytes) {
        double mb = bytes / (1024.0 * 1024.0);
        return String.format("%,.2f MB", mb);
    }

    public static ImageIcon makeThumbnail(File file, int maxW, int maxH) {
        try {
            BufferedImage img = ImageIO.read(file);
            if (img == null) return null;
            int w = img.getWidth(), h = img.getHeight();
            double s = Math.min(maxW/(double)w, maxH/(double)h);
            if (s > 1) s = 1;
            int nw = Math.max(1, (int)(w*s));
            int nh = Math.max(1, (int)(h*s));
            Image scaled = img.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) { return null; }
    }

    public static JFileChooser chooserForSave(String defaultName) {
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(defaultName));
        return fc;
    }

    // ===== Folder helpers =====
    public static long folderSize(File dir) {
        if (dir == null || !dir.exists()) return 0;
        if (dir.isFile()) return dir.length();
        long sum = 0;
        File[] list = dir.listFiles();
        if (list == null) return 0;
        for (File f : list) sum += folderSize(f);
        return sum;
    }

    /** Nén thư mục -> file ZIP tạm */
    public static File zipFolderToTemp(File folder) throws IOException {
        String base = folder.getName();
        File temp = Files.createTempFile(base + "_", ".zip").toFile();
        try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(temp)))) {
            zipWalk(folder, folder, zos);
        }
        return temp;
    }

    private static void zipWalk(File root, File src, ZipOutputStream zos) throws IOException {
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            if (files == null || files.length == 0) {
                String name = relPath(root, src) + "/";
                zos.putNextEntry(new ZipEntry(name));
                zos.closeEntry();
            } else {
                for (File f : files) zipWalk(root, f, zos);
            }
        } else {
            String entryName = relPath(root, src);
            zos.putNextEntry(new ZipEntry(entryName));
            try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(src))) {
                byte[] buf = new byte[64 * 1024];
                int r;
                while ((r = in.read(buf)) != -1) zos.write(buf, 0, r);
            }
            zos.closeEntry();
        }
    }

    private static String relPath(File root, File f) {
        String rp = root.toURI().relativize(f.toURI()).getPath();
        return rp.isEmpty() ? f.getName() : rp;
    }

    /** Giải nén file ZIP vào thư mục đích */
    public static void unzipTo(File zip, File destDir) throws IOException {
        if (!destDir.exists()) destDir.mkdirs();
        try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)))) {
            ZipEntry e;
            byte[] buf = new byte[64 * 1024];
            while ((e = zis.getNextEntry()) != null) {
                File out = new File(destDir, e.getName());
                if (e.isDirectory()) {
                    out.mkdirs();
                } else {
                    File parent = out.getParentFile();
                    if (!parent.exists()) parent.mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(out)) {
                        int r;
                        while ((r = zis.read(buf)) != -1) fos.write(buf, 0, r);
                    }
                }
                zis.closeEntry();
            }
        }
    }
}
