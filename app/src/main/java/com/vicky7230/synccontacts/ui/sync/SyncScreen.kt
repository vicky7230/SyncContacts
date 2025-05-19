package com.vicky7230.synccontacts.ui.sync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vicky7230.synccontacts.data.models.User
import com.vicky7230.synccontacts.ui.theme.SyncContactsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncScreen(
    modifier: Modifier = Modifier,
    uiState: SyncUiState,
    onSyncClick: () -> Unit,
    onEditClick: (User) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Sync Contact") },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
            )
        }
    ){
        when (uiState) {
            is SyncUiState.Loading -> {
                Box(
                    modifier = modifier.padding(it),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(120.dp))
                }
            }

            is SyncUiState.Success -> {
                val users = uiState.users
                Column(modifier = modifier.padding(it)) {
                    UserList(
                        modifier = modifier.weight(1f),
                        users = users,
                        onEditClick = onEditClick
                    )
                    Button(
                        onClick = onSyncClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Sync Contacts to Device")
                    }
                }
            }

            is SyncUiState.Error -> {
                val message = uiState.message
                Box(modifier = modifier.padding(it), contentAlignment = Alignment.Center) {
                    Text(
                        text = message,
                        style = TextStyle(
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun UserList(
    modifier: Modifier = Modifier,
    users: List<User>,
    onEditClick: (User) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(users) { user ->
            UserCard(
                modifier = Modifier
                    .fillMaxWidth(),
                user = user,
                onEditClick = onEditClick
            )
        }
    }
}

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    user: User,
    onEditClick: (User) -> Unit
) {
    Card(
        modifier = modifier.padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Name: ${user.fullName}", style = MaterialTheme.typography.titleMedium)
            Text("Phone: ${user.phone}")
            Text("Email: ${user.email}")
            Text("Course: ${user.course}")
            Text("Enrolled On: ${user.enrolledOn}")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onEditClick(user) }
                ) {
                    Text("Edit")
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewSyncScreen(@PreviewParameter(SyncScreenPreviewProvider::class) state: SyncUiState) {
    SyncContactsTheme {
        SyncScreen(
            uiState = state,
            onSyncClick = {},
            onEditClick = {},
            onBackClick = {}
        )
    }
}