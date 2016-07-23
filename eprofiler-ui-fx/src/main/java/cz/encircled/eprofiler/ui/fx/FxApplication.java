package cz.encircled.eprofiler.ui.fx;

import java.io.File;

import cz.encircled.eprofiler.ui.fx.parser.ChronicleLogParser;
import cz.encircled.eprofiler.ui.fx.parser.LogEntryService;
import cz.encircled.eprofiler.ui.fx.parser.LogEntryServiceImpl;
import cz.encircled.eprofiler.ui.fx.tab.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * @author Kisel on 26.05.2016.
 */
public class FxApplication extends Application {

    public LogEntryService logEntryService = new LogEntryServiceImpl(new ChronicleLogParser());

    public StringProperty filePath = new SimpleStringProperty();

    public ObjectProperty<LogEntry> selectedMethod = new SimpleObjectProperty<>(null);
    // TODO
    public TabPane tabs;
    private Stage primaryStage;
    private Scene primaryScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("EProfiler reader");

        BorderPane root = new BorderPane();
        root.setTop(getMenu());

        primaryScene = new Scene(root);
        primaryScene.getStylesheets().add("/styles.css");

        tabs = new TabPane();
        root.setCenter(tabs);
        tabs.getTabs().add(new CallTreeTab(this));
        tabs.getTabs().add(new MethodDetailTab(this));
        tabs.getTabs().add(new PackageTab(this));

        tabs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ((AbstractProfilerTab) newValue).repaint();
        });

        primaryStage.setScene(primaryScene);
        primaryStage.show();
        primaryStage.setMaximized(true);

        // TODO
        filePath.set("C:/templogs");
    }

    public boolean isActiveTab(ProfilerTab tab) {
        return getActiveTab() == tab;
    }

    public ProfilerTab getActiveTab() {
        return ProfilerTab.getTabByIndex(tabs.getSelectionModel().getSelectedIndex());
    }

    public void switchTo(ProfilerTab tab) {
        tabs.getSelectionModel().select(tab.index);
    }

    private MenuBar getMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(getFileMenu());

        return menuBar;
    }

    private Menu getFileMenu() {
        Menu file = new Menu("File");

        MenuItem open = new MenuItem("Open");
        open.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        open.setOnAction(event -> this.openFile());

        MenuItem exit = new MenuItem("Exit");
        exit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        exit.setOnAction(event -> System.exit(0));

        file.getItems().addAll(open, exit);
        return file;
    }

    private void openFile() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Open Resource File");
        File selectedFile = dirChooser.showDialog(primaryStage);
        if (selectedFile != null) {
            filePath.set(selectedFile.getPath());
        }
    }

}
