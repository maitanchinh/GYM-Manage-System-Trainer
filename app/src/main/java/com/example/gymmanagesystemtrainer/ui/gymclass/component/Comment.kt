package com.example.gymmanagesystemtrainer.ui.gymClass.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymmanagesystemtrainer.R
import com.example.gymmanagesystemtrainer.model.Comment
import com.example.gymmanagesystemtrainer.ui.component.Gap

@Composable
fun Comment(modifier: Modifier, comment: Comment) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(comment.getUser()?.avatarUrl)
                .placeholder(
                    R.drawable.avatar_placeholder
                ).error(R.drawable.avatar_placeholder).build(),
            contentDescription = "Avatar",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(shape = RoundedCornerShape(25.dp))
        )
        Gap.k16.Width()
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clip(shape = RoundedCornerShape(16.dp))
                .background(color = MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = comment.getUser()?.name ?: "", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Gap.k8.Height()
                Text(text = comment.message!!)

            }
        }
    }
}