package cz.encircled.eprofiler.ui.fx.tab;

import cz.encircled.eprofiler.ui.fx.FxApplication;
import cz.encircled.eprofiler.ui.fx.LogEntry;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Vlad on 17-Jul-16.
 */
public class MethodDetailTab extends AbstractProfilerTab {

    private final BorderPane root;

    public MethodDetailTab(FxApplication fxApplication) {
        super(ProfilerTab.METHOD_DETAIL, fxApplication);
        setClosable(false);

        root = new BorderPane();
        setContent(root);

        fxApplication.selectedMethod.addListener((observable, oldValue, newValue) -> {
            repaintInternal(newValue);
        });
    }

    @Override
    public void repaint() {
        repaintInternal(fxApplication.selectedMethod.get());
    }

    private void repaintInternal(LogEntry entry) {
        if (entry != null) {
            root.setCenter(getLineChart(fxApplication.logEntryService.getAllCallsOfMethod(fxApplication.filePath.get(), entry.id)));
        }
    }

    private LineChart<String, Number> getLineChart(List<LogEntry> entries) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Time");
        yAxis.setLabel("Value");

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Chart of " + entries.get(0).methodName);
        //defining a threadSeries
        XYChart.Series<String, Number> threadSeries = new XYChart.Series<>();
        threadSeries.setName("Thread count");

        XYChart.Series<String, Number> timeSeries = new XYChart.Series<>();
        timeSeries.setName("Time");

        // TODO
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        threadSeries.getData().setAll(entries.stream()
                .map(logEntry -> new XYChart.Data<String, Number>(format.format(new Date(logEntry.start)), logEntry.threadCount))
                .collect(Collectors.toList()));
        timeSeries.getData().setAll(entries.stream()
                .map(logEntry -> new XYChart.Data<String, Number>(format.format(new Date(logEntry.start)), logEntry.totalTime))
                .collect(Collectors.toList()));

        lineChart.getData().add(threadSeries);
        lineChart.getData().add(timeSeries);

        return lineChart;
    }

}
