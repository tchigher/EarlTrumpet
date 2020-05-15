package com.sendi.trumpet.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.os.HandlerThread
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.sendi.trumpet.*
import com.sendi.trumpet.touch.DefaultBoundsStrategy
import com.sendi.trumpet.touch.IBoundsStrategy
import com.sendi.trumpet.touch.TrumpetToucher
import java.lang.Exception

class TrumpetSurfaceView(context: Context, attributeSet: AttributeSet?, defaultStyle: Int)
    : SurfaceView(context,attributeSet,defaultStyle),SurfaceHolder.Callback{

    private val myTag = "TrumpetSurfaceView"


    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null, 0)

    private var mTrumpetPool: TrumpetPool = TrumpetPool()

    private var mDrawHandler: TrumpetHandler? = null

    private var trumpetToucher: TrumpetToucher? = null

    @Volatile
    private var isPause = false

    init {
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSPARENT)
        setZOrderOnTop(true)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.i(myTag,"surfaceChanged wid: $width hei: $height")
        mTrumpetPool.updateWidthAndHeight(width,height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.i(myTag,"surfaceDestroyed")
        mDrawHandler?.removeMessages(TrumpetHandler.UPDATE)
        val message = mDrawHandler?.obtainMessage(TrumpetHandler.QUIT)
        mDrawHandler?.sendMessage(message)
        isPause = true
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.i(myTag,"surfaceCreated")
        start()
        isPause = false
    }

    fun setAdapter(adapter: Adapter<*>){
        mTrumpetPool.setAdapter(adapter)
    }

    fun addTrumpet(trumpet: Trumpet){
        mTrumpetPool.addTrumpet(trumpet)
    }

    fun addTrumpets(trumpets: List<Trumpet>){
        mTrumpetPool.addTrumpets(trumpets)
    }

    fun prepare(){
        checkDrawHandler()
    }

    fun quit(){
        mDrawHandler?.removeMessages(TrumpetHandler.UPDATE)
        val message = mDrawHandler?.obtainMessage(TrumpetHandler.QUIT)
        mDrawHandler?.sendMessage(message)
        mTrumpetPool.quitClear()
        holder.surface.release()
        holder.removeCallback(this)
    }

    fun clear(){
        try {
            val canvas = holder.lockCanvas()
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                holder.unlockCanvasAndPost(canvas)
            }
        }catch (e: Exception){
            Log.i(myTag,"clear throw exception:${e.message}")
        }

    }

    fun config(config: TrumpetConfig){
        //todo:像pool设置配置
        mTrumpetPool.speed = config.speed
    }

    fun setSpeed(speed: Float){
        mTrumpetPool.speed = speed
    }

    private fun start(){
        checkDrawHandler()
        val startMsg = mDrawHandler?.obtainMessage(TrumpetHandler.START)
        mDrawHandler?.sendMessage(startMsg)
    }

    private fun checkDrawHandler(){
        if (mDrawHandler == null){
            val thread = HandlerThread("draw#thread")
            thread.start()
            mDrawHandler = TrumpetHandler(this, thread.looper)
        }
    }

    private fun drawTrumpet(canvas: Canvas){
        mTrumpetPool.draw(canvas)
    }

    fun pause(){
        val message = mDrawHandler?.obtainMessage(TrumpetHandler.CLEAR)
        mDrawHandler?.sendMessage(message)

    }

    fun resume(){
        val message = mDrawHandler?.obtainMessage(TrumpetHandler.RESUME)
        mDrawHandler?.sendMessage(message)
    }

    fun draw(){
        if (isPause){
            return
        }
        var canvas: Canvas? = null
        try {
            canvas = holder.lockCanvas()
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
                drawTrumpet(canvas)

            }
        }catch (e: Exception){
            Log.i(myTag,"draw throw exception:${e.message}")
        }finally {
            try {
                if (canvas != null){
                    holder.unlockCanvasAndPost(canvas)
                }
            }catch (e: Exception){
                Log.i(myTag,"draw throw exception:${e.message}")
            }
        }
    }

    fun visibleTrumpet(): List<Trumpet>{
        return mTrumpetPool.visibleTrumpets()
    }

    fun openTouch(boundsStrategy: IBoundsStrategy = DefaultBoundsStrategy(), listener: OnTrumpetClickListener){
        trumpetToucher = TrumpetToucher(this,boundsStrategy)
        this.onTrumpetClickListener = listener
    }

    var onTrumpetClickListener: OnTrumpetClickListener? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val isEventConsumed = trumpetToucher?.onTouchEvent(event) == true
        return if (!isEventConsumed) {
            super.onTouchEvent(event)
        } else isEventConsumed
    }

    interface OnTrumpetClickListener{
        fun onClick(trumpet: Trumpet)
    }
}