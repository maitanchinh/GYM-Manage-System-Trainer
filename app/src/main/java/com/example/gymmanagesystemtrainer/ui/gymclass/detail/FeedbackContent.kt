package com.example.gymmanagesystemtrainer.ui.gymclass.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymmanagesystemtrainer.R
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.ui.component.shimmerLoadingAnimation
import com.example.gymmanagesystemtrainer.ui.gymclass.component.FeedbackItem
import com.example.gymmanagesystemtrainer.ui.theme.ForestGreen
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.utils.parseDateTime
import com.example.gymmanagesystemtrainer.viewmodel.FeedbackViewModel
import com.example.gymmanagesystemtrainer.viewmodel.LessonViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun FeedbackContent(
    modifier: Modifier = Modifier,
    feedbackViewModel: FeedbackViewModel = hiltViewModel(),
    lessonViewModel: LessonViewModel = hiltViewModel(),
    classId: String
) {
    val context = LocalContext.current
    val tabs = listOf("Feedbacks", "Lessons")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val feedbacksState by feedbackViewModel.feedbacks.collectAsState()
    val lessonsState by lessonViewModel.lessons.collectAsState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    val isShowDialog by feedbackViewModel.isShowOpenFeedbackDialog.collectAsState()
    val lessonChangedFeedbackStatus by feedbackViewModel.lessonChangedFeedbackStatus.collectAsState()
    val openCloseFeedbackState by feedbackViewModel.openCloseFeedback.collectAsState()

    LaunchedEffect(Unit) {
        feedbackViewModel.getFeedbacks(
            filterRequestBody = FilterRequestBody(
                classId = classId
            )
        )
        lessonViewModel.getLessons(filterRequestBody = FilterRequestBody(classId = classId))
    }

    LaunchedEffect(openCloseFeedbackState) {
        if (openCloseFeedbackState is DataState.Success) {
            lessonViewModel.getLessons(filterRequestBody = FilterRequestBody(classId = classId))
            Toast.makeText(context, "Open/Close feedback successfully", Toast.LENGTH_SHORT).show()
        } else if (openCloseFeedbackState is DataState.Error) {
            Toast.makeText(context, "Open/Close feedback failed", Toast.LENGTH_SHORT).show()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEach { tab ->
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(8.dp))
                    .background(
                        color = if (selectedTabIndex == tabs.indexOf(tab)) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                    )
                    .clickable { selectedTabIndex = tabs.indexOf(tab) }
                    .padding(8.dp)
            ) {
                Text(
                    text = tab,
                    color = if (selectedTabIndex == tabs.indexOf(tab)) MaterialTheme.colorScheme.primary else Color.Gray,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
    when (selectedTabIndex) {
        0 -> SwipeRefresh(
            modifier = modifier.padding(vertical = 16.dp),
            state = swipeRefreshState, onRefresh = {
                isRefreshing = true
                feedbackViewModel.getFeedbacks(
                    filterRequestBody = FilterRequestBody(
                        classId = classId
                    )
                )
                isRefreshing = false
            }) {
            when (feedbacksState) {
                is DataState.Loading -> {
                    Text(text = "Loading")
                }

                is DataState.Success -> {
                    val feedbacks = (feedbacksState as DataState.Success).data.data
                    LazyColumn {
                        items(feedbacks.size) { index ->
                            FeedbackItem(feedbacks[index])
                        }
                    }
                }

                is DataState.Error -> {
                    Text(text = "Something went wrong, please try again later")
                }

                else -> {}
            }
        }

        1 -> SwipeRefresh(
            modifier = modifier.padding(vertical = 16.dp),
            state = swipeRefreshState, onRefresh = {
                isRefreshing = true
                lessonViewModel.getLessons(filterRequestBody = FilterRequestBody(classId = classId))
                isRefreshing = false
            }) {
            when (lessonsState) {
                is DataState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                            .shimmerLoadingAnimation()
                    )
                }

                is DataState.Success -> {
                    val lessons = (lessonsState as DataState.Success).data.data
                    val listState = rememberLazyListState()
                    val currentLesson = lessons.first {parseDateTime(it.endTime!!).isAfter(LocalDateTime.now()) }
                    LaunchedEffect(Unit) {
                        listState.scrollToItem(lessons.indexOf(currentLesson))
                    }
                    LazyColumn(
                        modifier = modifier,
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(lessons.size) { index ->
                            val startTime =
                                parseDateTime(lessons[index].startTime!!).format(
                                    DateTimeFormatter.ofPattern("HH:mm")
                                )
                            val endTime =
                                parseDateTime(lessons[index].endTime!!).format(
                                    DateTimeFormatter.ofPattern("HH:mm")
                                )
                            val date =
                                parseDateTime(lessons[index].startTime!!).format(
                                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(shape = RoundedCornerShape(8.dp))
                                    .background(color = if (currentLesson.id == lessons[index].id) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.secondaryContainer)
                                    .clickable {
                                        if (currentLesson.id == lessons[index].id) {
                                            feedbackViewModel.setShowOpenFeedbackDialog()
                                            feedbackViewModel.setLessonChangedFeedbackStatus(lessons[index])
                                        }
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Column {
                                        Text(
                                            text = lessons[index].name!!,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Text(text = "$startTime - $endTime  $date")
                                    }
                                    if (lessons[index].feedbackStatus!!)
                                        Icon(
                                            painter = painterResource(id = R.drawable.round_lock_open_32),
                                            contentDescription = "Open feedback",
                                            tint = ForestGreen
                                        )
                                    else Icon(
                                        painter = painterResource(id = R.drawable.round_lock_outline_32),
                                        contentDescription = "Close feedback"
                                    )
                                }
                            }
                        }
                    }
                }

                is DataState.Error -> {
                    Text(text = "Something went wrong, please try again later")
                }

                else -> {}
            }
        }
    }

    if (isShowDialog) {
        AlertDialog(
            onDismissRequest = { feedbackViewModel.setShowOpenFeedbackDialog() },
            confirmButton = {
                TextButton(onClick = {
                    feedbackViewModel.setShowOpenFeedbackDialog()
                    feedbackViewModel.openCloseFeedback(lessonChangedFeedbackStatus?.id!!)
                }) {
                    Text(text = "Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { feedbackViewModel.setShowOpenFeedbackDialog() }) {
                    Text(text = "Cancel", color = MaterialTheme.colorScheme.onSecondaryContainer)
                }
            },
            text = {
                if (!lessonChangedFeedbackStatus?.feedbackStatus!!) {
                    Text(text = "Do you want to open feedback for this lesson?")
                } else {
                    Text(text = "Do you want to close feedback for this lesson?")
                }
            })
    }
}

