package sample;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class clientHandler extends Thread{ //This thread handles the process of a single Client connected to the Server
    Socket clientSocket;
    Server server;
    String username;
    public  DataOutputStream writer; //stream writes to Client
    public  DataInputStream listener; //Stream listens to Client
    ServerFxApp serverfx;

    //this database text file save all the messages
    public File messageDatabase = new File("C:\\Users\\patel\\Documents\\Year2_Docs\\csci2020u\\CSCI2020_Final_Project_2.0\\src\\main\\java\\saved\\messageDatabase.txt");


    public clientHandler(Socket client, Server server, ServerFxApp serverfx) throws IOException {
        clientSocket=client; //Client Socket this clientHandler handles
        this.server=server; //Server this client is connected to
        this.serverfx=serverfx;
        writer=new DataOutputStream(clientSocket.getOutputStream());
        listener=new DataInputStream(clientSocket.getInputStream());
    }


    //this function save the new message to the database which is passed in as a parameter
    public void saveToMessageDatabase(String newMessage) throws IOException {
        //this write to file, while appending the new text to the file
        FileWriter fileWriter = new FileWriter(messageDatabase,true);
        fileWriter.write(newMessage);
        fileWriter.write("\n");
        fileWriter.close();
    }


    public void run(){
        try {
            getUsername();
            onlineUser();
            clientHandle();
            offlineUser();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                //Close any open streams and display final disconnection message
                writer.close();
                listener.close();
                serverfx.putServerInfo("CONNECTION TERMINATED: "+clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clientHandle() throws IOException, InterruptedException { //This method manages interaction between Server and client
        String data;
        while((data=listener.readUTF())!=null){ //listen infinitely to incoming messages from the client
            serverfx.putServerInfo("MESSAGE FROM: "+clientSocket);
            if(data.equals("quit")){
                serverfx.putServerInfo("connection termination request received");
                writer.writeUTF(data); //if Client wants to quit connection, send termination message back (required in order to close any open streams and Socket connection from client side before closing from Server side)
                break;
            }

            for (clientHandler client: server.clientList){ //Send message to all the currently online Clients
                client.sendMessage(username+": "+data);
            }
            //saves message to database after being sent
            saveToMessageDatabase(username+": "+data);

            serverfx.putServerInfo("message sent to everyone");
        }
        //Close Server-side streams and connections
        writer.close();
        listener.close();
        clientSocket.close();
    }

    public void offlineUser() throws IOException { //method notifies all currently Online Clients about Client that just went Offline
        serverfx.putServerInfo("OFFLINE: "+this.clientSocket+" ("+this.username+")");
        server.clientList.remove(this); //remove client from list of clients ONLINE

        //Notify all currently Online clients about this client's status
        for (clientHandler client: server.clientList){
            client.sendMessage("OFFLINE: "+username);
        }

        serverfx.putServerInfo("offline notification sent");
    }

    public void onlineUser() throws IOException { //method notifies all currently Online Clients about the new Online Client
        Scanner scan = new Scanner(messageDatabase);
        while(scan.hasNextLine()){
            String line =  scan.nextLine();
            if(line.equals("")){
                continue;
            }
            else{
                sendMessage(line);
            }
        }
        scan.close();
        serverfx.putServerInfo("ONLINE: "+this.clientSocket+" ("+this.username+")");
        //Notify new Client about all currently Online clients
        for (clientHandler client: server.clientList){
            if(client==this){
                continue;
            }
            this.sendMessage("ONLINE: "+client.username);
        }

        //Notify all currently Online clients about new Client
        for (clientHandler client: server.clientList){
            client.sendMessage("ONLINE: "+username);
        }

        serverfx.putServerInfo("online notification sent");
    }
    public void getUsername() throws IOException { //method gets the username of client trying to access it
        String username;
        username=listener.readUTF();
        this.username=username;
    }

    public void sendMessage(String message) throws IOException { //This method sends a incoming message to the Client
        writer.writeUTF(message);
    }

}
