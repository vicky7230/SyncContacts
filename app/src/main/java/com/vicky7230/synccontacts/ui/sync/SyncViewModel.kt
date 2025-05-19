package com.vicky7230.synccontacts.ui.sync

import android.app.Application
import android.content.ContentProviderOperation
import android.provider.ContactsContract
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vicky7230.synccontacts.data.api.RetrofitInstance
import com.vicky7230.synccontacts.data.models.ApiResponse
import com.vicky7230.synccontacts.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SyncViewModel(application: Application) : AndroidViewModel(application) {

    private val _syncState = MutableStateFlow<SyncUiState>(SyncUiState.Loading)
    val syncState: StateFlow<SyncUiState> = _syncState.asStateFlow()

    fun fetchContacts() {
        viewModelScope.launch {
            try {
                val response: Response<ApiResponse> = RetrofitInstance.api.getContacts()
                if (response.isSuccessful) {
                    val data = response.body()
                    if (data != null && data.success) {
                        _syncState.value = SyncUiState.Success(data.data.users)
                    } else {
                        _syncState.value = SyncUiState.Error("Invalid response")
                    }
                } else {
                    _syncState.value = SyncUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _syncState.value = SyncUiState.Error("Exception: ${e.message}")
            }
        }
    }

    fun syncContactsToDevice(
        onResult: (Boolean, String) -> Unit
    ) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                try {
                    if(syncState.value is SyncUiState.Success) {
                        (syncState.value as SyncUiState.Success).users.forEach { user ->
                            val ops = ArrayList<ContentProviderOperation>()

                            ops.add(
                                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                                    .build()
                            )

                            ops.add(
                                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(
                                        ContactsContract.Data.MIMETYPE,
                                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                                    )
                                    .withValue(
                                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                                        user.fullName
                                    )
                                    .build()
                            )

                            ops.add(
                                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(
                                        ContactsContract.Data.MIMETYPE,
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                                    )
                                    .withValue(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                                        user.phone
                                    )
                                    .withValue(
                                        ContactsContract.CommonDataKinds.Phone.TYPE,
                                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                                    )
                                    .build()
                            )

                            ops.add(
                                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                                    .withValue(
                                        ContactsContract.Data.MIMETYPE,
                                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                                    )
                                    .withValue(
                                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                                        user.email
                                    )
                                    .withValue(
                                        ContactsContract.CommonDataKinds.Email.TYPE,
                                        ContactsContract.CommonDataKinds.Email.TYPE_WORK
                                    )
                                    .build()
                            )

                            getApplication<Application>().contentResolver.applyBatch(
                                ContactsContract.AUTHORITY,
                                ops
                            )
                        }
                        return@withContext true
                    }
                    return@withContext false
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext false
                }
            }

            if (success) {
                onResult(true, "Contacts synced successfully!")
            } else {
                onResult(false, "Failed to sync contacts")
            }
        }
    }

    fun updateContact(updatedUser: User) {
        val currentState = _syncState.value
        if (currentState is SyncUiState.Success) {
            val updatedList = currentState.users.map { user ->
                if (user.id == updatedUser.id) updatedUser else user
            }
            _syncState.value = SyncUiState.Success(updatedList)
        }
    }
}