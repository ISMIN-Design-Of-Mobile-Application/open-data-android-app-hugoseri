package com.ismin.opendataapp

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import java.util.*

fun textAsBitmap(text: String): Bitmap {
    val paint = Paint(ANTI_ALIAS_FLAG)
    paint.setTextSize(45.toFloat())
    paint.setColor(Color.WHITE)
    paint.setTextAlign(Paint.Align.LEFT)
    val baseline = -paint.ascent() // ascent() is negative
    val width = (paint.measureText(text) + 0.0f).toInt()// round
    val height = (baseline + paint.descent() + 0.0f).toInt()
    val image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(image)
    canvas.drawText(text, 0.toFloat(), baseline, paint)
    return image
}

fun onlyFirstLetterUpperCase(text: String): String {

    var finalText = text.toLowerCase(Locale.FRANCE)

    fun String.capitalizeWordsWithSpaces(): String = split(" ").map{it.capitalize()}.joinToString(" ")
    fun String.capitalizeWordsWithDashes(): String = split("-").map{it.capitalize()}.joinToString("-")

    finalText = finalText.capitalizeWordsWithDashes()
    finalText = finalText.capitalizeWordsWithSpaces()

    return finalText
}

fun httpToHttps(text: String): String {

    var textWithoutHttp = text.substring(4)
    var httpText = text.split(":")[0]
    httpText += "s"

    return httpText+textWithoutHttp
}