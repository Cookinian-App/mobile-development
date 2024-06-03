package com.bangkitcapstone.cookinian.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.databinding.ActivityRegisterBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val registerViewModel by viewModels<RegisterViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        binding.btnRegister.setOnClickListener { handleRegister() }
    }

    private fun handleRegister() {
        binding.apply {
            if (edRegisterName.text.toString().isNotEmpty() && edRegisterEmail.text.toString()
                    .isNotEmpty() && edRegisterPassword.text.toString().isNotEmpty()
            ) {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()
                registerViewModel.register(name, email, password)
            } else {
                Toast.makeText(this@RegisterActivity, "Semua data harus diisi", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        registerViewModel.result.observe(this@RegisterActivity) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.pbRegister.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.pbRegister.visibility = View.GONE
                        showToast(result.data.message)
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                    is Result.Error -> {
                        binding.pbRegister.visibility = View.GONE
                        showAlert(result.error)
                    }
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showAlert(message: String) {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(R.string.register)
            setMessage(message)
            setPositiveButton(R.string.dialog_positive_button) { _, _ -> }
                .show()
        }
    }

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.tvRegisterTitle, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.tvRegisterMessage, View.ALPHA, 1f).setDuration(500)
        val name = ObjectAnimator.ofFloat(binding.lRegisterName, View.ALPHA, 1f).setDuration(500)
        val email = ObjectAnimator.ofFloat(binding.lRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val password = ObjectAnimator.ofFloat(binding.lRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(title, message, name, email, password, login)
            startDelay = 500
            start()
        }
    }

}