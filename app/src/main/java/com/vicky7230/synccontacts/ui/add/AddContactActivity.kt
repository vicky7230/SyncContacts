package com.vicky7230.synccontacts.ui.add

import AddContactViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.vicky7230.synccontacts.ui.theme.SyncContactsTheme

class AddContactActivity : ComponentActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, AddContactActivity::class.java)
        }
    }

    private val viewModel: AddContactViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val contact = viewModel.contact
            SyncContactsTheme {
                AddContactScreen(
                    contact = contact,
                    modifier = Modifier.fillMaxSize(),
                    onContactSaved = { result: Boolean, message: String ->
                        Toast.makeText(this@AddContactActivity, message, Toast.LENGTH_SHORT).show()
                        if (result) finish()
                    },
                    onDataChange = { contact: Contact ->
                        viewModel.setContact(contact)
                    },
                    onSaveContactClick = {
                        viewModel.saveContact(it)
                    },
                    onBackClick = { finish() }
                )
            }
        }
    }
}
