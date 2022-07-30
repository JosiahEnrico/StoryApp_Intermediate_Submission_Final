package com.example.mysubmission2.ui.main

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mysubmission2.R
import com.example.mysubmission2.databinding.ActivityMainBinding
import com.example.mysubmission2.paging.adapter.StoryListAdapter
import com.example.mysubmission2.paging.adapter.LoadingStateAdapter
import com.example.mysubmission2.ui.newStory.NewStoryActivity
import com.example.mysubmission2.ui.login.LoginActivity
import com.example.mysubmission2.ui.maps.MapsActivity
import com.example.mysubmission2.ui.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainVIewModel>()
    private lateinit var binding: ActivityMainBinding
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()
    private lateinit var adapter: StoryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.home_title)

        adapter = StoryListAdapter()

        setupViewModel()
        setRecyclerView()
        action()
    }

    private fun action() {
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, NewStoryActivity::class.java)
            startActivity(intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle()
            )
        }
    }

    private fun setupViewModel() {
        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
        }

        viewModel.loading.observe(this) { showLoading(it) }
    }

    private fun setRecyclerView() {
        binding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                rvStories.layoutManager = GridLayoutManager(this@MainActivity, 2)
            else
                rvStories.layoutManager = GridLayoutManager(this@MainActivity, 1)

            rvStories.setHasFixedSize(true)
            rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_option, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.menu_logout -> {
                dataStoreViewModel.logout()
                Log.d("tag", "token deleted")
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            R.id.language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }
}