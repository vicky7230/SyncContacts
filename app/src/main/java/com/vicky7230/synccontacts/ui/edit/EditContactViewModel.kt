package com.vicky7230.synccontacts.ui.edit

import androidx.lifecycle.ViewModel
import com.vicky7230.synccontacts.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditContactViewModel:ViewModel() {
    private val _userState = MutableStateFlow(User("", ",",",","","",""))
    val userState: StateFlow<User> = _userState.asStateFlow()

    fun setUser(user: User) {
        _userState.value = user
    }

    fun updateUser(user: User) {
        _userState.value = user
    }
}