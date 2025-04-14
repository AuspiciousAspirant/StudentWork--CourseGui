package FinalProject;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;

public class MyClasses extends Application {
    private String ssn;
    private ListView<String> classListView;

    public MyClasses(String ssn) {
        this.ssn = ssn;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Manage Classes");

        BorderPane borderPane = new BorderPane();

        // Top part:
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("Actions");
        menuBar.getMenus().addAll(file);

        MenuItem add = new MenuItem("Add Class");
        add.setOnAction(event -> addClass(primaryStage));

        MenuItem remove = new MenuItem("Remove Class");
        remove.setOnAction(event -> removeClass());

        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> exitButton(primaryStage));

        file.getItems().addAll(add, remove, exit);

        VBox vbox1 = new VBox(10);
        Label welcomeLabel = new Label("Welcome " + retrieveStudentName());
        vbox1.getChildren().addAll(menuBar, welcomeLabel);
        borderPane.setTop(vbox1);
        BorderPane.setMargin(vbox1, new Insets(10, 10, 10, 10));

        // Middle part:
        VBox vbox = new VBox(10);
        Label listViewLabel = new Label("Format: CourseID - CourseTitle - Grade");
        vbox.getChildren().addAll(listViewLabel, createListView());
        borderPane.setCenter(vbox);
        BorderPane.setMargin(vbox, new Insets(10, 10, 10, 10));

        // Bottom part: Buttons for adding/removing classes and exiting
        Button addButton = new Button("Add Class");
        Button removeButton = new Button("Remove Class");
        Button exitButton = new Button("Exit");

        removeButton.setOnAction(e -> removeClass());
        addButton.setOnAction(e -> addClass(primaryStage));
        exitButton.setOnAction(e -> primaryStage.close());

        VBox buttonBox = new VBox(10);
        buttonBox.getChildren().addAll(addButton, removeButton, exitButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setSpacing(10);
        borderPane.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(10, 10, 10, 10));

        primaryStage.setScene(new Scene(borderPane, 400, 300));
        primaryStage.show();
    }

    private ListView<String> createListView() {
        classListView = new ListView<>();
        classListView.setPrefHeight(200);
        DBUtility.populateListView(ssn, classListView);
        return classListView;
    }

    private String retrieveStudentName() {
        return DBUtility.retrieveStudentName(ssn);
    }

    private void removeClass() {
        String selectedItem = classListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String[] parts = selectedItem.split(" - ");
            String courseId = parts[0];
            DBUtility.removeClass(ssn, courseId);
			// Refresh the ListView
			classListView.getItems().clear(); // Clear the existing items
			DBUtility.populateListView(ssn, classListView); // Populate with updated data
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please select a class to remove.");
        }
    }

    private void addClass(Stage primaryStage) {
        try {
            Register register = new Register(ssn);
            register.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "An error occurred while adding a class.");
        }
    }

    private void exitButton(Stage primaryStage) {
        primaryStage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
