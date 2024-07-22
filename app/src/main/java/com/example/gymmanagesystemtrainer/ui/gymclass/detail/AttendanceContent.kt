package com.example.gymmanagesystemtrainer.ui.gymclass.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.gymmanagesystemtrainer.model.Attendance
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.Lesson
import com.example.gymmanagesystemtrainer.ui.component.shimmerLoadingAnimation
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.utils.parseDateTime
import com.example.gymmanagesystemtrainer.viewmodel.AttendanceViewModel
import com.example.gymmanagesystemtrainer.viewmodel.LessonViewModel
import com.example.gymmanagesystemtrainer.viewmodel.UserViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AttendanceContent(
    modifier: Modifier,
    classId: String,
    attendanceViewModel: AttendanceViewModel = hiltViewModel(),
    lessonViewModel: LessonViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    onScanClick: () -> Unit
) {
    val attendancesState by attendanceViewModel.attendances.collectAsState()
    val lessonsState by lessonViewModel.lessons.collectAsState()
    val isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)
    var qrString by remember { mutableStateOf("") }
    var qrCodeImage by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(Unit) {
        lessonViewModel.getLessons(
            FilterRequestBody(
                classId = classId,
                orderBy = "StartTime",
                isAscending = true
            )
        )
        attendanceViewModel.fetchAttendances(
            FilterRequestBody(
                classId = classId,
                orderBy = "CreateAt",
                isAscending = true
            )
        )
    }
    SwipeRefresh(
        modifier = modifier,
        state = swipeRefreshState, onRefresh = {
            lessonViewModel.getLessons(
                FilterRequestBody(
                    classId = classId,
                    orderBy = "StartTime",
                    isAscending = true
                )
            )
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
                when (attendancesState) {
                    is DataState.Success -> {
                        val attendances = (attendancesState as DataState.Success).data.data
                        val attendanceLessons = mapAttendanceToSlots(attendances, lessons)
                        val lastPastLessonIndex = attendanceLessons.indexOfLast { it.isPast }
                        val listState = rememberLazyListState()
                        LaunchedEffect(lastPastLessonIndex) {
                            if (lastPastLessonIndex != -1) {
                                listState.scrollToItem(lastPastLessonIndex - 1)
                            }
                        }
                        LazyColumn(
                            modifier = modifier,
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(attendanceLessons.size) { index ->
                                val startTime =
                                    parseDateTime(attendanceLessons[index].startTime!!).format(
                                        DateTimeFormatter.ofPattern("HH:mm")
                                    )
                                val endTime =
                                    parseDateTime(attendanceLessons[index].endTime!!).format(
                                        DateTimeFormatter.ofPattern("HH:mm")
                                    )
                                val date =
                                    parseDateTime(attendanceLessons[index].startTime!!).format(
                                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                                    )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .background(color = if (lastPastLessonIndex == index - 1) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.secondaryContainer)
                                        .clickable {
                                            if (!attendanceLessons[index].isPast) {
                                                onScanClick()
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
                                    }
                                }
                            }
                        }
                    }

                    is DataState.Error -> {
                        Text(text = (attendancesState as DataState.Error).message)
                    }

                    else -> {}
                }


            }

            is DataState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = (lessonsState as DataState.Error).message)
                }
            }

            else -> {}
        }
    }
}


fun mapAttendanceToSlots(
    attendances: ArrayList<Attendance>,
    lessons: ArrayList<Lesson>
): ArrayList<Lesson> {
    val attendanceSet = attendances.map { it.slot!!.id }.toSet()
    val currentTime = LocalDateTime.now()
    lessons.forEach { lesson ->
        lesson.isAttendance = attendanceSet.contains(lesson.id)
        lesson.isPast = parseDateTime(lesson.endTime!!).isBefore(currentTime)
    }
    return lessons
}

