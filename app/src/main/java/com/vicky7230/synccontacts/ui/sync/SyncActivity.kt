package com.vicky7230.synccontacts.ui.sync

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vicky7230.synccontacts.data.models.User
import com.vicky7230.synccontacts.ui.edit.EditContactActivity
import com.vicky7230.synccontacts.ui.theme.SyncContactsTheme
import timber.log.Timber

class SyncActivity : ComponentActivity() {

    private val viewModel: SyncViewModel by viewModels()

    private val editContactLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val updatedUser =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getParcelableExtra("extra_user", User::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    result.data?.getParcelableExtra("extra_user")
                }
            updatedUser?.let {
                Timber.d("Updated user: ")
                Timber.d("Full name: ${updatedUser.fullName}")
                Timber.d("Email: ${updatedUser.email}")
                Timber.d("Phone: ${updatedUser.phone}")

                viewModel.updateContact(updatedUser)
            }
        }
    }

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, SyncActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fetchContacts()

        setContent {
            val state by viewModel.syncState.collectAsStateWithLifecycle()
            SyncContactsTheme {
                SyncScreen(
                    modifier = Modifier.fillMaxSize(),
                    uiState = state,
                    onSyncClick = {
                        viewModel.syncContactsToDevice({ _: Boolean, message: String ->
                            Toast.makeText(this@SyncActivity, message, Toast.LENGTH_SHORT).show()
                        })
                    },
                    onEditClick = {
                        val intent = EditContactActivity.getStartIntent(this@SyncActivity, it)
                        editContactLauncher.launch(intent)
                    },
                    onBackClick = {
                        finish()
                    }
                )
            }
        }
    }
}