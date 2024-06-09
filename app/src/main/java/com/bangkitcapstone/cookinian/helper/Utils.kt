package com.bangkitcapstone.cookinian.helper

fun capitalizeWords(text: String): String {
    return text.split(Regex("[\\s-]+")).joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
}