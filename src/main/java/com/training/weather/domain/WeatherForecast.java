package com.training.weather.domain;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Object representing a weather forecast data.
 *
 * @author Gabor_Bata
 */
public class WeatherForecast {
    private String location;
    private String temperatureHigh;
    private String temperatureLow;
    private String summary;
    private String date;

    private WeatherForecast() {
    }

    private WeatherForecast(Builder builder) {
        location = requireNonNull(builder.location);
        temperatureHigh = requireNonNull(builder.temperatureHigh);
        temperatureLow = requireNonNull(builder.temperatureLow);
        summary = requireNonNull(builder.summary);
        date = requireNonNull(builder.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeatherForecast forecast = (WeatherForecast) o;
        return Objects.equals(location, forecast.location) &&
                Objects.equals(temperatureHigh, forecast.temperatureHigh) &&
                Objects.equals(temperatureLow, forecast.temperatureLow) &&
                Objects.equals(summary, forecast.summary) &&
                Objects.equals(date, forecast.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, temperatureHigh, temperatureLow, summary, date);
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "location='" + location + '\'' +
                ", temperatureHigh='" + temperatureHigh + '\'' +
                ", temperatureLow='" + temperatureLow + '\'' +
                ", summary='" + summary + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getLocation() {
        return location;
    }

    public String getTemperatureHigh() {
        return temperatureHigh;
    }

    public String getTemperatureLow() {
        return temperatureLow;
    }

    public String getSummary() {
        return summary;
    }

    public String getDate() {
        return date;
    }

    /**
     * Creates a new builder instance for the {@link WeatherForecast} object.
     *
     * @return a new {@link Builder} instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * A builder for {@link WeatherForecast}.
     *
     * @author Gabor_Bata
     */
    public static class Builder {
        private String location;
        private String temperatureHigh;
        private String temperatureLow;
        private String summary;
        private String date;

        private Builder() {
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder temperatureHigh(String temperatureHigh) {
            this.temperatureHigh = temperatureHigh;
            return this;
        }

        public Builder temperatureLow(String temperatureLow) {
            this.temperatureLow = temperatureLow;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public WeatherForecast build() {
            return new WeatherForecast(this);
        }
    }
}
