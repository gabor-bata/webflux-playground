package com.training.weather.strategy;

import com.training.weather.domain.WeatherForecast;
import reactor.core.publisher.MonoSink;

/**
 * Interface for emitting weather forecast.
 *
 * @author Gabor_Bata
 */
public interface WeatherForecastEmitterStrategy {

    /**
     * Emits weather forecast for the given location.
     *
     * @param location the location
     * @param emitter  the emitter
     */
    void emitWeatherForecast(String location, MonoSink<WeatherForecast> emitter);
}
