package com.example.gymmanagesystemtrainer.ui.equipment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymmanagesystemtrainer.R
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.network.BorrowRequestBody
import com.example.gymmanagesystemtrainer.ui.component.Gap
import com.example.gymmanagesystemtrainer.ui.component.IconTextField
import com.example.gymmanagesystemtrainer.ui.component.LargeButton
import com.example.gymmanagesystemtrainer.ui.component.shimmerLoadingAnimation
import com.example.gymmanagesystemtrainer.ui.theme.ForestGreen
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.viewmodel.EquipmentViewModel
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun BorrowEquipmentScreen(
    slotId: String,
    onBack: () -> Unit,
    onBorrowRequestListClick: () -> Unit,
    equipmentViewModel: EquipmentViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val equipmentsState by equipmentViewModel.equipments.collectAsState()
    val borrowEquipmentState by equipmentViewModel.equipmentsRequested.collectAsState()
    var searchText by remember { mutableStateOf("") }
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    var selectedEquipments by remember { mutableStateOf<List<String>?>(null) }

    LaunchedEffect(searchText) {
        equipmentViewModel.fetchEquipments(FilterRequestBody(search = searchText))
    }

    LaunchedEffect(borrowEquipmentState) {
        if (borrowEquipmentState is DataState.Success) {
            Toast.makeText(
                context,
                "Request sent successfully",
                Toast.LENGTH_SHORT
            ).show()
            onBack()
        } else if (borrowEquipmentState is DataState.Error) {
            Toast.makeText(
                context,
                "Request failed, please try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        isRefreshing = true
        equipmentViewModel.fetchEquipments(FilterRequestBody())
        isRefreshing = false
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Gap.k16.Height()
            IconTextField(
                value = searchText,
                placeholder = "Search equipment",
                onValueChange = { searchText = it })
            Gap.k16.Height()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LargeButton(
                    modifier = Modifier.weight(1f),
                    text = "Send Request",
                    isLoading = borrowEquipmentState is DataState.Loading
                ) {
                    if (selectedEquipments != null && selectedEquipments!!.isNotEmpty())
                        equipmentViewModel.borrowRequestList(
                            BorrowRequestBody(
                                slotId = slotId,
                                equipmentIds = selectedEquipments!!
                            )
                        )
                }
                Gap.k16.Width()
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .width(50.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(color = MaterialTheme.colorScheme.secondaryContainer)
                        .clickable {
                            onBorrowRequestListClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_format_list_bulleted_24),
                        contentDescription = "Request List",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }
            Gap.k16.Height()
            when (equipmentsState) {
                is DataState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is DataState.Success -> {
                    val equipments = (equipmentsState as DataState.Success).data.data
                    if (equipments.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No equipment found",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else
                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                        ) {
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                mainAxisSpacing = 16.dp,
                                crossAxisSpacing = 16.dp
                            ) {
                                equipments.chunked(2).forEach { pair ->
                                    pair.forEach {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.48f)
                                                .clip(shape = RoundedCornerShape(16.dp))
                                                .background(Color.White)
                                        ) {
                                            Column {
                                                AsyncImage(
                                                    model = ImageRequest.Builder(LocalContext.current)
                                                        .data(it.thumbnailUrl).placeholder(
                                                            R.drawable.placeholder
                                                        ).error(R.drawable.error).build(),
                                                    contentDescription = "Equipment Image",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier
                                                        .height(100.dp)
                                                )
                                                Column(modifier = Modifier.padding(8.dp)) {
                                                    Text(
                                                        text = it.name!!,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Gap.k16.Width()
                                                    Text(
                                                        text = it.status.toString(),
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (it.status == "Active") ForestGreen else Color.Gray,

                                                        )
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        it.description?.let { description ->
                                                            Text(
                                                                modifier = Modifier.weight(1f),
                                                                text = description,
                                                                overflow = TextOverflow.Ellipsis,
                                                                maxLines = 1
                                                            )
                                                        }
                                                        if (it.status == "Active") {
                                                            if (selectedEquipments == null || selectedEquipments?.contains(
                                                                    it.id!!
                                                                ) == false
                                                            )
                                                                Icon(
                                                                    modifier = Modifier.clickable {
                                                                        selectedEquipments =
                                                                            if (selectedEquipments == null) listOf(
                                                                                it.id!!
                                                                            ) else {
                                                                                selectedEquipments!!.plus(
                                                                                    it.id!!
                                                                                )
                                                                            }
                                                                    },
                                                                    painter = painterResource(id = R.drawable.round_radio_button_unchecked_32),
                                                                    contentDescription = "Select",
                                                                    tint = Color.Gray
                                                                )
                                                            else if (selectedEquipments != null && selectedEquipments!!.contains(
                                                                    it.id!!
                                                                )
                                                            ) {
                                                                println(
                                                                    "Contained: ${
                                                                        selectedEquipments!!.contains(
                                                                            it.id!!
                                                                        )
                                                                    }"
                                                                )
                                                                Icon(
                                                                    modifier = Modifier.clickable {
                                                                        if (selectedEquipments != null && selectedEquipments!!.contains(
                                                                                it.id!!
                                                                            )
                                                                        )
                                                                            selectedEquipments =
                                                                                selectedEquipments!!.minus(
                                                                                    it.id!!
                                                                                )
                                                                    },
                                                                    painter = painterResource(id = R.drawable.round_check_circle_32),
                                                                    contentDescription = "Unselect",
                                                                    tint = MaterialTheme.colorScheme.primary
                                                                )
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }


                            }
                        }
                }

                is DataState.Error -> {

                }

                else -> {}
            }

        }
    }
}