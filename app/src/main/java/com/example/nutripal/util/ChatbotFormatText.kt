package com.example.nutripal.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight

fun chatbotFormatText(text: String): AnnotatedString {
    val annotatedStringBuilder = AnnotatedString.Builder()
    val pattern = "(\\*\\*.*?\\*\\*)".toRegex()

    var lastIndex = 0
    val matches = pattern.findAll(text)

    for (match in matches) {
        annotatedStringBuilder.append(text.substring(lastIndex, match.range.first))

        annotatedStringBuilder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
        annotatedStringBuilder.append(match.value.removePrefix("**").removeSuffix("**"))
        annotatedStringBuilder.pop()

        lastIndex = match.range.last + 1
    }

    annotatedStringBuilder.append(text.substring(lastIndex))

    return annotatedStringBuilder.toAnnotatedString()
}
