package by.geekbrains.pictureseveryday.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.geekbrains.pictureseveryday.R
import by.geekbrains.pictureseveryday.databinding.ActivityMainBinding
import by.geekbrains.pictureseveryday.view.details.SETTINGS_SHARED_PREFERENCES
import by.geekbrains.pictureseveryday.view.details.THEME_RES_ID
import by.geekbrains.pictureseveryday.view.main.MainPictureFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(
            getSharedPreferences(SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE)
                .getInt(THEME_RES_ID, R.style.AppTheme)
        )
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_picture_fragment_container, MainPictureFragment.newInstance())
                .commit()
        }
    }
}