package com.example.exchangesample.presentation

import android.icu.text.DecimalFormat
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class AmountInputVisualTransformation : VisualTransformation {
    private val symbols = DecimalFormat().decimalFormatSymbols

    //assuming all currencies have 2 digits in the fraction part
    //which is wrong, but we don't have actual currency formatting info
    //also base currency for now only EUR
    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text
        // int part of the formatted amount or 0 if empty
        val intPart = inputText
            .dropLast(2)
            .ifEmpty { symbols.zeroDigit.toString() }

        // fraction part of the formatted amount or "00" if empty
        val fractionPart = inputText.takeLast(2).let {
            if (it.length != 2) {
                List(2 - it.length) {
                    symbols.zeroDigit.toString()
                }.joinToString("") + it
            } else {
                it
            }
        }
        val formattedNumber =
            buildString { append(intPart, symbols.decimalSeparator, fractionPart) }
        return TransformedText(
            AnnotatedString(
                text = formattedNumber,
                spanStyles = text.spanStyles,
                paragraphStyles = text.paragraphStyles
            ), FixedCursorOffsetMapping(
                contentLength = inputText.length,
                formattedContentLength = formattedNumber.length
            )
        )
    }

    //With this implementation cursor will be stuck at the end of input
    //That will allow user to input amount without using a decimal separator
    private class FixedCursorOffsetMapping(
        private val contentLength: Int,
        private val formattedContentLength: Int,
    ) : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int = formattedContentLength
        override fun transformedToOriginal(offset: Int): Int = contentLength
    }
}