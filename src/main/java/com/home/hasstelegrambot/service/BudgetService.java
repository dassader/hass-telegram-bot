package com.home.hasstelegrambot.service;

import com.google.common.collect.Iterables;
import com.home.hasstelegrambot.repository.ConfigRepository;
import com.home.hasstelegrambot.repository.NumberStatisticRepository;
import com.home.hasstelegrambot.repository.entity.NumberStatisticEntity;
import com.home.hasstelegrambot.repository.entity.config.BudgetConfigEntity;
import com.home.hasstelegrambot.service.dto.BudgetCalculateDto;
import com.home.hasstelegrambot.service.dto.BudgetHistoryDto;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.XYStyler;
import org.knowm.xchart.style.markers.Circle;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.None;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

import static com.home.hasstelegrambot.repository.entity.NumberStatisticType.DAY_BUDGET;

@Log4j2
@Service
@AllArgsConstructor
public class BudgetService {
    private static final String CHART_PATH = "/tmp/chart";
    public static final String CHART_FILE_PATH = CHART_PATH + ".jpg";
    public static final String DEFAULT = "DEFAULT";
    public static final String COMPARE = "COMPARE";


    private NumberStatisticRepository statisticRepository;
    private ConfigRepository configRepository;

    public Optional<BudgetHistoryDto> getBudgetHistory() {
        Page<NumberStatisticEntity> statisticEntities =
                statisticRepository.findAllByType(DAY_BUDGET, PageRequest.of(0, 45, Sort.by(Sort.Direction.ASC, "date")));

        if (statisticEntities.isEmpty()) {
            return Optional.empty();
        }

        List<NumberStatisticEntity> statisticList = statisticEntities.getContent();
        NumberStatisticEntity lastChange = Iterables.getLast(statisticList);
        String lastChangeDate = lastChange.getDate().format(DateTimeFormatter.ofPattern("dd.MM"));

        BudgetConfigEntity budgetConfig = configRepository.getBudgetConfig();

        generateBudgetChart(statisticEntities, budgetConfig.getBudgetLimit());

        return Optional.of(new BudgetHistoryDto(lastChangeDate, (int) lastChange.getValue()));
    }

    public BudgetCalculateDto updateBudget(int money) {
        BudgetConfigEntity budgetConfig = configRepository.getBudgetConfig();

        OffsetDateTime now = OffsetDateTime.now();

        int salaryDay = configRepository.getBudgetConfig().getSalaryDay();

        int dayTillSalary = getDaysCountTillSalary(now, budgetConfig.getSalaryDay());
        String targetDate;
        if(dayTillSalary > salaryDay) {
            targetDate = now.plusMonths(1).withDayOfMonth(salaryDay).format(DateTimeFormatter.ofPattern("dd.MM"));
        } else {
            targetDate = now.withDayOfMonth(salaryDay).format(DateTimeFormatter.ofPattern("dd.MM"));
        }
        String currentDate = now.format(DateTimeFormatter.ofPattern("dd.MM"));
        int dayBudget = money / dayTillSalary;

        int globalDeviation = money - (dayTillSalary * budgetConfig.getBudgetLimit());

        BudgetCalculateDto budgetCalculateDto = new BudgetCalculateDto()
                .setCurrentDate(currentDate)
                .setDayTillSalary(dayTillSalary)
                .setTargetDate(targetDate)
                .setDayBudget(dayBudget)
                .setGlobalDeviation(globalDeviation);

        statisticRepository.save(new NumberStatisticEntity(now, dayBudget, DAY_BUDGET));


        Page<NumberStatisticEntity> statisticEntities =
                statisticRepository.findAllByType(DAY_BUDGET, PageRequest.of(0, 45, Sort.by(Sort.Direction.ASC, "date")));

        generateBudgetChart(statisticEntities, budgetConfig.getBudgetLimit());

        return budgetCalculateDto;
    }

    protected int getDaysCountTillSalary(OffsetDateTime now, int salaryDay) {
        int count = 1;

        for (OffsetDateTime date = now; date.getDayOfMonth() != salaryDay; date = date.plusDays(1)) {
            count++;
        }

        return count;
    }

    private Optional<String> generateBudgetChart(Iterable<NumberStatisticEntity> statisticEntities, double compareLine) {
        BudgetConfigEntity budgetConfig = configRepository.getBudgetConfig();

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
                .width(budgetConfig.getChartWidth())
                .height(budgetConfig.getChartWidth())
                .theme(Styler.ChartTheme.Matlab)
                .build();

        XYStyler styler = chart.getStyler();

        double defaultMaxValue = budgetConfig.getChartDefaultMaxValue();

        Double maxValue = yDataList.stream().max(Double::compareTo).get();

        if(maxValue > defaultMaxValue) {
            styler.setYAxisMax(maxValue);
        } else {
            styler.setYAxisMax(defaultMaxValue);
        }

        double defaultMinValue = budgetConfig.getChartDefaultMinValue();

        Double minValue = yDataList.stream().min(Double::compareTo).get();

        if(minValue < defaultMinValue) {
            styler.setYAxisMin(minValue);
        } else {
            styler.setYAxisMin(defaultMinValue);
        }

        styler.setLegendVisible(false);
        styler.setChartTitleVisible(false);
        styler.setMarkerSize(6);

        chart.setTitle(DEFAULT);
        chart.addSeries(DEFAULT, xDataList, yDataList);

        List<Marker> markers = new ArrayList<>();
        markers.add(new Circle());

        List<Color> colors = new ArrayList<>();
        colors.add(new Color(25, 128, 25, 255));

        if(xDataList.size() > 1) {
            chart.addSeries(COMPARE, getDateBorderValues(xDataList), Arrays.asList(compareLine, compareLine));
            markers.add(new None());
            colors.add(new Color(255, 0, 0, 150));
        }

        styler.setSeriesMarkers(markers.toArray(new Marker[0]));
        styler.setSeriesColors(colors.toArray(new Color[0]));

        try {
            BitmapEncoder.saveBitmapWithDPI(chart, CHART_PATH, BitmapEncoder.BitmapFormat.JPG, 300);
            return Optional.of(CHART_PATH + ".jpg");
        } catch (Exception e) {
            log.error("Error while create chart", e);
            return Optional.empty();
        }
    }

    private List<Date> getDateBorderValues(List<Date> list) {
        if(list.size() == 2) {
            return list;
        }

        return Arrays.asList(list.get(0), list.get(list.size() - 1));
    }
}
