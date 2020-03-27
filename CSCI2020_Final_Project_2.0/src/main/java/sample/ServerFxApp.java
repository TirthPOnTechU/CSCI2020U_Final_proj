package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerFxApp extends Application {
    int port;
    TextArea ta;
    VBox pane;
    Stage mainStage;
    Server server;

    public ServerFxApp(int port){
        this.port=port;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //Server GUI
        ta=new TextArea();
        pane=new VBox();
        pane.getChildren().addAll(ta);
        Scene scene=new Scene(pane);
        mainStage=new Stage();
        mainStage.setTitle("Server");
        mainStage.setScene(scene);
        mainStage.show();

        //Create and start new server
        server=new Server(this.port, this);
        InvalidExitCheck exitCheck=new InvalidExitCheck();
        exitCheck.start();
        server.start();
    }

    public void putServerInfo(String message){
        ta.appendText(message+"\n");
    }

    public class InvalidExitCheck extends Thread{ //Thread that keeps searching for whether the Server GUI window is closed
        public void run(){
            while(server.isAlive()){ //repeat till server is still running(infinite)
                if (!mainStage.isShowing()){ //if the window is closed...
                    System.out.println("Exit");
                    System.exit(0); //Close the entire system
                    break;
                }
            }
        }
    }
}
