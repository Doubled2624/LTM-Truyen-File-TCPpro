package app;

import java.io.*;
import java.net.Socket;

public class TransferSession {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;

    public interface Progress { void onProgress(long done, long total); }

    public TransferSession(Socket socket) throws IOException {
        this.socket = socket;
        this.in  = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public Socket getSocket() { return socket; }

    /** Client: gửi file (đã có handshake: REQ -> chờ ACPT/REJ) */
    public boolean sendFile(File file, Progress cb) throws IOException {
        String header = Protocol.MSG_REQ + Protocol.SEP + file.getName() + Protocol.SEP + file.length();
        out.writeUTF(header);
        out.flush();

        String resp = in.readUTF();
        if (!Protocol.MSG_ACPT.equals(resp)) return false;

        long total = file.length();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[64 * 1024];
            long sent = 0;
            int r;
            while ((r = fis.read(buf)) != -1) {
                out.write(buf, 0, r);
                sent += r;
                if (cb != null) cb.onProgress(sent, total);
            }
            out.flush();
        }
        out.writeUTF(Protocol.MSG_DONE);
        out.flush();
        return true;
    }

    /** Server: đọc control message; null nếu ngắt kết nối */
    public String readControlOrNull() throws IOException {
        try { return in.readUTF(); }
        catch (EOFException eof) { return null; }
    }

    /** Server: nhận file kích thước size vào target */
    public void receiveFile(long size, File target, Progress cb) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(target)) {
            byte[] buf = new byte[64 * 1024];
            long got = 0;
            while (got < size) {
                int need = (int)Math.min(buf.length, size-got);
                int r = in.read(buf,0,need);
                if (r == -1) throw new EOFException("Stream ended unexpectedly");
                fos.write(buf,0,r);
                got += r;
                if (cb != null) cb.onProgress(got, size);
            }
        }
    }

    /** Server: trả lời Accept/Reject cho REQ */
    public void replyAccept(boolean accept) throws IOException {
        out.writeUTF(accept ? Protocol.MSG_ACPT : Protocol.MSG_REJ);
        out.flush();
    }

    public String readUTF() throws IOException { return in.readUTF(); }
    public void close() { try { socket.close(); } catch(Exception ignore){} }
}
