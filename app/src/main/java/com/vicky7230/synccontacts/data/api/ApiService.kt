package com.vicky7230.synccontacts.data.api

import com.vicky7230.synccontacts.data.models.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("contacts")
    suspend fun getContacts(): Response<ApiResponse>
}