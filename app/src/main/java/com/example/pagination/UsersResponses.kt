package com.example.pagination

data class UsersResponses(
    val page: Int,
    val total_pages: Int,
    val data: ArrayList<User>
)