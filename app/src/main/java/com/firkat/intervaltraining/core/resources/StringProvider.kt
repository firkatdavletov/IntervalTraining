package com.firkat.intervaltraining.core.resources

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

interface StringProvider {
    fun getString(@StringRes resId: Int): String
}

@Singleton
class AndroidStringProvider @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : StringProvider {

    override fun getString(@StringRes resId: Int): String = context.getString(resId)
}
