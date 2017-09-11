package com.training.weather.controller;

import com.training.weather.domain.WeatherForecast;
import com.training.weather.service.WeatherForecastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Controller for handling weather forecast requests.
 *
 * @author Gabor_Bata
 */
@RestController
public class YahooWeatherForecastController {
    private static final Logger LOG = LoggerFactory.getLogger(YahooWeatherForecastController.class);

    private WeatherForecastService weatherForecastService;

    public YahooWeatherForecastController(@Qualifier("yahooWeatherForecastService") WeatherForecastService weatherForecastService) {
        this.weatherForecastService = weatherForecastService;
    }

    /**
     * Get weather forecast for the given locations.
     *
     * @param locations the array of locations
     * @return a {@link Flux} of {@link WeatherForecast}
     */
    @GetMapping("/weather/{locations}")
    public Flux<WeatherForecast> getForecast(@PathVariable String[] locations) {
        LOG.debug("Request received for [{}]", locations);
        Flux<WeatherForecast> response = weatherForecastService.getForecasts(locations);
        LOG.debug("Return response for [{}]", locations);
        return response;
    }
}
