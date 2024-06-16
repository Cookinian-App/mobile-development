package com.bangkitcapstone.cookinian.ui.profile_edit_pass

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.databinding.ActivityProfileEditPassBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showAlert

class ProfileEditPassActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileEditPassBinding
    private lateinit var email: String

    private val profileEditViewModel by viewModels<ProfileEditPassViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileEditViewModel.getSession().observe(this) { user ->
            email = user.email
        }

        setupToolbar()

        binding.btnEditPass.setOnClickListener { changePassword() }
    }

    private fun changePassword() {
        binding.apply {
            if(edEditPassOld.text.toString().isNotEmpty() && edEditPassNew.text.toString().isNotEmpty() && edEditPassConfirm.text.toString().isNotEmpty()) {
                val currentPassword = edEditPassOld.text.toString()
                val newPassword = edEditPassNew.text.toString()
                val confirmNewPassword = edEditPassConfirm.text.toString()
                if (newPassword == confirmNewPassword) {
                    profileEditViewModel.changePassword(email, currentPassword, newPassword, confirmNewPassword)
                } else {
                    showAlert(this@ProfileEditPassActivity,
                        getString(R.string.change_password), getString(R.string.old_password_diff))
                }
            } else {
                showAlert(this@ProfileEditPassActivity, getString(R.string.change_password),
                    getString(
                        R.string.error_empty_input
                    ))
            }
        }

        profileEditViewModel.result.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when(result) {
                    is Result.Loading -> {
                        binding.pbEditPass.visibility = View.VISIBLE
                        binding.btnEditPass.isEnabled = false
                    }
                    is Result.Success -> {
                        binding.pbEditPass.visibility = View.GONE
                        binding.btnEditPass.isEnabled = true
                        resetForm()
                        showAlert(this, getString(R.string.change_password),
                            getString(R.string.change_password_success))
                    }
                    is Result.Error -> {
                        binding.pbEditPass.visibility = View.GONE
                        binding.btnEditPass.isEnabled = true
                        showAlert(this, getString(R.string.change_password), result.error)
                    }
                }
            }
        }
    }

    private fun resetForm() {
        binding.apply {
            edEditPassOld.text?.clear()
            edEditPassNew.text?.clear()
            edEditPassConfirm.text?.clear()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            title = getString(R.string.change_password)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}