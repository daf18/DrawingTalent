package com.example.drawingtalent

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs){

        private var mDrawPath : CustomPath? = null
        private var mCanvasBitmap : Bitmap? = null
        private var mDrawPaint : Paint? = null
        private var mCanvasPaint : Paint? = null
        private var mBrushSize : Float = 0.toFloat()
        private var color = Color.BLACK
        private var canvas : Canvas? = null


        init{
            setUpDrawing()
        }

    private fun setUpDrawing(){
        mDrawPaint = Paint()
        //color, mBrush variables not empty- we can use them
        mDrawPath = CustomPath(color, mBrushSize)
        //defined above so we know it is not empty
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }
    //method from View class, called during layout when the size of the view has changed
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        //use this bitmap as the canvas
        canvas = Canvas(mCanvasBitmap!!)
    }
    //what should happen when we want to Draw
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //setting our canvas to draw on on the left corner
        canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)

        if(!mDrawPath!!.isEmpty){
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
            mDrawPaint!!.color = mDrawPath!!.color
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
        }
    }

    //what should happen when we touch it
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        //where was it touched x,y
        val touchX = event?.x
        val touchY = event?.y

        //actions of the motion event
        when(event?.action ){
            //what should happen when we press on the screen
            MotionEvent.ACTION_DOWN -> {
                mDrawPath!!.color =color
                mDrawPath!!.brushThickness = mBrushSize

                mDrawPath!!.reset()
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.moveTo(touchX , touchY)
                    }
                }
            }
            //what should happen when we drag over the screen
            MotionEvent.ACTION_MOVE -> {
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.lineTo(touchX, touchY)
                    }
                }
            }
            //what should happen when we release the screen
            MotionEvent.ACTION_UP -> {
                mDrawPath = CustomPath(color, mBrushSize)
            }

            //for the other events we take no action
            else -> return false
        }
        //invalidate the whole view is the view is visible
        invalidate()
        return true
    }

    internal inner class CustomPath(var color : Int,
    var brushThickness : Float) : Path() {

    }


}