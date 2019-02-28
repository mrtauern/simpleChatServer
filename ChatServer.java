import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {
    private static List<Socket> sockets;

    public static void main(String[] args) {
        sockets = new ArrayList<>();

        new ChatServer().runServer();
    }

    private static void runServer() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Skriv port nr.: (Skriv \"0\", for at bruge standard)");
        int port = scanner.nextInt();
        if(port < 1){
            port = 1337;
        }

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Din IP adresse er: "+InetAddress.getLocalHost().getHostAddress());
            System.out.println("Din port er: " + port);
            while (true){
                System.out.println("Venter på at oprette forbindelse til client...");
                Socket socket = serverSocket.accept();
                System.out.println(":::::Forbindelse oprettet:::::");
                System.out.println("Local IP-address: " + socket.getLocalAddress());
                System.out.println("Internet IP-address: " + socket.getLocalAddress().getHostAddress());
                System.out.println("Local Port: " + socket.getLocalPort());
                System.out.println("internet Port: " + socket.getPort());
                System.out.println("::::::::::::::::::::::::::::::");
                sockets.add(socket);
                Thread t = new Thread() {
                    @Override
                    public void run(){
                        System.out.println("Thread is running...");

                        try {
                            handleSocket(socket);
                            /*try (Scanner scanner = new Scanner(socket.getInputStream())) {
                                while (scanner.hasNextLine()) {
                                    System.out.println(scanner.nextLine() + "\n");
                                }
                            }*/
                            for (int i = 0; i < sockets.size(); i++){
                                if(sockets.get(i) == socket){
                                    sockets.remove(i);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.err.println("Forbindelse lukket!");
                    }
                };
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleSocket(Socket socket) throws IOException {
        while (true) {
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos;
            String message = dis.readUTF();
            System.out.println(message);
            for(Socket s: sockets) {
                if(s != socket) {
                    dos = new DataOutputStream(s.getOutputStream());
                    dos.writeUTF(message);
                }
            }
        }
    }

}
