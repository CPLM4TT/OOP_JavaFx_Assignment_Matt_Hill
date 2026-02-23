module org.example.oop_javafx_assignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.oop_javafx_assignment to javafx.fxml;
    exports org.example.oop_javafx_assignment;
}