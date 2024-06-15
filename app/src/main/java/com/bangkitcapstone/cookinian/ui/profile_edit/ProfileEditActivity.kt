package com.bangkitcapstone.cookinian.ui.profile_edit

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.databinding.ActivityProfileEditBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showAlert
import com.bumptech.glide.Glide

class ProfileEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var email: String

    private val profileEditViewModel by viewModels<ProfileEditViewModel>() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        profileEditViewModel.getSession().observe(this) { user ->
            email = user.email
            binding.edEditName.setText(user.name)
            Glide.with(this).load(user.avatarUrl).into(binding.ivEditProfilePicture)
        }

        setupToolbar()

        binding.btnEditProfile.setOnClickListener { handleEditProfile() }
    }

    private fun handleEditProfile() {
        binding.apply {
            if(edEditName.text.toString().isNotEmpty()) {
                val name = edEditName.text.toString()
                profileEditViewModel.editProfile(email, name)
            } else {
                showAlert(this@ProfileEditActivity, "Gagal", "Nama profil tidak boleh kosong!")
            }
        }

        profileEditViewModel.result.observe(this) { event ->
            event.getContentIfNotHandled()?.let { result ->
                when(result) {
                    is Result.Loading -> {
                        binding.pbEditProfile.visibility = View.VISIBLE
                        binding.btnEditProfile.isEnabled = false
                    }
                    is Result.Success -> {
                        binding.pbEditProfile.visibility = View.GONE
                        binding.btnEditProfile.isEnabled = true
                        showAlert(this, "Berhasil", "Nama profil anda telah diubah!")
                    }
                    is Result.Error -> {
                        binding.pbEditProfile.visibility = View.GONE
                        binding.btnEditProfile.isEnabled = true
                        showAlert(this, "Terjadi kesalahan", result.error)
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_back)
            title = "Edit Profil"
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