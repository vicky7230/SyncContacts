import android.app.Application
import android.content.ContentProviderOperation
import android.provider.ContactsContract
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vicky7230.synccontacts.ui.add.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddContactViewModel(application: Application) : AndroidViewModel(application) {

    private var _contact by mutableStateOf(Contact())
    val contact: Contact get() = _contact

    fun setContact(newContact: Contact) {
        _contact = newContact
    }

    fun saveContact(onComplete: (Boolean, String) -> Unit) {
        val context = getApplication<Application>().applicationContext

        if (_contact.firstName.isBlank() || _contact.mobile.isBlank()) {
            onComplete(false, "First name and mobile cannot be empty")
            return
        }

        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                try {
                    val ops = ArrayList<ContentProviderOperation>()
                    val rawContactInsertIndex = ops.size

                    ops.add(
                        ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                            .build()
                    )

                    ops.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID,
                                rawContactInsertIndex
                            )
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                                _contact.firstName
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME,
                                _contact.lastName
                            )
                            .build()
                    )

                    if (_contact.company.isNotBlank()) {
                        ops.add(
                            ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                                .withValueBackReference(
                                    ContactsContract.Data.RAW_CONTACT_ID,
                                    rawContactInsertIndex
                                )
                                .withValue(
                                    ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                                )
                                .withValue(
                                    ContactsContract.CommonDataKinds.Organization.COMPANY,
                                    _contact.company
                                )
                                .build()
                        )
                    }

                    ops.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(
                                ContactsContract.Data.RAW_CONTACT_ID,
                                rawContactInsertIndex
                            )
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.Phone.NUMBER,
                                _contact.mobile
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                            )
                            .build()
                    )

                    context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
                    true
                } catch (e: Exception) {
                    e.printStackTrace()
                    false
                }
            }
            onComplete(success, "Contact saved successfully")
        }
    }
}
