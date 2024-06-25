package com.example.gymmanagesystemtrainer.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LargeButton(text: String, isLoading: Boolean, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        enabled = enabled,
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp).background(MaterialTheme.colorScheme.primary).padding(8.dp))
        } else {
            Text(text, modifier = Modifier.padding(8.dp))
        }
    }
}