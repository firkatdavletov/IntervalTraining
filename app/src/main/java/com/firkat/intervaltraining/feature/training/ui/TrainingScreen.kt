package com.firkat.intervaltraining.feature.training.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.firkat.intervaltraining.R
import com.firkat.intervaltraining.core.model.IntervalSegment
import com.firkat.intervaltraining.feature.training.presentation.TrainingAction
import com.firkat.intervaltraining.feature.training.presentation.TrainingUiState
import com.firkat.intervaltraining.feature.training.presentation.TrainingViewModel
import com.firkat.intervaltraining.ui.components.BackButton
import com.firkat.intervaltraining.ui.components.GhostButton
import com.firkat.intervaltraining.ui.components.IntervalItem
import com.firkat.intervaltraining.ui.components.PrimaryButton
import com.firkat.intervaltraining.ui.components.TimerCard
import com.firkat.intervaltraining.ui.model.IntervalTimerState
import com.firkat.intervaltraining.ui.model.WorkoutTimerState
import com.firkat.intervaltraining.ui.theme.AppColor
import com.firkat.intervaltraining.ui.theme.AppSpacing
import com.firkat.intervaltraining.ui.theme.AppTypography
import com.firkat.intervaltraining.util.TimeFormatter

@Composable
fun TrainingRoute(viewModel: TrainingViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TrainingScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TrainingScreen(
    state: TrainingUiState,
    onAction: (TrainingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onAction(TrainingAction.DismissError)
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onAction(TrainingAction.RefreshTimer)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val currentSegment = state.segments.getOrNull(state.currentSegmentIndex)
    val currentSegmentElapsedSeconds = currentSegment?.elapsedSeconds ?: 0
    val currentSegmentTotalSeconds = currentSegment?.totalSeconds ?: 0
    val workoutRemainingSeconds = (state.workoutTotalSeconds - state.elapsedSeconds).coerceAtLeast(0)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    BackButton(
                        onClick = {
                        },
                    )
                },
                title = {
                    Text(text = state.workoutTitle)
                },
                actions = {
                    when (state.workoutTimerState) {
                        is WorkoutTimerState.Completed -> {
                            Text(
                                text = "Завершена",
                                style = AppTypography.label,
                                color = AppColor.secondary,
                            )
                        }

                        is WorkoutTimerState.Paused -> {
                            Image(
                                modifier = Modifier.size(12.dp),
                                imageVector = ImageVector.vectorResource(R.drawable.ic_pause),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(AppColor.surface),
                            )
                            Spacer(Modifier.width(AppSpacing.xs))
                            Text(
                                text = "Пауза",
                                style = AppTypography.label,
                                color = AppColor.orange,
                            )
                        }

                        is WorkoutTimerState.Pending -> {
                            Text(
                                text = TimeFormatter.formatIntervalTime(state.workoutTotalSeconds),
                                style = AppTypography.label,
                                color = AppColor.textTertiary,
                            )
                        }

                        is WorkoutTimerState.Started -> {
                            Box(
                                modifier =
                                    Modifier
                                        .size(12.dp)
                                        .background(AppColor.primary)
                                        .clip(CircleShape),
                            )
                            Text(
                                text = TimeFormatter.formatIntervalTime(workoutRemainingSeconds),
                                style = AppTypography.label,
                                color = AppColor.primary,
                            )
                        }
                    }
                },
                windowInsets = WindowInsets(left = 8.dp, right = 16.dp),
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = AppColor.bg,
                    ),
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = AppColor.bg,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val title =
                    when (state.workoutTimerState) {
                        is WorkoutTimerState.Completed -> "Отличная работа"
                        else -> currentSegment?.name ?: if (state.isLoading) "Загрузка" else "Нет интервалов"
                    }
                val timerCardTotalSeconds =
                    if (state.workoutTimerState is WorkoutTimerState.Completed) {
                        state.workoutTotalSeconds
                    } else {
                        currentSegmentTotalSeconds
                    }
                val timerCardElapsedSeconds =
                    if (state.workoutTimerState is WorkoutTimerState.Completed) {
                        state.elapsedSeconds
                    } else {
                        currentSegmentElapsedSeconds
                    }
                TimerCard(
                    title = title,
                    totalSeconds = timerCardTotalSeconds,
                    elapsedSeconds = timerCardElapsedSeconds,
                    state = state.workoutTimerState,
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Интервалы",
                        style = AppTypography.label,
                        color = AppColor.textSecondary,
                    )
                    Text(
                        text = "${if (state.segments.isEmpty()) 0 else state.currentSegmentIndex + 1} из ${state.segments.size}",
                        style = AppTypography.label,
                        color = AppColor.textTertiary,
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.s),
                ) {
                    items(
                        count = state.segments.size,
                    ) {
                        val interval = state.segments[it]
                        IntervalItem(
                            index = it + 1,
                            title = interval.name,
                            totalSeconds = interval.totalSeconds,
                            elapsedSeconds = interval.elapsedSeconds,
                            state =
                                when {
                                    state.workoutTimerState is WorkoutTimerState.Completed -> IntervalTimerState.Completed
                                    state.workoutTimerState !is WorkoutTimerState.Pending &&
                                        interval.elapsedSeconds >= interval.totalSeconds -> IntervalTimerState.Completed
                                    state.currentSegmentIndex == it -> state.timerState
                                    else -> IntervalTimerState.Pending
                                },
                        )
                    }
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(start = AppSpacing.l, end = AppSpacing.l, bottom = AppSpacing.l)
                        .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.s),
            ) {
                when (state.workoutTimerState) {
                    WorkoutTimerState.Pending -> {
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onAction(TrainingAction.StartPauseClicked) },
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_play),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(AppColor.surface),
                            )
                            Spacer(Modifier.width(AppSpacing.s))
                            Text(
                                text = "Старт",
                                style = AppTypography.button,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }

                    is WorkoutTimerState.Started -> {
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            accentColor = AppColor.orange,
                            onClick = { onAction(TrainingAction.StartPauseClicked) },
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_pause),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(AppColor.surface),
                            )
                            Spacer(Modifier.width(AppSpacing.s))
                            Text(
                                text = "Пауза",
                                style = AppTypography.button,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        GhostButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Сбросить тренировку",
                            negative = true,
                            onClick = { onAction(TrainingAction.ResetClicked) },
                        )
                    }

                    is WorkoutTimerState.Paused -> {
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onAction(TrainingAction.StartPauseClicked) },
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_play),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(AppColor.surface),
                            )
                            Spacer(Modifier.width(AppSpacing.s))
                            Text(
                                text = "Продолжить",
                                style = AppTypography.button,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        GhostButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Сбросить тренировку",
                            negative = true,
                            onClick = { onAction(TrainingAction.ResetClicked) },
                        )
                    }

                    is WorkoutTimerState.Completed -> {
                        PrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            accentColor = AppColor.secondary,
                            onClick = { onAction(TrainingAction.StartPauseClicked) },
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(R.drawable.ic_replay),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(AppColor.surface),
                            )
                            Spacer(Modifier.width(AppSpacing.s))
                            Text(
                                text = "Запустить заново",
                                style = AppTypography.button,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        GhostButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Новая тренировка",
                            negative = true,
                            onClick = { onAction(TrainingAction.ResetClicked) },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TrainingScreen_Preview() {
    TrainingScreen(
        state =
            TrainingUiState(
                workoutTitle = "Тренировка",
                currentSegmentIndex = 0,
                segments =
                    listOf(
                        IntervalSegment(name = "Разминка", totalSeconds = 60, elapsedSeconds = 0),
                        IntervalSegment(name = "Быстрый бег", totalSeconds = 45, elapsedSeconds = 0),
                        IntervalSegment(name = "Ходьба", totalSeconds = 30, elapsedSeconds = 0),
                        IntervalSegment(name = "Прыжки", totalSeconds = 40, elapsedSeconds = 0),
                        IntervalSegment(name = "Отдых", totalSeconds = 20, elapsedSeconds = 0),
                    ),
                elapsedSeconds = 0,
                workoutTotalSeconds = 300,
                workoutTimerState = WorkoutTimerState.Pending,
                timerState = IntervalTimerState.Pending,
            ),
        onAction = {},
    )
}
