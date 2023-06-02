package my.training.core.core_api.extensions

import android.os.Build
import android.os.Parcel

inline fun <reified T> Parcel.readParcelList(list: List<T>) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        readList(
            list.toMutableList(),
            T::class.java.classLoader,
            T::class.java
        )
    } else {
        @Suppress("Deprecation")
        readList(list.toMutableList(), T::class.java.classLoader)
    }
}