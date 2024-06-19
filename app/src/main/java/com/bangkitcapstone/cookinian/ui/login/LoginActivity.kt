package com.bangkitcapstone.cookinian.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.databinding.ActivityLoginBinding
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showAlert
import com.bangkitcapstone.cookinian.helper.showToast
import com.bangkitcapstone.cookinian.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()

        binding.btnLogin.setOnClickListener { handleLogin() }
    }

    private fun handleLogin() {
        binding.apply {
            if(edLoginEmail.text.toString().isNotEmpty() && edLoginPassword.text.toString().isNotEmpty()) {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()
                loginViewModel.login(email, password)
            } else {
                showAlert(this@LoginActivity, getString(R.string.login), getString(R.string.error_empty_input))
            }
        }

        loginViewModel.result.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.pbLogin.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.pbLogin.visibility = View.GONE
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    is Result.Error -> {
                        binding.pbLogin.visibility = View.GONE
                        showAlert(this, "Terjadi Kesalahan", result.error)
                    }
                }
            }
        }
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(400)
        val message = ObjectAnimator.ofFloat(binding.tvLoginMessage, View.ALPHA, 1f).setDuration(400)
        val email = ObjectAnimator.ofFloat(binding.lLoginEmail, View.ALPHA, 1f).setDuration(400)
        val password = ObjectAnimator.ofFloat(binding.lLoginPassword, View.ALPHA, 1f).setDuration(400)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(400)

        AnimatorSet().apply {
            playSequentially(title, message, email, password, login)
            startDelay = 400
            start()
        }
    }
}