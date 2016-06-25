package cz.encircled.eprofiler.ui.fx;

import cz.encircled.eprofiler.ui.fx.parser.ChronicleLogParser;
import cz.encircled.eprofiler.ui.fx.parser.LogParser;
import cz.encircled.eprofiler.ui.fx.tab.CallStackTab;
import javafx.application.Application;
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
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * @author Kisel on 26.05.2016.
 */
public class FxApplication extends Application {

    public LogParser logParser = new ChronicleLogParser();
    public StringProperty filePath = new SimpleStringProperty();
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

        TabPane tabs = new TabPane();
        root.setCenter(tabs);
        tabs.getTabs().add(new CallStackTab(this));

        filePath.addListener(((observable, oldValue, newValue) -> {
            if (StringUtils.isNotBlank(newValue)) {
                ((CallStackTab) tabs.getTabs().get(0)).repaint();
            }
        }));

        primaryStage.setScene(primaryScene);
        primaryStage.show();
        primaryStage.setMaximized(true);
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
