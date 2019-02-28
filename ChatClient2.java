import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class ChatClient2 {
    private static Scanner scanner;
    private static DataOutputStream dos;
    private static DataInputStream dis;

    public static void main(String[] args) {
        new ChatClient2().runClient();

    }

    private static void runClient() {
        scanner = new Scanner(System.in);
        System.out.println("Skriv IP-adresse nr.: (Skriv \"0\", for at bruge standard)");
        String ipAddress  = scanner.next();
        if(ipAddress.equals("0")){
            ipAddress = "localhost";
        }
        System.out.println("Skriv port nr.: (Skriv \"0\", for at bruge standard)");
        int port = scanner.nextInt();
        if(port < 1){
            port = 1337;
        }
        try {
            Socket socket = new Socket(ipAddress,port);
            System.out.println("Forbindelse oprettet til server");
            System.out.println("Server IP adresse er: " + ipAddress);
            System.out.println("Server port er: " + port);

            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
            scanner = new Scanner(System.in);

            Thread tSend = new Thread(){
                @Override
                public void run(){
                    while (true){
                        try {
                            System.out.println("Skriv besked:");
                            dos.writeUTF("Client2: " + scanner.nextLine());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            tSend.start();

            Thread tReceive = new Thread(){
                @Override
                public  void run(){
                    while (true){
                        try {
                            System.out.println(dis.readUTF());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            tReceive.start();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Forbindelse mislykkeds!");
        }
    }
}