package com.vicky7230.synccontacts.ui.home

import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract

class ContactsObserver(
    handler: Handler,
    private val onContactsChanged: () -> Unit
) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        onContactsChanged()
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        onContactsChanged()
    }
}
