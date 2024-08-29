package com.example.gymmanagesystemtrainer.ui.equipment.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.ImagePainter
import coil.request.ImageRequest
import com.example.gymmanagesystemtrainer.R
import com.example.gymmanagesystemtrainer.model.SlotEquipment
import com.example.gymmanagesystemtrainer.ui.component.Gap
import com.example.gymmanagesystemtrainer.ui.theme.ForestGreen
import com.example.gymmanagesystemtrainer.ui.theme.GoldYellow

@Composable
fun RequestItem(it: SlotEquipment, onRepayClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier
                .weight(1f)
                .padding(16.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.equipment?.thumbnailUrl).placeholder(
                            R.drawable.placeholder
                        ).error(R.drawable.error).build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(70.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(8.dp))
                )
                Gap.k16.Width()
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = it.equipment!!.name.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = it.status.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (it.status!!.contains("Accept")) ForestGreen else if (it.status!!.contains(
                                "Reject"
                            )
                        ) MaterialTheme.colorScheme.error else GoldYellow
                    )
                }
            }
            if (it.status == "Accept borrow") IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    painter = painterResource(id = R.drawable.round_more_vert_24),
                    contentDescription = "Menu"
                )
            }
            DropdownMenu(
                expanded = expanded,
                offset = DpOffset(x = LocalConfiguration.current.screenWidthDp.dp, y = (-70).dp),
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onRepayClick()
                    },
                    text = {
                        Text(
                            "Repay request",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    })
            }
        }
    }
}