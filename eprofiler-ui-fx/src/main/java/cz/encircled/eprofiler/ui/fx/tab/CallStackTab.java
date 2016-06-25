package cz.encircled.eprofiler.ui.fx.tab;

import cz.encircled.eprofiler.ui.fx.FxApplication;
import cz.encircled.eprofiler.ui.fx.LogEntry;
import cz.encircled.eprofiler.ui.fx.components.NumberTextField;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vlad on 25-Jun-16.
 */
public class CallStackTab extends Tab {

    private final TreeView<String> treeView;
    private List<LogEntry> logEntries = null;
    private FxApplication fxApplication;
    private ObjectProperty<MethodOrder> methodOrder = new SimpleObjectProperty<>();

    private StringProperty minElapsedTime = new SimpleStringProperty();

    public CallStackTab(FxApplication fxApplication) {
        super("Call stack");
        setClosable(false);
        this.fxApplication = fxApplication;

        BorderPane root = new BorderPane();

        ToolBar toolBar = getToolBar();
        root.setTop(toolBar);

        treeView = new TreeView<>();

        root.setCenter(treeView);

        setListeners();

        setContent(root);
    }

    private void setListeners() {
        minElapsedTime.addListener(observable -> repaint());
        methodOrder.addListener(observable -> repaint());
    }

    @NotNull
    private ToolBar getToolBar() {
        ToolBar toolBar = new ToolBar();

        ComboBox<MethodOrder> orderCombo = new ComboBox<>();
        orderCombo.getItems().setAll(MethodOrder.values());
        orderCombo.valueProperty().bindBidirectional(methodOrder);

        TextField minTimeInput = new NumberTextField();
        minTimeInput.textProperty().bindBidirectional(minElapsedTime);

        Button reload = new Button("Reload");
        reload.setOnAction(event -> {
            logEntries = null;
            repaint();
        });

        toolBar.getItems().addAll(
                new Label("order:"), orderCombo, new Separator(Orientation.VERTICAL),
                new Label("min elapsed time:"), minTimeInput, new Separator(Orientation.VERTICAL),
                reload
        );


        return toolBar;
    }

    public void repaint() {
        new Thread(() -> {
            if (logEntries == null) {
                logEntries = fxApplication.logParser.parse(fxApplication.filePath.get());
            }
            List<LogEntry> filtered = logEntries.stream().filter(logEntry -> {
                if (StringUtils.isNoneBlank(minElapsedTime.get())) {
                    long minTime = Long.parseLong(minElapsedTime.get());
                    if (minTime > logEntry.totalTime) {
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());

            if (methodOrder.get() != null) {
                switch (methodOrder.get()) {
                    case START_TIME:
                        Collections.sort(filtered, (o1, o2) -> Long.compare(o2.start, o1.start));
                        break;
                    case ELAPSED_TIME:
                        Collections.sort(filtered, (o1, o2) -> Long.compare(o2.totalTime, o1.totalTime));
                        break;
                }
            }

            Platform.runLater(() -> print(filtered));
        }).start();
    }

    private void print(List<LogEntry> filtered) {
        TreeItem<String> rootItem = new TreeItem<>("Call stack");
        rootItem.setExpanded(true);

        for (LogEntry logEntry : filtered) {
            addSubTree(rootItem, logEntry);
        }
        treeView.setRoot(rootItem);
    }


    private void addSubTree(TreeItem<String> parent, LogEntry logEntry) {
        String timeFormat = logEntry.totalTime > 999L ? "s 'sec'" : "S 'ms'";
        String time = DurationFormatUtils.formatDuration(logEntry.totalTime, timeFormat);
        String text = logEntry.methodName + "()#" + logEntry.className + " - " + time;
        if (logEntry.repeats > 1) {
            text += ". Repeats: " + logEntry.repeats;
        }
        TreeItem<String> item = new TreeItem<>(text);

        parent.getChildren().add(item);
        for (LogEntry child : logEntry.children) {
            addSubTree(item, child);
        }
    }

}