package my.training.feature.auth.domain

import my.training.core.core_api.domain.preferences.Preferences
import javax.inject.Inject

internal class SaveAccessTokenUseCase @Inject constructor(
    private val preferences: Preferences
) {

    operator fun invoke(accessToken: String) {
        preferences.saveAccessToken(accessToken)
    }

}