package by.geekbrains.pictureseveryday.viewmodel

import by.geekbrains.pictureseveryday.domain.MainPictureServerResponseData

sealed class AppState {
    data class Success(val serverResponseData: MainPictureServerResponseData) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}