package com.offhome.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.offhome.app.databinding.ActivityMainBinding

/**
 * Main class
 */
class MainActivity : AppCompatActivity() {
    /**
     * oncreate App
     */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewHelloWorld.text = "OFF Home"
    }
}