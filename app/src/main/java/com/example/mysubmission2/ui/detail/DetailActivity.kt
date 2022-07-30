package com.example.mysubmission2.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.mysubmission2.R
import com.example.mysubmission2.data.response.ListStoryItem
import com.example.mysubmission2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var detailStories: ListStoryItem
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.detail_title)
        setDetail()

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setDetail() {
        intent.apply {
            detailStories = intent.getParcelableExtra<ListStoryItem>("stories") as ListStoryItem

            binding.apply {
                tvItemName.text = detailStories.name
                tvItemDescription.text = detailStories.createdAt
                Glide.with(this@DetailActivity)
                    .load(detailStories.photoUrl)
                    .into(imItemPhoto)
            }
        }
    }
}