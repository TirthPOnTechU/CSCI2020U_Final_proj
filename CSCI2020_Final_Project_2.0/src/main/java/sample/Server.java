package sample;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread{
    ServerSocket server;
    ArrayList<clientHandler> clientList; //list of clientHandlers working(clients online)
    ServerFxApp serverfx;

    public Server(int port, ServerFxApp serverfx) throws IOException {
        server=new ServerSocket(port); //make new Socket for all clients to connect to
        clientList=new ArrayList<>();
        this.serverfx=serverfx;

    }

    public void run() {
        try {
            recieveClients();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void recieveClients() throws IOException { //method that accepts new Client connection requests to the Server socket and manages them
        Socket client;
        while (true){
            client=server.accept(); //accept new Client connection
            //System.out.println("ONLINE: "+client);


            clientHandler handleClient=new clientHandler(client, this, this.serverfx); //make new thread to handle this Client Socket's process
            clientList.add(handleClient);
            handleClient.start();
        }
    }
}
