package by.geekbrains.pictureseveryday.domain

import androidx.fragment.app.Fragment

class FragmentsFactory(
    val title: String,
    val factoryMethod: () -> Fragment,
)