package com.example.mysubmission2.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mysubmission2.R
import com.example.mysubmission2.ui.login.LoginActivity
import com.example.mysubmission2.ui.main.MainActivity
import com.example.mysubmission2.ui.DataStoreViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()

        lifecycleScope.launch {
            delay(2500)
            setupViewModel()
        }
    }

    override fun onPause() {
        lifecycleScope.cancel()
        super.onPause()
    }

    private fun setupViewModel(){
        dataStoreViewModel.getSession().observe(this) { userSession ->
            if (!userSession.isLogin) {
                Log.d("tag", userSession.token)
                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
                finish()
            }
            else {
                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                finish()
            }


        }
    }
}