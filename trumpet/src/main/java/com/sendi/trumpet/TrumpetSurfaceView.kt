package com.sendi.trumpet

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
import com.sendi.trumpet.touch.DefaultBoundsStrategy
import com.sendi.trumpet.touch.IBoundsStrategy
import com.sendi.trumpet.touch.TrumpetToucher
import java.util.concurrent.Executors

class TrumpetSurfaceView(context: Context, attributeSet: AttributeSet?, defaultStyle: Int)
    : SurfaceView(context,attributeSet,defaultStyle),SurfaceHolder.Callback{

    private val myTag = "TrumpetSurfaceView"

    private val DANMU_EXECUTOR = Executors.newFixedThreadPool(3)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null, 0)

    private var mTrumpetPool: TrumpetPool = TrumpetPool()

    private lateinit var mDrawHandler: TrumpetHandler

    private var trumpetToucher: TrumpetToucher? = null

    @Volatile
    private var isPause = false

    init {
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSPARENT)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.i(myTag,"wid: $width hei: $height")
        mTrumpetPool.updateWidthAndHeight(width,height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        isPause = true
        quit()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        prepare()
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

    private fun prepare(){
    }

    private fun quit(){
        val message = mDrawHandler.obtainMessage(TrumpetHandler.QUIT)
        mDrawHandler.sendMessage(message)
    }

    fun config(config: TrumpetConfig){
        //todo:像pool设置配置
        mTrumpetPool.speed = config.speed
    }

    fun start(){
        checkDrawHandler()
        val startMsg = mDrawHandler.obtainMessage(TrumpetHandler.START)
        mDrawHandler.sendMessage(startMsg)
    }

    private fun checkDrawHandler(){
        if (!::mDrawHandler.isInitialized){
            val thread = HandlerThread("draw#thread")
            thread.start()
            mDrawHandler = TrumpetHandler(this, thread.looper)
        }
    }

    private fun drawTrumpet(canvas: Canvas){
        mTrumpetPool.draw(canvas)
    }

    fun draw(){
        val canvas = holder.lockCanvas()
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
            drawTrumpet(canvas)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    fun visibleTrumpet(): List<Trumpet>{
        return mTrumpetPool.visibleTrumpets()
    }

    fun openTouch(boundsStrategy: IBoundsStrategy = DefaultBoundsStrategy(),listener: OnTrumpetClickListener){
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