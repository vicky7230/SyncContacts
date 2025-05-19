package com.vicky7230.synccontacts.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val success: Boolean,
    @SerialName("Data")
    val data: UserData
)

@Serializable
data class UserData(
    val date: String,
    val totalUsers: Int,
    val users: List<User>
)

@Parcelize
@Serializable
data class User(
    val id: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val course: String,
    val enrolledOn: String
): Parcelable