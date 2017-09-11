package com.training.weather.configuration;

import com.training.weather.service.WeatherForecastService;
import com.training.weather.service.impl.DefaultWeatherForecastService;
import com.training.weather.strategy.WeatherForecastEmitterStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Yahoo Weather Service related configuration.
 *
 * @author Gabor_Bata
 */
@Configuration
public class YahooWeatherServiceConfiguration {

    @Autowired
    private WeatherForecastEmitterStrategy yahooWeatherForecastEmitterStrategy;

    @Bean
    public WeatherForecastService yahooWeatherForecastService() {
        return new DefaultWeatherForecastService(yahooWeatherForecastEmitterStrategy);
    }
}
