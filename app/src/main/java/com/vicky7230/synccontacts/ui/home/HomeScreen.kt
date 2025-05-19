package com.vicky7230.synccontacts.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vicky7230.synccontacts.ui.theme.SyncContactsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    displayContacts: List<DisplayContact>,
    onAddContactClick: () -> Unit,
    onSyncContactClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Phone Contacts") },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    FloatingActionButton(
                        onClick = onSyncContactClick ,
                        containerColor = Color.Blue
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Sync", tint = Color.White)
                    }

                    FloatingActionButton(
                        onClick = onAddContactClick,
                        containerColor = Color.Blue
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .padding(padding)
        ) {
            items(displayContacts) { contact ->
                ContactItem(contact)
                Spacer(
                    modifier = Modifier
                        .background(color = Color.Black)
                        .height(1.dp)
                        .fillParentMaxWidth()
                )
            }
        }
    }
}

@Composable
fun ContactItem(displayContact: DisplayContact) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = displayContact.name, style = MaterialTheme.typography.titleMedium)
        Text(text = displayContact.phoneNumber, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    SyncContactsTheme {
        HomeScreen(
            modifier = Modifier.fillMaxSize(),
            displayContacts = listOf(
                DisplayContact(name = "Vipin", phoneNumber = "+91842568944"),
                DisplayContact(name = "Vipin", phoneNumber = "+91842568944"),
                DisplayContact(name = "Vipin", phoneNumber = "+91842568944"),
                DisplayContact(name = "Vipin", phoneNumber = "+91842568944"),
                DisplayContact(name = "Vipin", phoneNumber = "+91842568944")
            ),
            onAddContactClick = {},
            onSyncContactClick = {}
        )
    }
}