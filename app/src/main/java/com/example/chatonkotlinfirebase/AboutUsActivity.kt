package com.example.chatonkotlinfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatonkotlinfirebase.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpActionBar()
    }

    //отвечает за кнопку возврата в экшн баре
    private fun setUpActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "About us"
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}