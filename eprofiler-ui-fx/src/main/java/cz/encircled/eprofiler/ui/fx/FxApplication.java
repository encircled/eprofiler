package cz.encircled.eprofiler.ui.fx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.TailerDirection;

/**
 * @author Kisel on 26.05.2016.
 */
public class FxApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Collection<LogEntry> logEntries = parseLog();

        primaryStage.setTitle("Tree View Sample");

        TreeItem<String> rootItem = new TreeItem<>("Inbox");
        rootItem.setExpanded(true);
        for (LogEntry logEntry : logEntries) {
            TreeItem<String> item = new TreeItem<>(logEntry.name + " - " + logEntry.time);
            rootItem.getChildren().add(item);
        }
        TreeView<String> tree = new TreeView<>(rootItem);
        StackPane root = new StackPane();
        root.getChildren().add(tree);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    private Collection<LogEntry> parseLog() {
        List<LogEntry> roots = new ArrayList<>(64);
        Map<Long, LogEntry> index = new HashMap<>(256);

        String basePath = "D:/temp/";
        ChronicleQueue queue = ChronicleQueueBuilder.single(basePath).build();
        ExcerptTailer tailer = queue.createTailer();
        tailer.direction(TailerDirection.BACKWARD);
        tailer.toEnd();

        String s;
        while ((s = tailer.readText()) != null) {
            if (!s.endsWith("in 0") && !s.endsWith("in 1")) {
                String[] split = s.split(":");
                LogEntry entry = new LogEntry();
                entry.id = Long.parseLong(split[0]);
                entry.name = split[2];
                entry.time = Long.parseLong(split[3]);
                if (split[1].isEmpty()) {
                    roots.add(entry);
                } else {
                    index.get(Long.parseLong(split[1])).children.add(entry);
                }
                index.put(entry.id, entry);
                System.out.println(s);
            }
        }
        return roots;
    }

}