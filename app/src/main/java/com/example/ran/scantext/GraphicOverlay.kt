package com.example.ran.scantext

import android.content.Context
import android.graphics.Canvas
import android.hardware.camera2.CameraCharacteristics
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import android.view.View
import java.util.HashSet

class GraphicOverlay(context : Context, var attrs : AttributeSet) : View(context, attrs) {

    private val lock = Any()
    private var previewWidth: Int = 0
    private var widthScaleFactor = 1.0f
    private var previewHeight: Int = 0
    private var heightScaleFactor = 1.0f
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private var facing = CameraCharacteristics.LENS_FACING_BACK
    private val graphics = HashSet<Graphic>()

    companion object {

        abstract class Graphic(var graphicOverlay: GraphicOverlay) {

            abstract fun draw(canvas: Canvas)
            fun scaleX(horizontal : Float) : Float =
                    horizontal * graphicOverlay.widthScaleFactor
            fun scaleY(vertical : Float) : Float =
                    vertical * graphicOverlay.heightScaleFactor

            fun getApplicationContext() =
                    graphicOverlay.context.applicationContext

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            fun translateX(x : Float) : Float = when {
                graphicOverlay.facing == CameraCharacteristics.LENS_FACING_FRONT -> graphicOverlay.width - scaleX(x)
                else -> scaleX(x)
            }

            fun translateY(y : Float) : Float = scaleY(y)

            fun postInvalidate() {
                graphicOverlay.postInvalidate()
            }
        }
    }

    fun clear(){
        synchronized(lock) {
            graphics.clear()
        }
        postInvalidate()
    }

    fun add(graphic : Graphic) {
        synchronized(lock){
            graphics.add(graphic)
        }
        postInvalidate()
    }

    fun remove(graphic : Graphic) {
        synchronized(lock){
            graphics.remove(graphic)

        }
        postInvalidate()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setCameraInfo(previewWidth: Int, previewHeight: Int, facing: Int) {
        synchronized(lock) {
            this.previewWidth = previewWidth
            this.previewHeight = previewHeight
            this.facing = facing
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        synchronized(lock){
            when {
                previewWidth != 0 && previewHeight != 0 -> {
                    widthScaleFactor = canvas!!.width.toFloat()/previewWidth.toFloat()
                    heightScaleFactor = canvas.height.toFloat()/previewHeight.toFloat()
                }
            }

            for(graphic in graphics){
                graphic.draw(canvas!!)
            }
        }
    }

}