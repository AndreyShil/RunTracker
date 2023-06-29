package my.training.feature.tracker.data.model

import my.training.core.core_api.domain.model.race.Race

 sealed interface RaceModel {

    fun getItemId(): String
    fun getItemViewType(): Int

    companion object {
        const val DATE_HEADER_VIEW_TYPE = 0
        const val RACE_VIEW_TYPE = 1
    }

    data class DateHeader(
        val date: String
    ) : RaceModel {
        override fun getItemId(): String = date
        override fun getItemViewType(): Int = DATE_HEADER_VIEW_TYPE
    }

    data class RaceInfo(
        val race: Race,
        val isExpanded: Boolean = false
    ) : RaceModel {
        override fun getItemId(): String = race.id
        override fun getItemViewType(): Int = RACE_VIEW_TYPE
    }
}