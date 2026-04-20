package com.firkat.intervaltraining.testutil

import androidx.annotation.StringRes
import com.firkat.intervaltraining.R
import com.firkat.intervaltraining.core.resources.StringProvider

class FakeStringProvider : StringProvider {

    override fun getString(@StringRes resId: Int): String =
        when (resId) {
            R.string.error_load_workout_failed -> "Не удалось загрузить тренировку"
            R.string.error_workout_id_not_found -> "Workout id не найден"
            else -> error("Unknown string resource id: $resId")
        }
}
