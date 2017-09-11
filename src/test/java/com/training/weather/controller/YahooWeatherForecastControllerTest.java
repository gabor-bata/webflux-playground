package com.training.weather.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.training.weather.domain.WeatherForecast;
import com.training.weather.service.WeatherForecastService;
import com.training.weather.strategy.WeatherForecastEmitterStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.MonoSink;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.eq;

/**
 * Spring Boot test for {@link YahooWeatherForecastController}.
 *
 * @author Gabor_Bata
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class YahooWeatherForecastControllerTest {
    private static final String LOCATION_1 = "Alamo";
    private static final String LOCATION_2 = "Budapest";
    private static final String LOCATION_3 = "Cardiff";

    private static final WeatherForecast WEATHER_FORECAST_1 = WeatherForecast.builder()
            .location(LOCATION_1)
            .date("Tuesday, 12 September 2017")
            .summary("Sunny")
            .temperatureHigh("34 CELSIUS")
            .temperatureLow("19 CELSIUS")
            .build();

    private static final WeatherForecast WEATHER_FORECAST_2 = WeatherForecast.builder()
            .location(LOCATION_2)
            .date("Tuesday, 12 September 2017")
            .summary("Scattered Showers")
            .temperatureHigh("18 CELSIUS")
            .temperatureLow("12 CELSIUS")
            .build();

    private static final WeatherForecast WEATHER_FORECAST_3 = WeatherForecast.builder()
            .location(LOCATION_3)
            .date("Tuesday, 12 September 2017")
            .summary("Showers")
            .temperatureHigh("16 CELSIUS")
            .temperatureLow("10 CELSIUS")
            .build();

    private static final Map<String, WeatherForecast> LOCATION_TO_WEATHER_FORECAST = ImmutableMap.<String, WeatherForecast>builder()
            .put(LOCATION_1, WEATHER_FORECAST_1)
            .put(LOCATION_2, WEATHER_FORECAST_2)
            .put(LOCATION_3, WEATHER_FORECAST_3)
            .build();

    private static final String LOCATIONS_PATH_PARAMETER = LOCATION_TO_WEATHER_FORECAST.keySet().stream()
            .collect(Collectors.joining(","));

    private WebTestClient webTestClient;

    @Autowired
    private WeatherForecastService weatherForecastService;

    @MockBean
    private WeatherForecastEmitterStrategy yahooWeatherForecastService;

    @Before
    public void setup() {
        webTestClient = WebTestClient.bindToController(new YahooWeatherForecastController(weatherForecastService)).build();
        doAnswer(emitSuccess).when(yahooWeatherForecastService).emitWeatherForecast(anyString(), any());
    }

    @Test
    public void shouldResponseWithWeatherForecastsOrderedByLocation() {
        List<WeatherForecast> EXPECTED_FORECASTS = ImmutableList.of(WEATHER_FORECAST_1, WEATHER_FORECAST_2, WEATHER_FORECAST_3);
        webTestClient.get().uri("/weather/" + LOCATIONS_PATH_PARAMETER)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(WeatherForecast.class).isEqualTo(EXPECTED_FORECASTS);
    }

    @Test
    public void shouldResponseEmptyWeatherForecastInCaseOfError() {
        doAnswer(emitError).when(yahooWeatherForecastService).emitWeatherForecast(eq(LOCATION_2), any());

        List<WeatherForecast> EXPECTED_FORECASTS = ImmutableList.of(WEATHER_FORECAST_1, WEATHER_FORECAST_3);
        webTestClient.get().uri("/weather/" + LOCATIONS_PATH_PARAMETER)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(WeatherForecast.class).isEqualTo(EXPECTED_FORECASTS);
    }

    @SuppressWarnings("unchecked")
    private Answer<Void> emitSuccess = invocation -> {
        String location = (String) invocation.getArguments()[0];
        MonoSink<WeatherForecast> emitter = (MonoSink<WeatherForecast>) invocation.getArguments()[1];
        emitter.success(LOCATION_TO_WEATHER_FORECAST.get(location));
        return null;
    };

    @SuppressWarnings("unchecked")
    private Answer<Void> emitError = invocation -> {
        MonoSink<WeatherForecast> emitter = (MonoSink<WeatherForecast>) invocation.getArguments()[1];
        emitter.error(new IOException("Intentional exception for testing purposes."));
        return null;
    };
}
