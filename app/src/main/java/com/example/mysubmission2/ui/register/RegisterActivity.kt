package com.example.mysubmission2.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import com.example.mysubmission2.R
import com.example.mysubmission2.databinding.ActivityRegisterBinding
import com.example.mysubmission2.ui.login.LoginActivity
import com.example.mysubmission2.customView.EmailEditText
import com.example.mysubmission2.customView.NameEditText
import com.example.mysubmission2.customView.PasswordEditText
import com.example.mysubmission2.customView.RegisterButton
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    private lateinit var registerButton: RegisterButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private lateinit var nameEditText: NameEditText

    private var correctEmail: Boolean = false
    private var correctPassword: Boolean = false
    private var correctName: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        registerButton = binding.signupButton
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        nameEditText = binding.nameEditText

        setupViewModel()
        action()
        playAnimation()
    }

    private fun action() {
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctName =
                    !s.isNullOrEmpty() && s.length > 1
                setRegisterButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctEmail =
                    !s.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()
                setRegisterButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctPassword = !s.isNullOrEmpty() && s.length > 5
                setRegisterButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.signupButton.setOnClickListener {
            register()
        }

        binding.tvAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity as Activity)
                    .toBundle()
            )
        }
    }

    private fun setRegisterButtonEnable() {
        registerButton.isEnabled = correctName && correctEmail && correctPassword
    }

    private fun setupViewModel() {
        viewModel.loading.observe(this) { showLoading(it) }
    }

    private fun register() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        viewModel.registerUser(name, email, password)

        Toast.makeText(
            this,
            getString(R.string.please_login),
            Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this, LoginActivity::class.java)

        startActivity(intent,
            ActivityOptionsCompat.makeSceneTransitionAnimation(this@RegisterActivity as Activity)
                .toBundle()
        )
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(500)
        val haveAccount = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, 1F).setDuration(500)


        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                nameEditTextLayout,
                emailEditTextLayout,
                passwordEditTextLayout,
                signup,
                haveAccount
            )
            startDelay = 500
        }.start()
    }
}