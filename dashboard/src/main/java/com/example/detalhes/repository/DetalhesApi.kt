package com.example.detalhes.repository

import com.example.dashboard.data.models.PlacesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DetalhesApi{
    @GET("nearbysearch/json")
    fun searchNearby(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String
    ): PlacesResponse
}