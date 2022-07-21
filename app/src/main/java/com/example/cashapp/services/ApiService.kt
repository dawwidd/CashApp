package com.example.cashapp.services

import com.example.cashapp.models.Category
import com.example.cashapp.models.Transaction
import com.example.cashapp.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/transactions")
    suspend fun getUserTransactions(@Query("user_id") user_id: Int = 0) : Response<ArrayList<Transaction>>

    @POST("/login")
    fun login(@Body user: User) : Call<User>

    @POST("/register")
    fun register(@Body user: User) : Call<User>

//    @POST("/categories")
//    fun addCategory(@Body category: Category) :
}