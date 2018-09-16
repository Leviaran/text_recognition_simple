package com.example.ran.scantext

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import com.example.ran.scantext.GraphicOverlay.Companion.Graphic
import com.google.firebase.ml.vision.text.FirebaseVisionText

class TextGraphic( graphicOverlay: GraphicOverlay, var element : FirebaseVisionText.Element?) : Graphic(graphicOverlay) {

    private val TAG = "TextGraphic"
    private val TEXT_COLOR = Color.RED
    private val TEXT_SIZE = 54.0f
    private val STROKE_WIDTH = 4.0f

    private lateinit var rectPaint: Paint
    private lateinit var textPaint: Paint

    init {
        rectPaint = Paint()
        rectPaint.color = TEXT_COLOR
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = STROKE_WIDTH

        textPaint = Paint()
        textPaint.color = TEXT_COLOR
        textPaint.textSize = TEXT_SIZE
        postInvalidate()
    }

    override fun draw(canvas: Canvas) {

        Log.e("TAG", "on draw graphic text")
        element?: throw IllegalStateException("Null Text")

        val rect = RectF(element?.boundingBox)
        canvas.drawRect(rect, rectPaint)

        canvas.drawText(element?.text, rect.left, rect.bottom, textPaint)

    }


}