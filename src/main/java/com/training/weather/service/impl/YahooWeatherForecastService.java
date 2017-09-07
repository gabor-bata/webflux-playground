package com.training.weather.service.impl;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import com.training.weather.converter.YahooChannelToWeatherForecastConverter;
import com.training.weather.domain.WeatherForecast;
import com.training.weather.service.WeatherForecastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;
import reactor.core.scheduler.Schedulers;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Yahoo Wather specific implementation of {@link WeatherForecastService}.
 *
 * @author Gabor_Bata
 */
@Service
public class YahooWeatherForecastService implements WeatherForecastService {
    private static final Logger LOG = LoggerFactory.getLogger(YahooWeatherForecastService.class);

    @Autowired
    private YahooChannelToWeatherForecastConverter yahooChannelToWeatherForecastConverter;

    @Override
    public Mono<WeatherForecast> getForecast(String location) {
        return Mono
                .<WeatherForecast>create(callback -> consumeWeatherForecast(location, callback))
                .doOnSubscribe(consumer -> LOG.debug("Subscibed to [{}] forecast stream", location))
                .doOnError(error -> LOG.warn("Error occurred during processing [{}] forecast: {}", location, error.getMessage()))
                .doOnSuccess(consumer -> LOG.debug("Received forecast data for [{}]", location))
                .onErrorResume(error -> Mono.empty());
    }

    @Override
    public Flux<WeatherForecast> getForecasts(String[] locations) {
        List<Mono<WeatherForecast>> forecasts = Arrays.stream(locations)
                .map(this::getForecast)
                .map(emitter -> emitter.subscribeOn(Schedulers.parallel()))
                .collect(Collectors.toList());
        return Flux
                .merge(forecasts)
                .sort(Comparator.comparing(WeatherForecast::getLocation));
    }

    private void consumeWeatherForecast(String location, MonoSink<WeatherForecast> callback) {
        try {
            LOG.debug("Getting forecast data for [{}]", location);
            YahooWeatherService yahooWeatherService = new YahooWeatherService();
            List<Channel> channels = yahooWeatherService.getForecastForLocation(location, DegreeUnit.CELSIUS).first(1);
            callback.success(channels.stream()
                    .map(yahooChannelToWeatherForecastConverter::convert)
                    .findFirst()
                    .orElse(null));
        } catch (JAXBException | IOException | NullPointerException e) {
            callback.error(e);
        }
    }
}
