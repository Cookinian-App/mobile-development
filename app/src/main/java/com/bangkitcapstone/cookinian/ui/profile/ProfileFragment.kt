package com.bangkitcapstone.cookinian.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.preference.UserPreference
import com.bangkitcapstone.cookinian.databinding.FragmentProfileBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.helper.showAlert
import com.bangkitcapstone.cookinian.ui.login.LoginActivity
import com.bangkitcapstone.cookinian.ui.profile_edit.ProfileEditActivity
import com.bangkitcapstone.cookinian.ui.profile_edit_pass.ProfileEditPassActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    private val profileViewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }

    private var selectedCheckedItem: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileData()
        observeThemeMode()

        binding.llProflieEdit.setOnClickListener { goToProfileEdit() }
        binding.llProfileEditPass.setOnClickListener { goToProfileEditPass() }
        binding.llProfileContact.setOnClickListener { contactUs() }
        binding.llProfileMode.setOnClickListener { showThemeModeDialog() }
        binding.llProfileLogout.setOnClickListener { logout() }
    }

    private fun goToProfileEdit() {
        startActivity(Intent(requireContext(), ProfileEditActivity::class.java))
    }

    private fun goToProfileEditPass() {
        startActivity(Intent(requireContext(), ProfileEditPassActivity::class.java))
    }

    private fun observeThemeMode() {
        profileViewModel.getThemeMode().observe(viewLifecycleOwner) { themeMode ->
            when (themeMode) {
                UserPreference.SYSTEM_DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                UserPreference.LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                UserPreference.DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            selectedCheckedItem = when (themeMode) {
                UserPreference.SYSTEM_DEFAULT -> 0
                UserPreference.LIGHT_MODE -> 1
                UserPreference.DARK_MODE -> 2
                else -> -1
            }
        }
    }

    private fun setProfileData() {
        profileViewModel.getSession().observe(viewLifecycleOwner) { user ->
            binding.tvProfileName.text = user.name
            binding.tvProfileEmail.text = user.email
            Glide.with(requireContext())
                .load(user.avatarUrl)
                .into(binding.ivProfilePicture)
        }
    }

    private fun contactUs() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(getString(R.string.cookinian_email))
        }
        try {
            startActivity(emailIntent)
        } catch (e: Exception) {
            showAlert(requireContext(), getString(R.string.error_title),
                getString(R.string.error_no_email))
        }
    }

    private fun showThemeModeDialog() {
        val options = arrayOf("Default Sistem", "Terang", "Gelap")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.select_theme))
            .setSingleChoiceItems(options, selectedCheckedItem) { _, which ->
                selectedCheckedItem = which
            }
            .setPositiveButton(getString(R.string.dialog_positive_button)) { _, _ ->
                if (selectedCheckedItem != -1) {
                    val selectedThemeMode = when (selectedCheckedItem) {
                        0 -> UserPreference.SYSTEM_DEFAULT
                        1 -> UserPreference.LIGHT_MODE
                        2 -> UserPreference.DARK_MODE
                        else -> UserPreference.SYSTEM_DEFAULT
                    }
                    profileViewModel.saveThemeMode(selectedThemeMode)
                }
            }
            .setNegativeButton(getString(R.string.dialog_negative_button)) { _, _ -> }
            .show()
    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.logout_confirmation))
            .setPositiveButton(R.string.dialog_positive_button) { _, _ ->
                profileViewModel.logout()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.dialog_negative_button)) { _, _ -> }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}