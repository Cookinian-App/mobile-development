package com.bangkitcapstone.cookinian.helper

fun capitalizeWords(text: String): String {
    return text.split(" ").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}
