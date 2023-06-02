package my.training.feature.stats

import my.training.feature.stats.model.RaceData

internal object MockData {
    val statsData = listOf(
        RaceData(
            date = "2023-05-16T22:31:34.835Z",
            distance = 2863,
            burnedCalories = 145.2
        ),
        RaceData(
            date = "2023-05-18T22:31:34.835Z",
            distance = 3500,
            burnedCalories = 163.8
        ),
        RaceData(
            date = "2023-05-21T22:31:34.835Z",
            distance = 2350,
            burnedCalories = 115.0
        ),
        RaceData(
            date = "2023-05-22T22:31:34.835Z",
            distance = 2350,
            burnedCalories = 115.0
        ),
        RaceData(
            date = "2023-05-23T22:31:34.835Z",
            distance = 2350,
            burnedCalories = 115.0
        ),
        RaceData(
            date = "2023-05-24T22:31:34.835Z",
            distance = 1500,
            burnedCalories = 75.0
        ),
        RaceData(
            date = "2023-05-26T22:31:34.835Z",
            distance = 1483,
            burnedCalories = 73.4
        ),
        RaceData(
            date = "2023-05-28T22:31:34.835Z",
            distance = 2450,
            burnedCalories = 125.3
        ),
        RaceData(
            date = "2023-05-31T22:31:34.835Z",
            distance = 6000,
            burnedCalories = 125.3
        )
    )
}