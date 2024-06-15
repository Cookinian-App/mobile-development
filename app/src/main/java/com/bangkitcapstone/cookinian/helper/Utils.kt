package com.bangkitcapstone.cookinian.helper

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.bangkitcapstone.cookinian.R
import com.bangkitcapstone.cookinian.data.Result
import com.bangkitcapstone.cookinian.data.api.response.RegisterResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import retrofit2.HttpException

fun capitalizeWords(text: String): String {
    return text.split(Regex("[\\s-]+")).joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}

fun showAlert(context: Context, title: String, message: String) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.dialog_positive_button) { _, _ -> }
        .show()
}

fun <T> handleHttpException(e: HttpException, liveData: MutableLiveData<Result<T>>) {
    val error = e.response()?.errorBody()?.string()
    val message = Gson().fromJson(error, RegisterResponse::class.java)
    liveData.value = Result.Error(message.message)
}