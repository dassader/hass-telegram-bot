package com.home.hasstelegrambot.service;

import com.home.hasstelegrambot.repository.entity.NumberStatisticEntity;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
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

    public Optional<String> generateNumberStatisticChart(Iterable<NumberStatisticEntity> statisticEntities, String title, String seriesName) {
        List<Date> xDataList = new ArrayList<>();
        List<Double> yDataList = new ArrayList<>();

        for (NumberStatisticEntity statistic : statisticEntities) {
            LocalDateTime localDateTime = statistic.getDate().toLocalDateTime();
            Timestamp timestamp = Timestamp.valueOf(localDateTime);

            xDataList.add(timestamp);
            yDataList.add(statistic.getValue());
        }

        XYChart chart = new XYChartBuilder().width(800).height(600).title(title).build();
        chart.addSeries(seriesName, xDataList, yDataList);

        try {
            BitmapEncoder.saveBitmapWithDPI(chart, CHART_PATH, BitmapEncoder.BitmapFormat.JPG, 300);
            return Optional.of(CHART_PATH + ".jpg");
        } catch (Exception e) {
            log.error("Error while create chart", e);
            return Optional.empty();
        }
    }
}
