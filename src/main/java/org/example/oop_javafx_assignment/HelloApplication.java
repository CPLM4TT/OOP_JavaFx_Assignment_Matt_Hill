package org.example.oop_javafx_assignment;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    private final ObservableList<Student> students = FXCollections.observableArrayList();
    private int nextId = 1;

    @Override
    public void start(Stage stage) {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        Menu themeMenu = new Menu("Theme");
        Menu helpMenu = new Menu("Help");

        MenuItem clearTableItem = new MenuItem("Clear Table");
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().addAll(clearTableItem, new SeparatorMenuItem(), exitItem);

        MenuItem clearFormItem = new MenuItem("Clear Form");
        editMenu.getItems().add(clearFormItem);

        MenuItem defaultThemeItem = new MenuItem("Default Theme");
        themeMenu.getItems().add(defaultThemeItem);

        MenuItem aboutItem = new MenuItem("About");
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, editMenu, themeMenu, helpMenu);

        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(130);
        leftPanel.setPadding(new Insets(10));
        leftPanel.getStyleClass().add("left-panel");

        StackPane imageBox = new StackPane();
        imageBox.setPrefSize(110, 110);
        imageBox.getStyleClass().add("image-box");

        Label imageLabel = new Label("👤");
        imageLabel.getStyleClass().add("image-label");

        imageBox.getChildren().add(imageLabel);
        leftPanel.getChildren().add(imageBox);

        TableView<Student> tableView = new TableView<>();
        tableView.setItems(students);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student, String> idCol = new TableColumn<>("ID");
        TableColumn<Student, String> firstCol = new TableColumn<>("First Name");
        TableColumn<Student, String> lastCol = new TableColumn<>("Last Name");
        TableColumn<Student, String> deptCol = new TableColumn<>("Department");
        TableColumn<Student, String> majorCol = new TableColumn<>("Major");
        TableColumn<Student, String> emailCol = new TableColumn<>("Email");

        idCol.setCellValueFactory(data -> data.getValue().idProperty());
        firstCol.setCellValueFactory(data -> data.getValue().firstNameProperty());
        lastCol.setCellValueFactory(data -> data.getValue().lastNameProperty());
        deptCol.setCellValueFactory(data -> data.getValue().departmentProperty());
        majorCol.setCellValueFactory(data -> data.getValue().majorProperty());
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());

        tableView.getColumns().addAll(idCol, firstCol, lastCol, deptCol, majorCol, emailCol);
        tableView.setPlaceholder(new Label("No content in table"));

        VBox rightPanel = new VBox(8);
        rightPanel.setPrefWidth(220);
        rightPanel.setPadding(new Insets(8));
        rightPanel.getStyleClass().add("right-panel");

        TextField firstNameField = makeTextField("First Name");
        TextField lastNameField = makeTextField("Last Name");
        TextField departmentField = makeTextField("Department");
        TextField majorField = makeTextField("Major");
        TextField emailField = makeTextField("Email");
        TextField imageUrlField = makeTextField("imageURL");

        Button clearBtn = new Button("Clear");
        Button addBtn = new Button("Add");
        Button deleteBtn = new Button("Delete");
        Button editBtn = new Button("Edit");

        clearBtn.getStyleClass().add("action-button");
        addBtn.getStyleClass().add("action-button");
        deleteBtn.getStyleClass().add("action-button");
        editBtn.getStyleClass().add("action-button");

        clearBtn.setMaxWidth(Double.MAX_VALUE);
        addBtn.setMaxWidth(Double.MAX_VALUE);
        deleteBtn.setMaxWidth(Double.MAX_VALUE);
        editBtn.setMaxWidth(Double.MAX_VALUE);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        rightPanel.getChildren().addAll(
                firstNameField,
                lastNameField,
                departmentField,
                majorField,
                emailField,
                imageUrlField,
                spacer,
                clearBtn,
                addBtn,
                deleteBtn,
                editBtn
        );

        HBox centerRow = new HBox();
        HBox.setHgrow(tableView, Priority.ALWAYS);
        centerRow.getChildren().addAll(leftPanel, tableView, rightPanel);

        Region bottomStrip = new Region();
        bottomStrip.setPrefHeight(32);
        bottomStrip.getStyleClass().add("bottom-strip");

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");
        root.setTop(menuBar);
        root.setCenter(centerRow);
        root.setBottom(bottomStrip);

        clearBtn.setOnAction(e -> {
            clearFields(firstNameField, lastNameField, departmentField, majorField, emailField, imageUrlField);
            tableView.getSelectionModel().clearSelection();
        });

        addBtn.setOnAction(e -> {
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            String dept = departmentField.getText().trim();
            String major = majorField.getText().trim();
            String email = emailField.getText().trim();

            if (first.isEmpty() || last.isEmpty()) {
                showInfo("Enter at least First Name and Last Name.");
                return;
            }

            Student s = new Student(String.valueOf(nextId), first, last, dept, major, email);
            nextId++;
            students.add(s);

            clearFields(firstNameField, lastNameField, departmentField, majorField, emailField, imageUrlField);
        });

        deleteBtn.setOnAction(e -> {
            Student selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                students.remove(selected);
            } else {
                showInfo("Select a row to delete.");
            }
        });

        editBtn.setOnAction(e -> showInfo("Edit not implemented."));

        clearTableItem.setOnAction(e -> students.clear());
        exitItem.setOnAction(e -> stage.close());
        clearFormItem.setOnAction(e ->
                clearFields(firstNameField, lastNameField, departmentField, majorField, emailField, imageUrlField)
        );
        defaultThemeItem.setOnAction(e -> showInfo("Default theme is already applied."));
        aboutItem.setOnAction(e -> showInfo("CSC325 JavaFX UI Mockup"));

        Scene scene = new Scene(root, 900, 620);
        scene.getStylesheets().add(HelloApplication.class.getResource("style.css").toExternalForm());

        stage.setTitle("FSC CSC325_Full Stack Project");
        stage.setScene(scene);
        stage.show();
    }

    private TextField makeTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("form-field");
        return tf;
    }

    private void clearFields(TextField... fields) {
        for (TextField tf : fields) {
            tf.clear();
        }
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static class Student {
        private final SimpleStringProperty id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty department;
        private final SimpleStringProperty major;
        private final SimpleStringProperty email;

        public Student(String id, String firstName, String lastName, String department, String major, String email) {
            this.id = new SimpleStringProperty(id);
            this.firstName = new SimpleStringProperty(firstName);
            this.lastName = new SimpleStringProperty(lastName);
            this.department = new SimpleStringProperty(department);
            this.major = new SimpleStringProperty(major);
            this.email = new SimpleStringProperty(email);
        }

        public SimpleStringProperty idProperty() { return id; }
        public SimpleStringProperty firstNameProperty() { return firstName; }
        public SimpleStringProperty lastNameProperty() { return lastName; }
        public SimpleStringProperty departmentProperty() { return department; }
        public SimpleStringProperty majorProperty() { return major; }
        public SimpleStringProperty emailProperty() { return email; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}