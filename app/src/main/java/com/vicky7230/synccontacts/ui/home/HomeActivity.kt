package com.vicky7230.synccontacts.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.vicky7230.synccontacts.ui.add.AddContactActivity
import com.vicky7230.synccontacts.ui.sync.SyncActivity
import com.vicky7230.synccontacts.ui.theme.SyncContactsTheme

class HomeActivity : ComponentActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private var contactsObserver: ContactsObserver? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val readGranted = permissions[Manifest.permission.READ_CONTACTS] == true
            val writeGranted = permissions[Manifest.permission.WRITE_CONTACTS] == true

            if (readGranted && writeGranted) {
                registerContactsObserver()
                viewModel.loadContacts()
            } else {
                Toast.makeText(
                    this,
                    "Both permissions are required to sync contacts",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestPermission()

        setContent {
            SyncContactsTheme {
                val contacts = viewModel.displayContacts
                HomeScreen(
                    modifier = Modifier.fillMaxSize(),
                    displayContacts = contacts,
                    onSyncContactClick = {
                        startActivity(SyncActivity.getStartIntent(this@HomeActivity))
                    },
                    onAddContactClick = {
                        startActivity(AddContactActivity.getStartIntent(this@HomeActivity))
                    }
                )
            }
        }
    }

    private fun registerContactsObserver() {
        if (contactsObserver == null) {
            contactsObserver = ContactsObserver(Handler(Looper.getMainLooper())) {
                if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    viewModel.loadContacts()
                }
            }
            contentResolver.registerContentObserver(
                ContactsContract.Contacts.CONTENT_URI,
                true,
                contactsObserver!!
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contactsObserver?.let { contentResolver.unregisterContentObserver(it) }
    }

    private fun requestPermission() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED

        if (hasReadPermission && hasWritePermission) {
            registerContactsObserver()
            viewModel.loadContacts()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_CONTACTS,
                    Manifest.permission.WRITE_CONTACTS
                )
            )
        }
    }
}