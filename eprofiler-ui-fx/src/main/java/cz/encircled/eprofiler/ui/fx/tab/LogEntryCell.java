package cz.encircled.eprofiler.ui.fx.tab;

import cz.encircled.eprofiler.ui.fx.LogEntry;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.FlowPane;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * @author Vlad on 26-Jun-16.
 */
public class LogEntryCell extends TextFieldTreeCell<LogEntry> {

    public LogEntryCell() {
    }

    @Override
    public void updateItem(LogEntry logEntry, boolean empty) {
        super.updateItem(logEntry, empty);

        if (logEntry != null) {
            getStyleClass().remove("empty");
            String timeFormat = logEntry.totalTime > 999L ? "s 'sec' S 'ms'" : "S 'ms'";
            String time = DurationFormatUtils.formatDuration(logEntry.totalTime, timeFormat);

            if (logEntry.parent == null) {
                getStyleClass().add("root");
            } else {
                getStyleClass().remove("root");
            }

            setText("");
            FlowPane pane = new FlowPane();

            // TODO separate arguments (non-bold)
            Label methodLabel = new Label(logEntry.className + "." + logEntry.methodName);
            methodLabel.getStyleClass().add("method-label");

            Label timeLabel = new Label(time);

            Label repeats = new Label();
            if (logEntry.repeats > 1) {
                repeats.setText("(" + logEntry.repeats + " calls)");
                repeats.getStyleClass().add("repeats-label");
            }

            pane.getChildren().setAll(new Label(logEntry.packageName), methodLabel, timeLabel, repeats);
            setGraphic(pane);
        } else {
            getStyleClass().remove("root");
            getStyleClass().addAll("empty");
        }
    }
}