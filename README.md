# WebFlux Playground [![Build Status](https://travis-ci.org/gabor-bata/webflux-playground.svg)](https://travis-ci.org/gabor-bata/webflux-playground)

The aim of this project is to try out some of the updates from Spring Framework 5, including the WebFlux framework for reactive style programming.

The project implements a simple weather service using Spring Boot and [yahoo-weather-java-api](https://github.com/fedy2/yahoo-weather-java-api).

## Running the project
To build and run the project you need `Maven` or an IDE which supports that (preferably `IntelliJ`).

You can run the project from the command line via: `mvn spring-boot:run`

or you can also run the project from your IDE by running the `WeatherApplication` class.

## API
**Request**

| Method | URL                  | Description                                  |
| ------ | -------------------- | -------------------------------------------- |
| GET    | /weather/{locations} | Get weather forecast for the given locations |

**URL Parameters**

| Name      | Type | Data Type      | Description             |
| --------- | -----| -------------- | ----------------------- |
| locations | path | array (string) | Array of location names |

**Success Response**
* Code: 200
* Content:
  ```json
  {
    "$schema": "http://json-schema.org/draft-04/schema#",
    "type": "array",
    "items": {
      "type": "object",
      "properties": {
        "location": {
          "type": "string"
        },
        "temperatureHigh": {
          "type": "string"
        },
        "temperatureLow": {
          "type": "string"
        },
        "summary": {
          "type": "string"
        },
        "date": {
          "type": "string"
        }
      }
    }
  }
  ```

### Examples
**Request:**
`GET http://localhost:8080/weather/Szeged,Budapest`

**Response:**
```json
[
   {
      "location":"Budapest, Hungary",
      "temperatureHigh":"21 CELSIUS",
      "temperatureLow":"10 CELSIUS",
      "summary":"Mostly Cloudy",
      "date":"Friday, 8 September 2017"
   },
   {
      "location":"Szeged, Hungary",
      "temperatureHigh":"23 CELSIUS",
      "temperatureLow":"11 CELSIUS",
      "summary":"Mostly Cloudy",
      "date":"Friday, 8 September 2017"
   }
]
```
