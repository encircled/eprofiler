package cz.encircled.eprofiler.ui.fx.tab;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import cz.encircled.eprofiler.ui.fx.FxApplication;
import cz.encircled.eprofiler.ui.fx.LogEntry;
import cz.encircled.eprofiler.ui.fx.components.NumberTextField;
import javafx.application.Platform;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

/**
 * @author Vlad on 23-Jul-16.
 */
public class PackageTab extends AbstractProfilerTab {

    private final TableView<Map.Entry<String, Long>> table;

    private LongProperty packageDepth = new SimpleLongProperty(4L);

    public PackageTab(FxApplication fxApplication) {
        super(ProfilerTab.PACKAGE, fxApplication);

        BorderPane pane = new BorderPane();
        setContent(pane);

        table = new TableView<>();

        TableColumn<Map.Entry<String, Long>, String> column1 = new TableColumn<>("Package");
        column1.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));

        TableColumn<Map.Entry<String, Long>, Long> column2 = new TableColumn<>("Value");
        column2.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue().getValue()));

        table.getColumns().addAll(column1, column2);
        pane.setCenter(table);

        NumberTextField depthInput = new NumberTextField();
        depthInput.bindBidirectional(packageDepth);
        pane.setTop(depthInput);

        packageDepth.addListener((observable, oldValue, newValue) -> repaint());
    }

    @Override
    public void repaint() {
        if(packageDepth.getValue() == null || packageDepth.getValue() < 1) {
            return;
        }
        int intValue = packageDepth.getValue().intValue();

        new Thread(() -> {
            List<LogEntry> all = fxApplication.logEntryService.collectAllChildren(fxApplication.filePath.get());
            Function<LogEntry, String> transform = o -> {
                String[] split = o.packageName.split("\\.");
                if (split.length >= intValue) {
                    String[] strings = new String[intValue];
                    System.arraycopy(split, 0, strings, 0, intValue);
                    return String.join(".", strings);
                } else {
                    return o.packageName;
                }
            };

            final Map<String, Long> result = all.parallelStream().collect(Collectors.groupingBy(transform, Collectors.summingLong(LogEntry::getSelfTotalTime)));
            ObservableList<Map.Entry<String, Long>> entries = FXCollections
                    .observableArrayList(result.entrySet().stream().sorted((o1, o2) -> Long.compare(o2.getValue(), o1.getValue())).collect(
                            Collectors.toList()));
            Platform.runLater(() -> table.setItems(entries));
        }).start();
    }

}
