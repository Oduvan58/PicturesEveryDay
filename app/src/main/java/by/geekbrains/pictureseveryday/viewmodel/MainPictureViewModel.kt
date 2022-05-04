package by.geekbrains.pictureseveryday.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import by.geekbrains.pictureseveryday.BuildConfig
import by.geekbrains.pictureseveryday.domain.MainPictureServerResponseData
import by.geekbrains.pictureseveryday.repository.MainPictureRetrofitImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPictureViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val retrofitImpl: MainPictureRetrofitImpl = MainPictureRetrofitImpl(),
) : ViewModel() {

    fun getLiveData(): LiveData<AppState> {
        sendServerRequest()
        return liveDataToObserve
    }

    private fun sendServerRequest() {
        liveDataToObserve.value = AppState.Loading
        val apiKey: String = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            AppState.Error(Throwable("You need API key"))
        } else {
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
                                    Throwable("Unidentified error")
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