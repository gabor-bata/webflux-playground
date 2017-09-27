package com.training.weather.strategy.impl;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import com.training.weather.domain.WeatherForecast;
import com.training.weather.strategy.WeatherForecastEmitterStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.MonoSink;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

/**
 * Yahoo Weather specific implementation of {@link WeatherForecastEmitterStrategy}.
 *
 * @author Gabor_Bata
 */
@Component
public class YahooWeatherForecastEmitterStrategy implements WeatherForecastEmitterStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(YahooWeatherForecastEmitterStrategy.class);

    @Autowired
    private Converter<Channel, WeatherForecast> yahooChannelToWeatherForecastConverter;

    @Override
    public void emitWeatherForecast(String location, MonoSink<WeatherForecast> emitter) {
        try {
            LOG.debug("Getting forecast data for [{}]", location);
            YahooWeatherService yahooWeatherService = new YahooWeatherService();
            List<Channel> channels = yahooWeatherService.getForecastForLocation(location, DegreeUnit.CELSIUS).first(1);
            emitter.success(channels.stream()
                    .map(yahooChannelToWeatherForecastConverter::convert)
                    .findFirst()
                    .orElse(null));
        } catch (JAXBException | IOException | NullPointerException e) {
            emitter.error(e);
        }
    }
}
