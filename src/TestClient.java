
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.util.Arrays;

public class TestClient {
    public static void main(String[] args) throws InterruptedException{
        try(Socket socket = new Socket("localhost", 3345);
            BufferedReader br =new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
            DataInputStream ois = new DataInputStream(socket.getInputStream())
        ){
            System.out.println(socket.getInetAddress());
            System.out.println("Client connected to socket.\n");
            System.out.println("Client writing channel = oos & reading channel = ois initialized.");
            while(!socket.isOutputShutdown()){
                if(br.ready()){
                    System.out.println("Client start writing in channel...");
                    Thread.sleep(1000);
                    String clientCommand = br.readLine();
                    oos.writeUTF(clientCommand);
                    oos.flush();
                    System.out.println("Clien sent message " + clientCommand + " to server.");
                    Thread.sleep(1000);
                    if(clientCommand.equalsIgnoreCase("quit")){
                        System.out.println("Client kill connections");
                        Thread.sleep(2000);
                        if(ois.available() != -1)     {
                            System.out.println("reading...");
                            String in = ois.readUTF();
                            System.out.println(in);
                        }
                        break;
                    }
                    System.out.println("Client sent message & start waiting for data from server...");
                    Thread.sleep(2000);
                    if(ois.available() != -1)     {
                        System.out.println("reading...");
                        String in = ois.readUTF();
                        in = in.replaceAll(">", ">,,,,,,");
                        String [] printData = in.split(",,,,,,");
                        for (String i : printData) System.out.println(i);
                    }
                }
            }
            System.out.println("Closing connections & channels on clentSide - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}