package com.example.gymmanagesystemtrainer.ui.schedule.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymmanagesystemtrainer.R
import com.example.gymmanagesystemtrainer.model.Course
import com.example.gymmanagesystemtrainer.ui.component.Gap
import java.time.Duration
import java.time.LocalTime

@Composable
fun InDay(course: Course, onBorrowClick: (lessonId: String) -> Unit) {
    val lessons = course.classes.first().lessons
    val upcomingLesson = lessons[course.classes[0].lessonCount!!]
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.padding(12.dp)) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(course.thumbnailUrl)
                            .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                            .build(),
                        contentDescription = "Course Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = RoundedCornerShape(16.dp))
                    )
                    Gap.k16.Width()
                    Column {
                        Text(
                            text = "#" + (lessons.indexOf(upcomingLesson) + 1).toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        Gap.k8.Height()
                        Text(
                            text = "${upcomingLesson.name}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Gap.k8.Height()
                        Text(
                            text = "${
                                Duration.between(
                                    LocalTime.parse(course.classes[0].from),
                                    LocalTime.parse(course.classes[0].to)
                                ).toMinutes()
                            } min",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                IconButton(
                    colors = IconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ), onClick = {
                        onBorrowClick(upcomingLesson.id!!)
                    }) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_fitness_center_24),
                        contentDescription = "Borrow material"
                    )
                }
            }
            Gap.k4.Height()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "${course.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium

                    )
                    Gap.k8.Height()
                    LinearProgressIndicator(
                        progress = { course.classes[0].lessonCount!!.toFloat() / course.classes[0].totalLesson!!.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(8.dp))
                    )
                    Gap.k8.Height()
                    BasicText(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Black,
                                    fontWeight = FontWeight.Medium
                                )
                            ) { append("${course.classes[0].lessonCount}/${course.classes[0].totalLesson}") }
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Gray,
                                )
                            ) { append(" lessons completed!") }
                        },
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

    }
}