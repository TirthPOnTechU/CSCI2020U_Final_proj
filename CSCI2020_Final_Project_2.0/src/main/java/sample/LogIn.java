package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class LogIn extends Application {
    //Store main args
    private static String[] mainArgs;

    Stage window;
    Scene loginScreen;
    Scene scene2;
    Scene signupScreen;

    //List to keep track of who is currently logged in
    List<String> currentlyLoggedIn = new ArrayList<String>();

    //hard codes path gotta change
    String fileName = "C:\\Users\\patel\\Documents\\Year2_Docs\\csci2020u\\CSCI2020_Final_Project_2.0\\src\\main\\java\\saved\\userDatabase.txt";
    Scanner scan = new Scanner(new File(fileName));
    HashMap<String, String> loginInfo = new HashMap<String, String>();
    Alert error = new Alert(Alert.AlertType.ERROR);
    Alert message = new Alert(Alert.AlertType.INFORMATION);

    //Creating all of the text fields
    TextField username = new TextField();
    PasswordField password = new PasswordField();

    //Sign-up Screen text fields
    TextField usernameSU = new TextField();
    PasswordField passwordSU = new PasswordField();
    PasswordField passwordSU2 = new PasswordField();

    //What is this contructor?
    public LogIn() throws FileNotFoundException {
    }

    public class loginHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent actionEvent) {
            try {
                checkLogin();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public class signupHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent actionEvent) {
            window.setScene(signupScreen);
        }
    }

    public class registerHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent actionEvent) {
            try {
                checkRegister();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Function to check if the User is a registered User of the chat server
    public void checkLogin() throws IOException, InterruptedException {
        //Checks if the password entered matches their profile password for their given username and checks if the user is already logged into the server
        //Also removes any spaces entered in their username section
        if (password.getText().equals(loginInfo.get(username.getText().replaceAll("\\s", ""))) && !currentlyLoggedIn.contains(username.getText())) {
            //Adds the username to a list to keep track of who is logged in to the server
            currentlyLoggedIn.add(username.getText());

            //user has sucessfully log in
            //launch a new client GUI
            new client1GUI(username.getText(),this).start(new Stage());

        } else {
            error.setContentText("Incorrect Username and Password combination. Try Again.");
            error.show();
        }
    }

    //Function to check if the User is successfully registering for a new account on the chat server
    public void checkRegister() throws IOException {
        if (passwordSU.getText().equals(passwordSU2.getText()) && !loginInfo.containsKey(usernameSU.getText())) {
            message.setContentText("You have successfully registered");
            message.show();
            addUser();
            window.setScene(loginScreen);

        } else {
            error.setContentText("Please try again.");
            error.show();
        }
    }

    //Function to add a new user to the text file where all the users are saved
    public void addUser() throws IOException {
        FileWriter W = new FileWriter(new File(fileName), true);
        W.write("\n");
        W.write(usernameSU.getText() + " " + passwordSU.getText());
        W.close();

        //Adds the new username and password combination to the map
        loginInfo.put(usernameSU.getText(), passwordSU.getText());
    }

    //Function to read from the file and update the map
    public void updateLoginInfo() {
        //While loop that adds the username and the password to a map
        while (scan.hasNextLine()) {
            String currentLine = scan.nextLine();
            String[] line = currentLine.split(" ");
            String key = line[0];
            String v = line[1];
            loginInfo.put(key, v);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        updateLoginInfo();

        window = primaryStage;
        primaryStage.setScene(loginScreen);
        primaryStage.setTitle("Log In");

        //Setting the background image of the login screen
        FileInputStream input = new FileInputStream("C:\\Users\\patel\\Documents\\Year2_Docs\\csci2020u\\CSCI2020_Final_Project_2.0\\src\\main\\java\\saved\\whisper.png");
        Image image = new Image(input);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);

        GridPane nextScreen = new GridPane();
        nextScreen.setMinSize(300, 300);
        scene2 = new Scene(nextScreen);

        //--- LABELS ---//
        //Login Screen labels
        Text username_label = new Text("Username");
        Text password_label = new Text("Password");

        //Sign-up screen labels
        Text usernameSU_label = new Text("Username:");
        Text passwordSU_label = new Text("Password:");
        Text passwordSU2_label = new Text("Re-type Password:");

        //--- BUTTONS ---//
        //Login screen buttons
        Button login = new Button("Login");
        Button signup = new Button("Sign Up");

        //Sign-up screen buttons
        Button register = new Button("Register");

        //--- SETTING UP PANES ---//
        // Login pane
        GridPane loginPane = new GridPane();
        //Setting the window pane to same size as image width and height
        loginPane.setMinSize(image.getWidth(), image.getHeight());
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setPadding(new Insets(10, 10, 10, 10));
        loginPane.setHgap(10);
        loginPane.setVgap(10);
        loginScreen = new Scene(loginPane);
        loginScreen.setOnKeyPressed(e -> {
            try {
                checkLogin();
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        loginPane.setBackground(background);

        //Adding the Labels and the text fields to the appropriate spots
        loginPane.add(username_label, 0, 3);
        loginPane.add(username, 1, 3);
        loginPane.add(password_label, 0, 4);
        loginPane.add(password, 1, 4);

        //Adding the buttons and their functionality
        loginPane.add(login, 0, 5);
        loginHandler loginHandler = new loginHandler();
        login.setOnAction(loginHandler);

        loginPane.add(signup, 1, 5);
        signupHandler signupHandler = new signupHandler();
        signup.setOnAction(signupHandler);

        //Signup Pane
        GridPane signupPane = new GridPane();
        signupPane.setAlignment(Pos.CENTER);
        signupPane.setMinSize(300, 150);
        signupScreen = new Scene(signupPane);
        signupScreen.setOnKeyPressed(e -> {
            try {
                checkRegister();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        //Adding the Labels and the text fields to the appropriate spots
        signupPane.add(usernameSU_label, 0, 0);
        signupPane.add(usernameSU, 1, 0);
        signupPane.add(passwordSU_label, 0, 1);
        signupPane.add(passwordSU, 1, 1);
        signupPane.add(passwordSU2_label, 0, 2);
        signupPane.add(passwordSU2, 1, 2);

        //Adding the button and its functionality
        signupPane.add(register, 1, 4);
        registerHandler registerHandler = new registerHandler();
        register.setOnAction(registerHandler);

        //TESTING PURPOSES
        //To see how the map prints
        /*for (String key : loginInfo.keySet()) {
            System.out.println(key + ":" + loginInfo.get(key));
        }*/

        primaryStage.setScene(loginScreen);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }

}