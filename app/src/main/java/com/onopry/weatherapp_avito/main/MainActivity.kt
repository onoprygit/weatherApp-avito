package com.onopry.weatherapp_avito.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onopry.weatherapp_avito.R
import com.onopry.weatherapp_avito.presentation.screens.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()


    }
}