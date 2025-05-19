package com.vicky7230.synccontacts.ui.edit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vicky7230.synccontacts.data.models.User
import com.vicky7230.synccontacts.ui.add.Contact
import com.vicky7230.synccontacts.ui.theme.SyncContactsTheme

class EditContactActivity : ComponentActivity() {

    private val viewModel: EditContactViewModel by viewModels()

    companion object {
        private const val EXTRA_USER = "extra_user"

        fun getStartIntent(context: Context, user: User): Intent {
            return Intent(context, EditContactActivity::class.java).apply {
                putExtra(EXTRA_USER, user)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user =  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra(EXTRA_USER, User::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_USER)
        }
        if (user == null) {
            finish()
            return
        }

        viewModel.setUser(user)

        setContent {
            val currentUser by viewModel.userState.collectAsStateWithLifecycle()

            SyncContactsTheme {
                EditContactScreen(
                    modifier = Modifier.fillMaxSize(),
                    user = currentUser,
                    onUserChange = {
                        viewModel.updateUser(it)
                    },
                    onSaveClick = {
                        val resultIntent = Intent().apply {
                            putExtra(EXTRA_USER, currentUser)
                        }
                        setResult(RESULT_OK, resultIntent)
                        finish()
                    },
                    onBackClick = {
                        finish()
                    }
                )
            }
        }
    }
}