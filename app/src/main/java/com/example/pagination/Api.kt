package com.example.pagination

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {
    @GET("users")
    fun getUsers(
        @QueryMap parameter: HashMap<String,String>
    ): Call<UsersResponses>
}