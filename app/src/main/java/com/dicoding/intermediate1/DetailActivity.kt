package com.dicoding.intermediate1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.intermediate1.databinding.ActivityDetailBinding
import android.content.Intent
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvProfileName.text = intent.getData(Constanst.DetailStory.UserName.name, "Name")
        Glide.with(binding.root)
            .load(intent.getData(Constanst.DetailStory.ImageURL.name, ""))
            .into(binding.imgStory)
        binding.tvDescription.text =
            intent.getData(Constanst.DetailStory.Description.name, "Description")
    }


    private fun Intent.getData(key: String, defaultValue: String = "None"): String {
        return getStringExtra(key) ?: defaultValue
    }
}