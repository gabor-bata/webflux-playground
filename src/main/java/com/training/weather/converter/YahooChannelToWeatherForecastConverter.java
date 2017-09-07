package com.training.weather.converter;

import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.Location;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import com.training.weather.domain.WeatherForecast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

/**
 * Converter which converts Yahoo specific {@link Channel} data to {@link WeatherForecast}.
 *
 * @author Gabor_Bata
 */
@Component
public class YahooChannelToWeatherForecastConverter implements Converter<Channel, WeatherForecast> {

    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    @Override
    public WeatherForecast convert(Channel channel) {
        requireNonNull(channel);
        WeatherForecast.Builder builder = WeatherForecast.builder();
        builder.location(formatLocation(channel.getLocation()));
        channel.getItem().getForecasts().stream().skip(1).findFirst().ifPresent(forecast -> {
            DegreeUnit degreeUnit = channel.getUnits().getTemperature();
            builder.temperatureHigh(formatTemperature(forecast.getHigh(), degreeUnit));
            builder.temperatureLow(formatTemperature(forecast.getLow(), degreeUnit));
            builder.summary(forecast.getText());
            builder.date(formatDate(forecast.getDate()));
        });
        return builder.build();
    }

    private String formatTemperature(int temperature, DegreeUnit degreeUnit) {
        return format("%d %s", temperature, degreeUnit.name());
    }

    private String formatLocation(Location location) {
        return format("%s, %s", location.getCity(), location.getCountry());
    }

    private String formatDate(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return localDateTime.format(dateTimeFormatter);
    }
}
