package com.example.mysubmission2.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import com.example.mysubmission2.R
import com.example.mysubmission2.data.response.Session
import com.example.mysubmission2.ui.main.MainActivity
import com.example.mysubmission2.ui.register.RegisterActivity
import com.example.mysubmission2.databinding.ActivityLoginBinding
import com.example.mysubmission2.ui.DataStoreViewModel
import com.example.mysubmission2.customView.EmailEditText
import com.example.mysubmission2.customView.LoginButton
import com.example.mysubmission2.customView.PasswordEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModelViewModel by viewModels<LoginViewModel>()
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    private lateinit var loginButton: LoginButton
    private lateinit var emailEditText: EmailEditText
    private lateinit var passwordEditText: PasswordEditText
    private var correctEmail: Boolean = false
    private var correctPassword: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        loginButton = binding.loginButton
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        setupViewModel()
        action()
        playAnimation()
    }

    private fun setupViewModel() {
        viewModelViewModel.loading.observe(this) { showLoading(it) }
        viewModelViewModel.errorMessage.observe(this) { toast(it) }
    }

    private fun action() {

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctEmail =
                    !s.isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(s).matches()
                setLoginButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctPassword = !s.isNullOrEmpty() && s.length > 5
                setLoginButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.loginButton.setOnClickListener {
            login()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        binding.tvAccount.setOnClickListener {
            val int = Intent(this, RegisterActivity::class.java)
            startActivity(int,
                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity as Activity).toBundle()
            )
        }
    }

    private fun setLoginButtonEnable() {
        loginButton.isEnabled = correctPassword && correctEmail
    }


    private fun login() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        when {
            email.isEmpty() -> {
                binding.emailEditTextLayout.error = getString(R.string.input_email)
            }
            password.isEmpty() -> {
                binding.passwordEditTextLayout.error = getString(R.string.input_password)
            }
            else -> {
                viewModelViewModel.loginUser(email, password)

                viewModelViewModel.login.observe(this) {
                    binding.progressBar.visibility = View.VISIBLE
                    if (it != null) {
                        AlertDialog.Builder(this).apply {
                            setTitle(R.string.login_success)
                            setMessage("${getString(R.string.welcome)}, ${it.name}!" )
                            setPositiveButton(R.string.ok_continue) { _, _ ->
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create()
                            show()
                        }
                        saveSession(Session(it.name, it.token, it.userId, true))
                    }

                }

            }
        }
    }

    private fun saveSession(user: Session) {
        dataStoreViewModel.setSession(user)
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(500)
        val noAccount = ObjectAnimator.ofFloat(binding.tvAccount, View.ALPHA, 1F).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailEditTextLayout,
                passwordEditTextLayout,
                login,
                noAccount
            )
            startDelay = 500
        }.start()
    }
}