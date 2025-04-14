package FinalProject;
import javafx.application.Application;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Register extends Application {
    private String ssn;

    public Register(String ssn) {
        this.ssn = ssn;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Register for a Class");

        VBox vbox = new VBox(10);
        HBox courseBox = new HBox(10);
        Label courseLabel = new Label("Course Title:");
        ComboBox<String> courseComboBox = new ComboBox<>();
        courseBox.getChildren().addAll(courseLabel, courseComboBox);

        try {
            populateComboBox(ssn, courseComboBox);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Button addButton = new Button("Add");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
					addClass(ssn, courseComboBox.getValue());
					primaryStage.close();
					launchNext2(ssn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
        
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
        	primaryStage.close();
        	launchNext1(ssn);
        });
        vbox.getChildren().addAll(courseBox, addButton,exitButton);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void populateComboBox(String ssn, ComboBox<String> comboBox) throws SQLException {
        DBUtility.populateComboBox(ssn, comboBox);
    }

    private void addClass(String ssn, String courseId) throws SQLException {
        DBUtility.addClass(ssn, courseId);
    }
    
    
    private void launchNext1(String ssn) {
        try {
            MyClasses myClasses = new MyClasses(ssn);
            Stage nextStage = new Stage();
            myClasses.start(nextStage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void launchNext2(String ssn) {
        try {
            Register Register = new Register(ssn);
            Stage nextStage = new Stage();
            Register.start(nextStage);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
