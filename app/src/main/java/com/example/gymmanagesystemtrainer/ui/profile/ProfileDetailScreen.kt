@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gymmanagesystemtrainer.ui.profile

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymmanagesystemtrainer.R
import com.example.gymmanagesystemtrainer.ui.component.Gap
import com.example.gymmanagesystemtrainer.ui.component.LargeButton
import com.example.gymmanagesystemtrainer.ui.component.TextField
import com.example.gymmanagesystemtrainer.utils.DataState
import com.example.gymmanagesystemtrainer.utils.parseDateTime
import com.example.gymmanagesystemtrainer.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ProfileDetailScreen(
    profileViewModel: ProfileViewModel = hiltViewModel(),
    userId: String
) {
    val context = LocalContext.current
    val userState = profileViewModel.userState.collectAsState()
    val userUpdatedState by profileViewModel.userUpdated.collectAsState()
    val genderOptions = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }
    val name by profileViewModel.name.collectAsState()
    val phone by profileViewModel.phone.collectAsState()
    val gender by profileViewModel.gender.collectAsState()
    val dateOfBirth by profileViewModel.dateOfBirth.collectAsState()
    val status by profileViewModel.status.collectAsState()
    val avatar by profileViewModel.avatar.collectAsState()
    val bitmap by profileViewModel.imageBitmap.collectAsState()
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    var showSelectImageDialog by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val galleryPermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    val launcherTakePhoto =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            profileViewModel.setImageBitmap(it)
            profileViewModel.setAvatarImage(saveBitmapToFile(it))
        }

    val launcherPickImage =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            profileViewModel.setImageBitmap(it?.let { getBitmapFromUri(context, it) })
            profileViewModel.setAvatarImage(saveBitmapToFile(it?.let {
                getBitmapFromUri(
                    context,
                    it
                )
            }))
        }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(selectedYear, selectedMonth, selectedDay)
            val localDateTime = LocalDateTime.of(
                selectedYear,
                selectedMonth + 1,
                selectedDay,
                0,
                0, 0, 0
            )
            profileViewModel.onDateOfBirthChange(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        }, year, month, day
    )

    LaunchedEffect(key1 = userId) {
        profileViewModel.getUserById(userId)
    }

    LaunchedEffect(key1 = userUpdatedState) {
        if (userUpdatedState is DataState.Success) {
            Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show()
        } else if (userUpdatedState is DataState.Error) {
            Toast.makeText(
                context,
                "Update failed: ${(userUpdatedState as DataState.Error).message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    when (userState.value) {
        is DataState.Loading -> {
            CircularProgressIndicator(modifier = Modifier
                .padding(300.dp)
                .fillMaxSize())
        }

        is DataState.Error -> {
            Text(text = "Error: ${(userState.value as DataState.Error).message}")
        }

        is DataState.Success -> {
            val user = (userState.value as DataState.Success).data
            println("User: $user")
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box {
                    if (bitmap == null)
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(user.avatarUrl)
                                .placeholder(R.drawable.avatar_placeholder)
                                .error(R.drawable.avatar_placeholder)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(75.dp)),
                            contentScale = ContentScale.Crop
                        )
                    else bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Image",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(75.dp))
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(75.dp))
                            .background(color = Color.Gray.copy(alpha = 0.5f))
                            .clickable { showSelectImageDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.round_camera_alt_24),
                            contentDescription = "Update avatar"
                        )
                    }
                }
                Gap.k32.Height()
                TextField(
                    label = "Name",
                    value = name,
                    onTextChange = { profileViewModel.onNameChange(it) })
                Gap.k16.Height()
                TextField(
                    label = "Phone",
                    value = phone,
                    keyboardOption = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    onTextChange = { profileViewModel.onPhoneChange(it) })
                Gap.k16.Height()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.White)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            expanded = true
                        }, contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = gender,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    DropdownMenu(
                        modifier = Modifier
                            .padding(16.dp)
                            .clip(shape = RoundedCornerShape(16.dp)),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        genderOptions.forEach { option ->
                            Text(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        profileViewModel.onGenderChange(option)
                                        expanded = false
                                    },
                                text = option
                            )
                        }

                    }
                }
                Gap.k16.Height()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(color = Color.White)
                        .border(
                            width = 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable {
                            datePickerDialog.show()
                        }, contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = parseDateTime(dateOfBirth).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                }
                Gap.k32.Height()
                LargeButton(
                    text = "Save",
                    isLoading = userUpdatedState is DataState.Loading,
                    onClick = {
                        profileViewModel.updateProfile(
                            id = user.id!!,
                            name = name,
                            phone = phone,
                            gender = gender,
                            status = null,
                            dateOfBirth = dateOfBirth,
                            avatar = avatar
                        )
                    })
//                Gap.k16.Height()
//                TextButton(onClick = { /*TODO*/ }) {
//                    Text(text = "Change password")
//                }
            }
        }

        else -> {
        }
    }
    if (showSelectImageDialog) {
        AlertDialog(
            onDismissRequest = { showSelectImageDialog = false },
            confirmButton = { /*TODO*/ },
            dismissButton = { Text(text = "Cancel") },
            text = {
                Column {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(shape = RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable(onClick = {
                                showSelectImageDialog = false

                                if (cameraPermissionState.status.isGranted)
                                    launcherTakePhoto.launch()
                                else {
                                    cameraPermissionState.launchPermissionRequest()
                                    launcherTakePhoto.launch()
                                }
                            }),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.round_photo_camera_24),
                                contentDescription = "Take Photo"
                            )
                            Gap.k16.Width()
                            Text(text = "Take a photo")
                        }
                    }
                    Gap.k16.Height()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(shape = RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable(onClick = {
                                showSelectImageDialog = false
                                if (galleryPermissionState.status.isGranted)
                                    launcherPickImage.launch("image/*")
                                else {
                                    galleryPermissionState.launchPermissionRequest()
                                    launcherPickImage.launch("image/*")
                                }
                            }),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.round_folder_24),
                                contentDescription = "Choose from Gallery"
                            )
                            Gap.k16.Width()
                            Text(text = "Choose from Gallery")
                        }
                    }
                }

            })
    }
}

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun saveBitmapToFile(bitmap: Bitmap?): File? {
    val file = File.createTempFile("avatar", ".jpg")
    try {
        val outputStream = FileOutputStream(file)
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        e.printStackTrace()
        return null
    }
    return file
}
