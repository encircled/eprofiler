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

    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public void updateItem(LogEntry logEntry, boolean empty) {
        super.updateItem(logEntry, empty);

        if (logEntry != null) {
            getStyleClass().remove("empty");
            String time = getFormattedTime(logEntry.totalTime);
            String cpuTime = Long.toString(logEntry.consumedCpu);

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

            Label cpuLabel = new Label(" CPU:" + cpuTime);

            Label memoryLabel = new Label(" RAM: " + humanReadableByteCount(logEntry.consumedMemory, false));
            memoryLabel.getStyleClass().add("ram-label");

            Label repeats = new Label();
            if (logEntry.repeats > 1) {
                repeats.setText("(" + logEntry.repeats + " calls)");
                repeats.getStyleClass().add("repeats-label");
            }

            pane.getChildren().setAll(new Label(logEntry.packageName), methodLabel, timeLabel, cpuLabel, repeats, memoryLabel);
            setGraphic(pane);
        } else {
            getStyleClass().remove("root");
            getStyleClass().addAll("empty");
        }
    }

    private String getFormattedTime(long time) {
        String timeFormat = time > 999L ? "s 'sec' S 'ms'" : "S 'ms'";
        return DurationFormatUtils.formatDuration(time, timeFormat);
    }

}