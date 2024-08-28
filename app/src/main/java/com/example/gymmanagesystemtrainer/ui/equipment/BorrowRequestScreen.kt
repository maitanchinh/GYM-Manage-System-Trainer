package com.example.gymmanagesystemtrainer.ui.equipment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymmanagesystemtrainer.R
import com.example.gymmanagesystemtrainer.model.FilterRequestBody
import com.example.gymmanagesystemtrainer.model.SlotEquipment
import com.example.gymmanagesystemtrainer.ui.component.Gap
import com.example.gymmanagesystemtrainer.ui.component.IconTextField
import com.example.gymmanagesystemtrainer.ui.theme.ForestGreen
import com.example.gymmanagesystemtrainer.ui.theme.GoldYellow
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.viewmodel.EquipmentViewModel
import com.example.gymmanagesystemtrainer.viewmodel.ProfileViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BorrowRequestScreen(
    slotId: String,
    equipmentViewModel: EquipmentViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val slotEquipmentsState by equipmentViewModel.slotEquipments.collectAsState()
    val slotEquipmentState by equipmentViewModel.slotEquipment.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    var searchText by remember { mutableStateOf("") }
    LaunchedEffect(searchText) {
        equipmentViewModel.fetchSlotEquipments(
            FilterRequestBody(
                search = searchText,
                slotId = slotId,
                trainerId = profileViewModel.getUser()!!.id
            )
        )
    }

    LaunchedEffect(slotEquipmentState) {
        if (slotEquipmentState is DataState.Success) {
            equipmentViewModel.fetchSlotEquipments(
                FilterRequestBody(
                    search = searchText,
                    slotId = slotId,
                    trainerId = profileViewModel.getUser()!!.id
                )
            )
            Toast.makeText(context, "Request updated", Toast.LENGTH_SHORT).show()
        } else if (slotEquipmentState is DataState.Error) {
            Toast.makeText(context, (slotEquipmentState as DataState.Error).message, Toast.LENGTH_SHORT).show()
        }

    }

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        isRefreshing = true
        equipmentViewModel.fetchSlotEquipments(
            FilterRequestBody(
                slotId = slotId,
                trainerId = profileViewModel.getUser()!!.id
            )
        )
        isRefreshing = false
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Gap.k16.Height()
            IconTextField(
                value = searchText,
                placeholder = "Search equipment",
                onValueChange = { searchText = it })
            Gap.k16.Height()
            when (slotEquipmentsState) {
                is DataState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is DataState.Success -> {
                    val slotEquipments =
                        (slotEquipmentsState as DataState.Success).data.data
                    if (slotEquipments.isEmpty()) {
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
                        slotEquipments.forEach { se ->
                            val swipeToDismissBoxState = rememberSwipeToDismissBoxState(
                                confirmValueChange = {
                                    if (it == SwipeToDismissBoxValue.EndToStart) {
                                        equipmentViewModel.deleteSlotEquipment(se.id!!)
                                    }
                                    true
                                }
                            )
                            SwipeToDismissBox(state = swipeToDismissBoxState, enableDismissFromEndToStart = true,
                                enableDismissFromStartToEnd = false, backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(32.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.round_delete_24),
                                            contentDescription = "Delete Inquiry",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                },
                                content = {
                                    RequestItem(se)
                                })
                            Gap.k16.Height()
                        }
                }

                is DataState.Error -> {
                    val error = (slotEquipmentsState as DataState.Error).message

                }

                else -> {}
            }
        }
    }
}

@Composable
private fun RequestItem(it: SlotEquipment) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(16.dp))
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
    }
}