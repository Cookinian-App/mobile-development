package com.bangkitcapstone.cookinian.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.preference.UserPreference
import com.bangkitcapstone.cookinian.databinding.FragmentProfileBinding
import com.bangkitcapstone.cookinian.helper.ViewModelFactory
import com.bangkitcapstone.cookinian.ui.login.LoginActivity
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

        binding.llProfileMode.setOnClickListener { showThemeModeDialog() }
        binding.llProfileLogout.setOnClickListener { logout() }
        binding.llProfileContact.setOnClickListener { contactUs() }
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
        }
    }

    private fun contactUs() {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:cookinian.app@gmail.com")
        }
        try {
            startActivity(emailIntent)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Tidak ada aplikasi email yang terpasang.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showThemeModeDialog() {
        val options = arrayOf("Default Sistem", "Terang", "Gelap")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Pilih Tema")
            .setSingleChoiceItems(options, selectedCheckedItem) { _, which ->
                selectedCheckedItem = which
            }
            .setPositiveButton("Simpan") { _, _ ->
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
            .setNegativeButton("Batal") { _, _ -> }
            .show()
    }

    private fun logout() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton(R.string.dialog_positive_button) { _, _ ->
                profileViewModel.logout()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            .setNegativeButton("Batal") { _, _ -> }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}