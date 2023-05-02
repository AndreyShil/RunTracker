package my.training.core.core_api.di

interface ProvidersFacade : MediatorProvider,
    HomeMediator,
    UserRepositoryProvider,
    ContextProvider,
    PreferencesProvider,
    DatabaseProvider