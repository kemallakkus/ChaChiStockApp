package com.kemalakkus.chachistockapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.kemalakkus.chachistockapp.databinding.ActivityMainBinding
import com.kemalakkus.chachistockapp.viewmodels.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val productViewModel: ProductViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_ChaChiStockApp)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}