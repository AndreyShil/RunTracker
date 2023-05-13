package my.training.feature.tracker.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import my.training.core.core_api.di.qualifiers.AppContext
import my.training.core.core_api.di.scopes.PerService

@Module
internal object LocationClientModule {

    @Provides
    @PerService
    fun provideFusedLocationProviderClient(
        @AppContext appContext: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(appContext)
    }
}