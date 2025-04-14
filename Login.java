package FinalProject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Login extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws SQLException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Login");

        VBox vbox = new VBox(10);
        HBox ssnBox = new HBox(10);
        Label ssnLabel = new Label("SSN:");
        TextField ssnField = new TextField();
        ssnField.setPromptText("Enter SSN");
        ssnBox.getChildren().addAll(ssnLabel, ssnField);

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> System.exit(0));

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String ssn = ssnField.getText();
            if (DBUtility.checkStudentExists(ssn)) {
			    launchNext(ssn);
			} else {
			    primaryStage.setTitle("Login: Error, Student Not Found");
			}
        });

        vbox.getChildren().addAll(ssnBox, loginButton, exitButton);

        Scene scene = new Scene(vbox, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void launchNext(String ssn) {
        try {
            MyClasses myClasses = new MyClasses(ssn);
            Stage nextStage = new Stage();
            myClasses.start(nextStage);
            primaryStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
