package com.example.kidsdrawingapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.util.AttributeSet
import android.view.MotionEvent

import kotlin.io.path.moveTo
import android.graphics.Path

class DrawingView(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var mDrawPath : CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null

    init{
        setUpDrawing()
    }

    private fun setUpDrawing(){
            mDrawPaint = Paint()
            mDrawPath = CustomPath(color, mBrushSize)
            mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }

    //setting up our canvas
    override fun onSizeChanged(w: Int, h: Int, wprev: Int, hprev: Int) {
        super.onSizeChanged(w, h, wprev, hprev)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    // what should happen when we want to draw
    //Change Canvas to Canvas? if fails
    override fun onDraw(canvas: Canvas){
        super.onDraw(canvas)


        mCanvasBitmap?.let{
            canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
        }


        // only if its not empty draw something
        //HOW THICK THE PAINT IS
        if(!mDrawPath!!.isEmpty()){
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

//        return super.onTouchEvent(event)

        when(event?.action){
            //HOW THICK THE PATH IS
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color = color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.moveTo(touchX, touchY)
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.lineTo(touchX, touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                mDrawPath = CustomPath(color, mBrushSize)

            }
            else -> return false
        }
        invalidate()
        return true
    }

     internal inner class CustomPath(var color:Int,var brushThickness:Float): android.graphics.Path() // only available to drawing view class




}