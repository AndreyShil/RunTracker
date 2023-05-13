package my.training.runtracker.domain

import my.training.core.core_api.domain.preferences.Preferences
import javax.inject.Inject

internal class GetAccessTokenUseCase @Inject constructor(
    private val preferences: Preferences
) {

    operator fun invoke(): String? {
        return preferences.getAccessToken()
    }
}