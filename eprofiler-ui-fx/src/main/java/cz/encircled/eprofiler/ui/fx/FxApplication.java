package cz.encircled.eprofiler.ui.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ChronicleQueueBuilder;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.TailerDirection;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Kisel on 26.05.2016.
 */
public class FxApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        boolean raw = false;
        primaryStage.setTitle("Log reader");
        TreeItem<String> rootItem = new TreeItem<>("Inbox");
        TreeView<String> tree = new TreeView<>(rootItem);
        rootItem.setExpanded(true);

        print(raw, rootItem);

        BorderPane root = new BorderPane();
        Button refresh = new Button("refresh");
        refresh.setOnAction(e -> print(false, rootItem));
        root.setBottom(refresh);
        root.setCenter(tree);
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    private void print(boolean raw, TreeItem<String> rootItem) {
        System.out.println("Print log");
        rootItem.getChildren().clear();
        if (raw) {
            String basePath = "D:/temp/";
            ChronicleQueue queue = ChronicleQueueBuilder.single(basePath).build();
            ExcerptTailer tailer = queue.createTailer();
            tailer.direction(TailerDirection.BACKWARD);
            tailer.toEnd();

            String s;
            while ((s = tailer.readText()) != null) {
                rootItem.getChildren().add(new TreeItem<>(s));
            }
            queue.close();
            queue.close();
        } else {
            Collection<LogEntry> logEntries = parseLog();
            for (LogEntry logEntry : logEntries) {
                addSubTree(rootItem, logEntry);
            }
        }
    }

    private void addSubTree(TreeItem<String> parent, LogEntry logEntry) {
        TreeItem<String> item = new TreeItem<>(logEntry.methodName + " - " + logEntry.totalTime);
        parent.getChildren().add(item);
        for (LogEntry child : logEntry.children) {
            addSubTree(item, child);
        }
    }

    private Collection<LogEntry> parseLog() {
        List<LogEntry> roots = new ArrayList<>(64);
        Map<Long, LogEntry> index = new HashMap<>(256);

        ExcerptTailer tailer = getExcerptTailer();

        String s;
        while ((s = tailer.readText()) != null) {
            System.out.println(s);
            String[] split = s.split(":");
            LogEntry entry = new LogEntry();
            entry.id = Long.parseLong(split[0]);
            entry.methodName = split[2];
            entry.className = split[3];
            entry.start = Long.parseLong(split[4]);
            entry.end = Long.parseLong(split[5]);
            entry.totalTime = entry.end - entry.start;

            if (split[1].isEmpty()) {
                roots.add(entry);
            } else {
                try {
                    index.get(Long.parseLong(split[1])).children.add(entry);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            index.put(entry.id, entry);
        }
        return roots;
    }

    @NotNull
    private ExcerptTailer getExcerptTailer() {
        String basePath = "D:/temp/";
        ChronicleQueue queue = ChronicleQueueBuilder.single(basePath).build();
        ExcerptTailer tailer = queue.createTailer();
        tailer.direction(TailerDirection.BACKWARD);
        tailer.toEnd();
        return tailer;
//        return null;
    }

}
