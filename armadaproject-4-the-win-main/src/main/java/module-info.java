module petespike {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;
    requires java.desktop;
    requires javafx.base;

    opens armada.view to javafx.fxml;
    exports armada.view;
    exports armada.model;
    exports backtracker;
}
