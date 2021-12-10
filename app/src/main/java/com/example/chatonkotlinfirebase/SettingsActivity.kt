package com.example.chatonkotlinfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatonkotlinfirebase.databinding.ActivityMainBinding
import com.example.chatonkotlinfirebase.databinding.ActivitySettingsBinding
import com.example.chatonkotlinfirebase.databinding.ActivitySignInBinding

class SettingsActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setUpActionBar()
        setContentView(binding.root)
    }


    //отвечает за кнопку возврата в экшн баре
    private fun setUpActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Settings"
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}