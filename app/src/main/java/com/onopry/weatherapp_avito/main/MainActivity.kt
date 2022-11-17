package com.onopry.weatherapp_avito.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.onopry.weatherapp_avito.R
import com.onopry.weatherapp_avito.databinding.ActivityMainBinding
import com.onopry.weatherapp_avito.presentation.screens.HomeFragment
import com.onopry.weatherapp_avito.presentation.screens.HomeViewModel
import com.onopry.weatherapp_avito.presentation.uistate.LocalityState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.localityState.collect{ state ->
                    when(state){
                        is LocalityState.City -> {
                            binding.searchView.queryHint = state.locality.name
                        }
                    }
                }
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()


    }
}