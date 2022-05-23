package by.geekbrains.pictureseveryday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.geekbrains.pictureseveryday.App
import by.geekbrains.pictureseveryday.BuildConfig
import by.geekbrains.pictureseveryday.R
import by.geekbrains.pictureseveryday.domain.MainPictureServerResponseData
import by.geekbrains.pictureseveryday.repository.MainPictureRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPictureViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: MainPictureRetrofitImpl = MainPictureRetrofitImpl(),
) : ViewModel() {

    fun getLiveData(date: String?): LiveData<AppState> {
        sendServerRequest(date)
        return liveDataToObserve
    }

    private fun sendServerRequest(date: String?) {
        liveDataToObserve.value = AppState.Loading
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            AppState.Error(Throwable(App.appInstance.getString(R.string.text_error_not_api_key)))
        } else {
            if (date.isNullOrEmpty()) {
                retrofitImpl
                    .getRetrofitImpl()
                    .getPictureOfTheDay(apiKey)
                    .enqueue(object : Callback<MainPictureServerResponseData> {
                        override fun onResponse(
                            call: Call<MainPictureServerResponseData>,
                            response: Response<MainPictureServerResponseData>,
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                liveDataToObserve.value = AppState.Success(
                                    response.body()!!
                                )
                            } else {
                                val message = response.message()
                                if (message.isNullOrEmpty()) {
                                    liveDataToObserve.value = AppState.Error(
                                        Throwable(App.appInstance.getString(R.string.text_error))
                                    )
                                } else {
                                    liveDataToObserve.value = AppState.Error(
                                        Throwable(message)
                                    )
                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<MainPictureServerResponseData>,
                            t: Throwable,
                        ) {
                            liveDataToObserve.value = AppState.Error(t)
                        }
                    })
            } else {
                retrofitImpl
                    .getRetrofitImpl()
                    .getPictureOfTheDay(date, apiKey)
                    .enqueue(object : Callback<MainPictureServerResponseData> {

                        override fun onResponse(
                            call: Call<MainPictureServerResponseData>,
                            response: Response<MainPictureServerResponseData>,
                        ) {
                            if (response.isSuccessful && response.body() != null) {
                                liveDataToObserve.value = AppState.Success(
                                    response.body()!!
                                )
                            } else {
                                val message = response.message()
                                if (message.isNullOrEmpty()) {
                                    liveDataToObserve.value = AppState.Error(
                                        Throwable(App.appInstance.getString(R.string.text_error))
                                    )
                                } else {
                                    liveDataToObserve.value = AppState.Error(
                                        Throwable(message)
                                    )
                                }
                            }
                        }

                        override fun onFailure(
                            call: Call<MainPictureServerResponseData>,
                            t: Throwable,
                        ) {
                            liveDataToObserve.value = AppState.Error(t)
                        }
                    })
            }
        }
    }
}