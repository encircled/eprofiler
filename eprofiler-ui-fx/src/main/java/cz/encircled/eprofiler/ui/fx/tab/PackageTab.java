package cz.encircled.eprofiler.ui.fx.tab;

import cz.encircled.eprofiler.ui.fx.FxApplication;
import cz.encircled.eprofiler.ui.fx.LogEntry;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Vlad on 23-Jul-16.
 */
public class PackageTab extends AbstractProfilerTab {

    private final TableView table;

    public PackageTab(FxApplication fxApplication) {
        super(ProfilerTab.PACKAGE, fxApplication);

        BorderPane pane = new BorderPane();
        setContent(pane);

        table = new TableView();

        TableColumn<Map.Entry<String, Long>, String> column1 = new TableColumn<>("Package");
        column1.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));

        TableColumn<Map.Entry<String, Long>, String> column2 = new TableColumn<>("Value");
        column2.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue().toString()));

        table.getColumns().addAll(column1, column2);
        pane.setCenter(table);
    }

    @Override
    public void repaint() {
        new Thread(() -> {
            List<LogEntry> all = fxApplication.logEntryService.getAllEntries(fxApplication.filePath.get());
            Function<LogEntry, String> transform = o -> {
                String[] split = o.packageName.split("\\.");
                if (split.length > 2) {
                    return split[0] + "." + split[1] + "." + split[2];
                } else {
                    return o.packageName;
                }
            };

            final Map<String, Long> result = all.stream().collect(Collectors.groupingBy(transform, Collectors.summingLong(LogEntry::getTotalTime)));
            ObservableList<Map.Entry<String, Long>> entries = FXCollections.observableArrayList(result.entrySet());
            Platform.runLater(() -> {
                table.setItems(entries);
            });
        }).start();
    }

}
