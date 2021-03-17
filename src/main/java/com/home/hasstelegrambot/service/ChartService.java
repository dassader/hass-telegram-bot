package com.home.hasstelegrambot.service;

import com.home.hasstelegrambot.repository.entity.NumberStatisticEntity;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class ChartService {
    public static final String CHART_PATH = "/tmp/chart";
    public static final String DEFAULT = "DEFAULT";
    public static final double DEFAULT_MAX_VALUE = 2000D;
    public static final double DEFAULT_MIN_VALUE = 200D;

    public Optional<String> generateNumberStatisticChart(Iterable<NumberStatisticEntity> statisticEntities, String title, String seriesName) {
        List<Date> xDataList = new ArrayList<>();
        List<Double> yDataList = new ArrayList<>();

        for (NumberStatisticEntity statistic : statisticEntities) {
            LocalDateTime localDateTime = statistic.getDate().toLocalDateTime();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);

            xDataList.add(timestamp);
            yDataList.add(statistic.getValue());
        }

        if (xDataList.isEmpty()) {
            return Optional.empty();
        }

        XYChart chart = new XYChartBuilder()
                .width(800)
                .height(600)
                .theme(Styler.ChartTheme.Matlab)
                .build();

        XYStyler styler = chart.getStyler();

        Double maxValue = yDataList.stream().max(Double::compareTo).get();

        if(maxValue > DEFAULT_MAX_VALUE) {
            styler.setYAxisMax(maxValue);
        } else {
            styler.setYAxisMax(DEFAULT_MAX_VALUE);
        }

        Double minValue = yDataList.stream().min(Double::compareTo).get();

        if(minValue < DEFAULT_MIN_VALUE) {
            styler.setYAxisMin(minValue);
        } else {
            styler.setYAxisMin(DEFAULT_MIN_VALUE);
        }

        if(seriesName == null || "".equals(seriesName)) {
            styler.setLegendVisible(false);
            seriesName = DEFAULT;
        }

        if(title == null || "".equals(title)) {
            styler.setChartTitleVisible(false);
            title = DEFAULT;
        }

        chart.setTitle(title);
        chart.addSeries(seriesName, xDataList, yDataList);

        try {
            BitmapEncoder.saveBitmapWithDPI(chart, CHART_PATH, BitmapEncoder.BitmapFormat.JPG, 300);
            return Optional.of(CHART_PATH + ".jpg");
        } catch (Exception e) {
            log.error("Error while create chart", e);
            return Optional.empty();
        }
    }

    public Optional<String> generateNumberStatisticChart(Iterable<NumberStatisticEntity> statisticEntities) {
        return generateNumberStatisticChart(statisticEntities, null, null);
    }
}
