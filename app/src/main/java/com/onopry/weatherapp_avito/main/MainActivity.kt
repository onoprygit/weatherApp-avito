package com.onopry.weatherapp_avito.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onopry.weatherapp_avito.R
import com.onopry.weatherapp_avito.presentation.screens.HomeFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        if (supportActionBar != null) {
            supportActionBar?.hide()
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }
}