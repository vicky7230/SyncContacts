package com.vicky7230.synccontacts.ui.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vicky7230.synccontacts.ui.theme.SyncContactsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(
    modifier: Modifier = Modifier,
    onContactSaved: (Boolean, String) -> Unit,
    contact: Contact,
    onDataChange: (Contact) -> Unit,
    onSaveContactClick: ((Boolean, String) -> Unit) -> Unit,
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Save Contact") },
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
        },
    ) {
        Column(
            modifier = modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = contact.firstName,
                onValueChange = { onDataChange(contact.copy(firstName = it)) },
                label = { Text("First Name*") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contact.lastName,
                onValueChange = { onDataChange(contact.copy(lastName = it)) },
                label = { Text("Last Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contact.company,
                onValueChange = { onDataChange(contact.copy(company = it)) },
                label = { Text("Company") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = contact.mobile,
                onValueChange = { onDataChange(contact.copy(mobile = it)) },
                label = { Text("Mobile Number*") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
            )

            Button(
                onClick = { onSaveContactClick(onContactSaved) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Save Contact")
            }
        }
    }
}

@Preview
@Composable
fun PreviewAddContactScreen() {
    SyncContactsTheme {
        AddContactScreen(
            contact = Contact(
                firstName = "Vipin",
                lastName = "Kumar",
                company = "ABC",
                mobile = "9999999999"
            ),
            modifier = Modifier.fillMaxSize(),
            onContactSaved = { _: Boolean, message: String -> },
            onDataChange = { contact: Contact -> },
            onSaveContactClick = {},
            onBackClick = {}
        )
    }
}