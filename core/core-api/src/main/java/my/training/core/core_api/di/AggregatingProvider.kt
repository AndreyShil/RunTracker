package my.training.core.core_api.di

interface AggregatingProvider : MediatorsMapProvider,
    HomeMediator,
    UserRepositoryProvider,
//    RaceRepositoryProvider,
    ContextProvider,
    PreferencesProvider,
    DatabaseProvider,
    FirebaseStorageProvider,
    NetworkProvider