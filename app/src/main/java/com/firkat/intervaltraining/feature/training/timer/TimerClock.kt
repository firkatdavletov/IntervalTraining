package com.firkat.intervaltraining.feature.training.timer

import android.os.SystemClock
import javax.inject.Inject

interface TimerClock {
    fun elapsedRealtimeMillis(): Long
}

class SystemTimerClock @Inject constructor() : TimerClock {
    override fun elapsedRealtimeMillis(): Long = SystemClock.elapsedRealtime()
}
