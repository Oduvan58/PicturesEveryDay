package by.geekbrains.pictureseveryday.repository

import by.geekbrains.pictureseveryday.domain.MainPictureServerResponseData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MainPictureApi {

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("api_key") apiKey: String,
    ): Call<MainPictureServerResponseData>

    @GET("planetary/apod")
    fun getPictureOfTheDay(
        @Query("date") date: String,
        @Query("api_key") apiKey: String,
    ): Call<MainPictureServerResponseData>
}