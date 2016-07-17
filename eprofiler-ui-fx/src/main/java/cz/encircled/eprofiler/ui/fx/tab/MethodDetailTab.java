package cz.encircled.eprofiler.ui.fx.tab;

import cz.encircled.eprofiler.ui.fx.FxApplication;
import cz.encircled.eprofiler.ui.fx.LogEntry;
import cz.encircled.eprofiler.ui.fx.model.AggregationType;
import cz.encircled.eprofiler.ui.fx.model.ChartAxisType;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author Vlad on 17-Jul-16.
 */
public class MethodDetailTab extends AbstractProfilerTab {

    private final BorderPane root;

    private final FlowPane methodInfoPane;

    ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:mm:ss");
        }
    };

    public MethodDetailTab(FxApplication fxApplication) {
        super(ProfilerTab.METHOD_DETAIL, fxApplication);
        setClosable(false);

        root = new BorderPane();

        BorderPane topPane = new BorderPane();
        methodInfoPane = new FlowPane(20D, 20D);
        topPane.setTop(methodInfoPane);

        root.setTop(topPane);
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
            methodInfoPane.getChildren().setAll(new Label("Method " + entry.methodName));

            root.setCenter(getLineChart(fxApplication.logEntryService.getAllCallsOfMethod(fxApplication.filePath.get(), entry.id), ChartAxisType.START_TIME, AggregationType.AVG, true));
            root.setBottom(getLineChart(fxApplication.logEntryService.getAllCallsOfMethod(fxApplication.filePath.get(), entry.id), ChartAxisType.THREAD_COUNT, AggregationType.AVG, true));
            root.setVisible(true);
        } else {
            root.setVisible(false);
        }
    }

    private LineChart<Number, Number> getLineChart(List<LogEntry> entries, ChartAxisType chartAxisType, AggregationType aggregationType, boolean reverseAxises) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

//        xAxis.setTickUnit(1000);
//        xAxis.setLowerBound(System.currentTimeMillis());
//        xAxis.setUpperBound(System.currentTimeMillis() * 2);

        xAxis.setLabel("Time");
        yAxis.setLabel("Value");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Chart of " + chartAxisType.name() + " and " + aggregationType.name());
        //defining a threadSeries
        XYChart.Series<NumberAxis, NumberAxis> threadSeries = new XYChart.Series<>();
        threadSeries.setName("Thread count");

        XYChart.Series<Number, Number> timeSeries = new XYChart.Series<>();
        timeSeries.setName("Time");

        Function<LogEntry, Long> keyFun = null;
        switch (chartAxisType) {
            case START_TIME:
                keyFun = this::getStartDate;
                break;
            case THREAD_COUNT:
                keyFun = this::getThreadCount;
                break;
        }

        switch (aggregationType) {
            case AVG:
                Map<Long, Double> avgAggregated = entries.stream().collect(Collectors.groupingBy(keyFun, Collectors.averagingLong(LogEntry::getTotalTime)));

                xAxis.setAutoRanging(false);
                xAxis.setLowerBound(avgAggregated.keySet().iterator().next());
                xAxis.setUpperBound(avgAggregated.keySet().iterator().next() + 10000L);
                xAxis.setTickUnit(1000);
                xAxis.setTickLabelFormatter(new StringConverter<Number>() {
                    @Override
                    public String toString(Number object) {
                        return timeFormat.get().format(new Date(object.longValue()));
                    }

                    @Override
                    public Number fromString(String string) {
                        return null;
                    }
                });

                timeSeries.getData().setAll(avgAggregated.entrySet().stream()
                        .map(doubleEntry -> {
                            return reverseAxises ?
                                    new XYChart.Data<Number, Number>(doubleEntry.getKey(), doubleEntry.getValue()) : new XYChart.Data<Number, Number>(doubleEntry.getValue(), doubleEntry.getKey());
                        }).collect(Collectors.toList()));
                break;
            default:
                Collector<LogEntry, ?, Optional<LogEntry>> collector = aggregationType == AggregationType.MIN ?
                        Collectors.minBy(Comparator.comparingLong(LogEntry::getTotalTime)) : Collectors.maxBy(Comparator.comparingLong(LogEntry::getTotalTime));
//                Map<Number, Optional<LogEntry>> aggregated = entries.stream().collect(Collectors.groupingBy(keyFun, collector));
        }

        // TODO


        lineChart.getData().add(timeSeries);


        return lineChart;
    }


    long getStartDate(LogEntry logEntry) {
        return logEntry.start;
    }

    long getThreadCount(LogEntry logEntry) {
        return logEntry.threadCount;
    }

}
