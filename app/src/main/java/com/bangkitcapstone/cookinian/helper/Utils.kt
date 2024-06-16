package com.bangkitcapstone.cookinian.helper

import android.content.Context
import android.widget.Toast
import androidx.core.text.HtmlCompat
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

fun formatDescription(description: String): String {
    var cleanedDescription = description

    // Menambahkan spasi setelah . ? ! : jika tidak diikuti spasi
    cleanedDescription = cleanedDescription.replace(Regex("([.?!:]+)([^\\s])"), "$1 $2")

    // Menambahkan newline setelah : jika diikuti spasi dan karakter
    cleanedDescription = cleanedDescription.replace(Regex(":\\s*(\\S)"), ":\n\n$1")

    // Menambahkan newline setelah huruf kecil yang diikuti huruf besar
    cleanedDescription = cleanedDescription.replace(Regex("(\\p{Lower})(\\p{Upper})"), "$1\n\n$2")

    // Menambahkan newline setelah . jika diikuti spasi dan angka
    cleanedDescription = cleanedDescription.replace(Regex("\\.\\s+(?=\\d\\.)"), ".\n\n")

    // Menambahkan newline setelah huruf dan titik diikuti dengan spasi dan huruf besar
    cleanedDescription = cleanedDescription.replace(Regex("(\\p{Lower})\\.\\s+(\\p{Upper})"), "$1.\n\n$2")

    return cleanedDescription
}



fun getHtml(htmlBody: String): CharSequence {
    val rawHtml: CharSequence = HtmlCompat.fromHtml(htmlBody, HtmlCompat.FROM_HTML_MODE_LEGACY)
    val formattedHtml = formatDescription(rawHtml.toString())
    return formattedHtml
}

fun formatRecipeTimes(times: String): String {
    return times.replace("jam", " Jam")
        .replace("mnt", " Mnt")
        .replace("j", " J")
}

fun showAlert(context: Context, title: String, message: String) {
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.dialog_positive_button) { _, _ -> }
        .show()
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun <T> handleHttpException(e: HttpException, liveData: MutableLiveData<Result<T>>) {
    val error = e.response()?.errorBody()?.string()
    val message = Gson().fromJson(error, RegisterResponse::class.java)
    liveData.value = Result.Error(message.message)
}