package by.geekbrains.pictureseveryday.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import by.geekbrains.pictureseveryday.R
import by.geekbrains.pictureseveryday.view.details.SETTINGS_SHARED_PREFERENCES
import by.geekbrains.pictureseveryday.view.details.THEME_RES_ID
import by.geekbrains.pictureseveryday.view.main.BOTTOM_SHEET_CONTENT
import by.geekbrains.pictureseveryday.view.main.BOTTOM_SHEET_HEADER
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class CollapsingToolbarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(
            getSharedPreferences(SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE)
                .getInt(THEME_RES_ID, R.style.AppTheme)
        )
        setContentView(R.layout.activity_collapsing_toolbar)
        setSupportActionBar(findViewById(R.id.toolbar))
        findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar_layout).title =
            intent.getStringExtra(BOTTOM_SHEET_HEADER)
        findViewById<TextView>(R.id.collapsing_toolbar_content_text_view).text =
            intent.getStringExtra(BOTTOM_SHEET_CONTENT)
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, getString(R.string.screen_send), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }
}