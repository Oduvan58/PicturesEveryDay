package by.geekbrains.pictureseveryday.view.details

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.geekbrains.pictureseveryday.R
import by.geekbrains.pictureseveryday.databinding.FragmentSettingsBinding

const val SETTINGS_SHARED_PREFERENCES = "SETTINGS_SHARED_PREFERENCES"
const val THEME_RES_ID = "THEME_RES_ID"
private const val THEME_NAME_SHARED_PREFERENCES = "THEME_NAME_SHARED_PREFERENCES"
const val APP_THEME = 0
const val MY_BLUE_THEME = 1
const val MY_RED_THEME = 2

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var currentTheme: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        setSharedPreferencesSettings()
        binding.appTheme.setOnClickListener {
            if (currentTheme != APP_THEME) {
                requireActivity().apply {
                    setTheme(R.style.AppTheme)
                    recreate()
                    saveThemeSettings(APP_THEME, R.style.AppTheme)
                }
            }
        }
        binding.blueTheme.setOnClickListener {
            if (currentTheme != MY_BLUE_THEME) {
                requireActivity().apply {
                    setTheme(R.style.MyBlueTheme)
                    recreate()
                    saveThemeSettings(MY_BLUE_THEME, R.style.MyBlueTheme)
                }
            }
        }
        binding.redTheme.setOnClickListener {
            if (currentTheme != MY_RED_THEME) {
                requireActivity().apply {
                    setTheme(R.style.MyRedTheme)
                    recreate()
                    saveThemeSettings(MY_RED_THEME, R.style.MyRedTheme)
                }
            }
        }
    }

    private fun setSharedPreferencesSettings() {
        activity?.let {
            currentTheme =
                it.getSharedPreferences(SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE)
                    .getInt(THEME_NAME_SHARED_PREFERENCES, APP_THEME)
            when (currentTheme) {
                MY_BLUE_THEME -> {
                    binding.blueTheme.isChecked = true
                }
                MY_RED_THEME -> {
                    binding.redTheme.isChecked = true
                }
                else -> {
                    binding.appTheme.isChecked = true
                }
            }
        }
    }

    private fun saveThemeSettings(currentTheme: Int, style: Int) {
        this.currentTheme = currentTheme
        activity?.let {
            with(it.getSharedPreferences(SETTINGS_SHARED_PREFERENCES, MODE_PRIVATE).edit()) {
                putInt(THEME_NAME_SHARED_PREFERENCES, currentTheme).apply()
                putInt(THEME_RES_ID, style).apply()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}