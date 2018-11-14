
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class TestServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        try(ServerSocket server = new ServerSocket(3345)){
            Socket client = server.accept();
            System.out.println("Connection accepted!");
            System.out.println(server.getInetAddress());
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            System.out.println("DataOutputStream  created");

            // канал чтения из сокета
            DataInputStream in = new DataInputStream(client.getInputStream());
            System.out.println("DataInputStream created");

            while(!client.isClosed()){
                System.out.println("Server reading from channel");
                String entry = in.readUTF();
                if(entry.equalsIgnoreCase("quit")){
                    System.out.println("TestClient initialize connections suicide ...");
                    out.writeUTF("Server reply - " + entry + " - OK");
                    out.flush();
                    Thread.sleep(3000);
                    break;
                }
                URL url = new URL("https://" + entry);
                InputStream is = url.openStream();
                Scanner scanner = new Scanner(is);
                StringBuilder answer = new StringBuilder();
                while (scanner.hasNext()) answer.append(scanner.nextLine());

                System.out.println("READ from client message - " + entry);
                System.out.println("Server try writing to channel");

                out.writeUTF("Server reply - " + answer.toString() + " - OK");
                System.out.println("Server Wrote message to client.");
                out.flush();

//                System.out.println("TestClient disconnected");
//                System.out.println("Closing connections & channels.");
//                // закрываем сначала каналы сокета !
//                in.close();
//                out.close();
//                // потом закрываем сам сокет общения на стороне сервера!
//                client.close();
//                System.out.println("Closing connections & channels - DONE.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}