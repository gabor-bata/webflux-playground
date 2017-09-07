package com.training.weather.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

/**
 * Configuration of date/time related dependencies.
 *
 * @author Gabor_Bata
 */
@Configuration
public class DateTimeConfiguration {

    @Bean
    public DateTimeFormatter getDateFormatter() {
        return new DateTimeFormatterBuilder()
                .appendText(DAY_OF_WEEK)
                .appendLiteral(", ")
                .appendValue(DAY_OF_MONTH)
                .appendLiteral(' ')
                .appendText(MONTH_OF_YEAR)
                .appendLiteral(' ')
                .appendValue(YEAR, 4)
                .toFormatter();
    }
}
