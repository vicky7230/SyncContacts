package com.vicky7230.synccontacts.ui.sync

import com.vicky7230.synccontacts.data.models.User

sealed class SyncUiState {
    data object Loading : SyncUiState()
    data class Success(val users: List<User>) : SyncUiState()
    data class Error(val message: String) : SyncUiState()
}