package com.firkat.intervaltraining.core.data.repository

import com.firkat.intervaltraining.R
import com.firkat.intervaltraining.core.di.IoDispatcher
import com.firkat.intervaltraining.core.model.IntervalSegment
import com.firkat.intervaltraining.core.model.Workout
import com.firkat.intervaltraining.core.resources.StringProvider
import com.firkat.intervaltraining.domain.repository.WorkoutRepository
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MockWorkoutRepository @Inject constructor(
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val stringProvider: StringProvider,
) : WorkoutRepository {

    override suspend fun getWorkoutById(id: String): Workout = withContext(ioDispatcher) {
        delay(MOCK_DELAY_MILLIS)
        Workout(
            id = id,
            title = stringProvider.getString(R.string.mock_workout_title),
            totalTime = 900,
            elapsedTime = 0,
            intervals =
                listOf(
                    IntervalSegment(
                        name = stringProvider.getString(R.string.mock_interval_medium_walk),
                        totalSeconds = 300,
                        elapsedSeconds = 0,
                    ),
                    IntervalSegment(
                        name = stringProvider.getString(R.string.mock_interval_intensive_walk),
                        totalSeconds = 300,
                        elapsedSeconds = 0,
                    ),
                    IntervalSegment(
                        name = stringProvider.getString(R.string.mock_interval_medium_walk),
                        totalSeconds = 120,
                        elapsedSeconds = 0,
                    ),
                    IntervalSegment(
                        name = stringProvider.getString(R.string.mock_interval_slow_run),
                        totalSeconds = 30,
                        elapsedSeconds = 0,
                    ),
                    IntervalSegment(
                        name = stringProvider.getString(R.string.mock_interval_medium_walk),
                        totalSeconds = 90,
                        elapsedSeconds = 0,
                    ),
                    IntervalSegment(
                        name = stringProvider.getString(R.string.mock_interval_slow_run),
                        totalSeconds = 30,
                        elapsedSeconds = 0,
                    ),
                    IntervalSegment(
                        name = stringProvider.getString(R.string.mock_interval_medium_walk),
                        totalSeconds = 30,
                        elapsedSeconds = 0,
                    ),
                ),
        )
    }

    private companion object {
        const val MOCK_DELAY_MILLIS = 1_000L
    }
}
