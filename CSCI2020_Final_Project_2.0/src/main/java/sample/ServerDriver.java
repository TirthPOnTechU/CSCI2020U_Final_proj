package sample;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerDriver extends Application {
    public LogIn login;
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new ServerFxApp(4001).start(new Stage()); //start the Server GUI
        new LogIn().start(new Stage());//start the Login menu for incoming clients
    }
}
