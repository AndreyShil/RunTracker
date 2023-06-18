package my.training.core.core_api.di

interface AggregatingProvider : MediatorsMapProvider,
    HomeMediator,
    UserRepositoryProvider,
    ContextProvider,
    PreferencesProvider,
    DatabaseProvider,
    FirebaseStorageProvider,
    NetworkProvider