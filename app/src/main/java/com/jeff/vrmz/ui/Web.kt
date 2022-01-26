package com.jeff.vrmz.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jeff.vrmz.R
import com.jeff.vrmz.databinding.ActivityWebBinding

class Web : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWebBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}