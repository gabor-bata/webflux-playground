package com.training.weather.service.impl;

import com.training.weather.domain.WeatherForecast;
import com.training.weather.service.WeatherForecastService;
import com.training.weather.strategy.WeatherForecastEmitterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link WeatherForecastService} which can get weather forecast through the
 * injected {@link WeatherForecastEmitterStrategy}.
 *
 * @author Gabor_Bata
 */
public class DefaultWeatherForecastService implements WeatherForecastService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultWeatherForecastService.class);

    private WeatherForecastEmitterStrategy weatherForecastEmitterStrategy;

    public DefaultWeatherForecastService(WeatherForecastEmitterStrategy weatherForecastEmitterStrategy) {
        this.weatherForecastEmitterStrategy = weatherForecastEmitterStrategy;
    }

    @Override
    public Mono<WeatherForecast> getForecast(String location) {
        return Mono
                .<WeatherForecast>create(emitter -> weatherForecastEmitterStrategy.emitWeatherForecast(location, emitter))
                .doOnSubscribe(subscription -> LOG.debug("Subscibed to [{}] forecast stream", location))
                .doOnError(error -> LOG.warn("Error occurred during processing [{}] forecast: {}", location, error.getMessage()))
                .doOnSuccess(forecast -> LOG.debug("Received forecast data for [{}]", location))
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
}
