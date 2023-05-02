package my.training.runtracker.presentation.main_activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import my.training.core.core_api.extensions.doOnFailure
import my.training.core.core_api.extensions.doOnSuccess
import my.training.core.core_api.extensions.getErrorMessage
import my.training.runtracker.domain.InsertUserToDatabaseUseCase
import my.training.runtracker.domain.LoadProfileUseCase

class MainActivityViewModel(
    private val loadProfile: LoadProfileUseCase,
    private val insertUserToDatabase: InsertUserToDatabaseUseCase
) : ViewModel() {

    private val _effect: Channel<MainActivityEffect> = Channel()
    val effect = _effect.receiveAsFlow()

    init {
        viewModelScope.launch {
            loadProfile()
                .doOnSuccess {
                    insertUserToDatabase(it)
                    _effect.send(MainActivityEffect.OpenMainGraph)
                }.doOnFailure {
                    _effect.send(MainActivityEffect.ShowError(it.getErrorMessage()))
                    _effect.send(MainActivityEffect.OpenAuthGraph)
                }
        }
    }

}