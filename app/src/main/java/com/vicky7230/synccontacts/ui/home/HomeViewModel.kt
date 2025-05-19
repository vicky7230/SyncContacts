package com.vicky7230.synccontacts.ui.home

import android.app.Application
import android.provider.ContactsContract
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _displayContacts = mutableStateListOf<DisplayContact>()
    val displayContacts: List<DisplayContact> = _displayContacts

    fun loadContacts() {
        viewModelScope.launch {
            val displayContactList = withContext(Dispatchers.IO) {
                val contentResolver = getApplication<Application>().contentResolver
                val result = mutableListOf<DisplayContact>()

                val cursor = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null
                )

                cursor?.use {
                    val nameIndex =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val numberIndex =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

                    while (it.moveToNext()) {
                        val name = it.getString(nameIndex) ?: "Unknown"
                        val phone = it.getString(numberIndex) ?: "N/A"
                        result.add(DisplayContact(name, phone))
                    }
                }
                result
            }
            _displayContacts.clear()
            _displayContacts.addAll(displayContactList)
        }
    }
}
