
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {

    public static void main(String[] args) throws Throwable {
        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            Socket s = ss.accept();
            System.err.println("Client accepted");
            new Thread(new SocketProcessor(s)).start();
        }
    }

    private static class SocketProcessor implements Runnable {

        private Socket s;
        private InputStream is;
        private OutputStream os;

        private SocketProcessor(Socket s) throws Throwable {
            this.s = s;
            this.is = s.getInputStream();
            this.os = s.getOutputStream();
        }

        public void run() {
            try {
                readInputHeaders();
                writeResponse("<html>" +
                            "<body>" +
                                "<h1>Hello from sumsung it school</h1>" +
                                "<p><b>Input your name:</b><br>\n" +
                        "   <input type=\"text\" size=\"40\">\n" +
                        "  </p>\n" +
                        "  <p><b>Which browser you prefer?:</b><Br>\n" +
                        "   <input type=\"radio\" name=\"browser\" value=\"ie\"> Internet Explorer<Br>\n" +
                        "   <input type=\"radio\" name=\"browser\" value=\"opera\"> Opera<Br>\n" +
                        "   <input type=\"radio\" name=\"browser\" value=\"firefox\"> Firefox<Br>\n" +
                        "  </p>\n" +
                        "  <p>Commit<Br>\n" +
                        "   <textarea name=\"comment\" cols=\"40\" rows=\"3\"></textarea></p>\n" +
                        "  <p><input type=\"submit\" value=\"send\">\n" +
                        "   <input type=\"reset\" value=\"clear\"></p>" +
                            "</body>" +
                        "</html>");
            } catch (Throwable ignored) {

            }
            System.err.println("Client processing finished");
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            os.write(result.getBytes());
            os.flush();
        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while(true) {
                String s = br.readLine();
                if(s == null || s.trim().length() == 0) {
                    break;
                }
            }
        }
    }
}