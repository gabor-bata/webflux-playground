package com.training.weather.service;

import com.training.weather.domain.WeatherForecast;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Get weather forecast for tomorrow.
 *
 * @author Gabor_Bata
 */
public interface WeatherForecastService {

    /**
     * Get weather forecast for tomorrow for the given location.
     *
     * @param location the location
     * @return a {@link Mono} of {@link WeatherForecast}
     */
    Mono<WeatherForecast> getForecast(String location);

    /**
     * Get weather forecast for tomorrow for the given locations.
     *
     * @param locations the array of locations
     * @return a {@link Flux} of {@link WeatherForecast}
     */
    Flux<WeatherForecast> getForecasts(String[] locations);
}
