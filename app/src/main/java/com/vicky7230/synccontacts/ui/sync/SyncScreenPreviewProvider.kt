package com.vicky7230.synccontacts.ui.sync

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.vicky7230.synccontacts.data.models.User

class SyncScreenPreviewProvider : PreviewParameterProvider<SyncUiState> {
    override val values = sequenceOf(
        SyncUiState.Success(
            users = listOf(
                User(
                    id = "usr_001",
                    fullName = "Rohit Sharma",
                    phone = "+91-9876543210",
                    email = "rohit.sharma@example.com",
                    course = "Data Science Bootcamp",
                    enrolledOn = "2025-05-13"
                ),
                User(
                    id = "usr_002",
                    fullName = "Sneha Verma",
                    phone = "+91-9123456789",
                    email = "sneha.verma@example.com",
                    course = "Android Development",
                    enrolledOn = "2025-05-13"
                )
            )
        ),
        SyncUiState.Loading,
        SyncUiState.Error(message = "Something went wrong")
    )
}
