package my.training.feature.stats

import my.training.feature.stats.model.RaceData

internal object MockData {
    val statsData = listOf(
        RaceData(
            date = "2023-05-16T22:31:34.835Z",
            distance = 2863,
            burnedCalories = 145.2,
            averageSpeed = 5.0
        ),
        RaceData(
            date = "2023-05-18T22:31:34.835Z",
            distance = 3500,
            burnedCalories = 163.8,
            averageSpeed = 6.2
        ),
        RaceData(
            date = "2023-05-21T22:31:34.835Z",
            distance = 2350,
            burnedCalories = 115.0,
            averageSpeed = 7.6
        ),
        RaceData(
            date = "2023-05-22T22:31:34.835Z",
            distance = 2350,
            burnedCalories = 115.0,
            averageSpeed = 10.0
        ),
        RaceData(
            date = "2023-05-23T22:31:34.835Z",
            distance = 2350,
            burnedCalories = 115.0,
            averageSpeed = 6.8
        ),
        RaceData(
            date = "2023-05-24T22:31:34.835Z",
            distance = 1500,
            burnedCalories = 75.0,
            averageSpeed = 5.5
        ),
        RaceData(
            date = "2023-05-26T22:31:34.835Z",
            distance = 1483,
            burnedCalories = 73.4,
            averageSpeed = 5.9
        ),
        RaceData(
            date = "2023-05-28T22:31:34.835Z",
            distance = 2450,
            burnedCalories = 125.3,
            averageSpeed = 9.6
        ),
        RaceData(
            date = "2023-05-31T22:31:34.835Z",
            distance = 6000,
            burnedCalories = 125.3,
            averageSpeed = 7.9
        ),
        RaceData(
            date = "2023-06-02T22:31:34.835Z",
            distance = 16520,
            burnedCalories = 260.3,
            averageSpeed = 8.9
        ),
        RaceData(
            date = "2023-06-05T22:31:34.835Z",
            distance = 6852,
            burnedCalories = 145.3,
            averageSpeed = 8.6
        ),
        RaceData(
            date = "2023-06-07T22:31:34.835Z",
            distance = 4895,
            burnedCalories = 99.3,
            averageSpeed = 9.9
        )
    )
}