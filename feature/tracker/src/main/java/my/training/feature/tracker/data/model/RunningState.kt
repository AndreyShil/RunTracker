package my.training.feature.tracker.data.model

internal enum class RunningState {
    INITIAL,        // Service not started: play button is displayed
    IN_PROGRESS,    // Service in progress: pause and stop buttons are displayed
    ON_PAUSE,       // Service on pause: play (resume) and stop buttons are displayed
    FINISH;         // Service on finish: finish button is displayed
}