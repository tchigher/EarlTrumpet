package com.sendi.trumpet.touch

import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import com.sendi.trumpet.Trumpet
import com.sendi.trumpet.view.TrumpetSurfaceView

/**
 * 通过此类来对弹幕的点击事件进行分发
 */
class TrumpetToucher(val trumpetView: TrumpetSurfaceView, private val boundsStrategy: IBoundsStrategy) {
    private val mTouchDelegate: GestureDetector

    init {
        mTouchDelegate = GestureDetector(trumpetView.context,provideGestureListener())
    }

    private fun provideGestureListener(): GestureDetector.OnGestureListener {
        return object : GestureDetector.SimpleOnGestureListener() {

            private var lastTrumpet: Trumpet? = null

            override fun onDown(event: MotionEvent): Boolean {
                trumpetView.onTrumpetClickListener?.let {
                    lastTrumpet = findHitTrumpet(event.x, event.y)
                    return lastTrumpet != null
                }

                return false
            }

            override fun onSingleTapConfirmed(event: MotionEvent): Boolean {
                var isEventConsumed = false
                lastTrumpet?.let {
                    isEventConsumed = performTrumpetClick(it)
                }
                return isEventConsumed
            }
        }
    }

    fun onTouchEvent(event: MotionEvent): Boolean{
        return mTouchDelegate.onTouchEvent(event)
    }

    private fun performTrumpetClick(trumpet: Trumpet): Boolean{
        trumpetView.onTrumpetClickListener?.onClick(trumpet)
        return true
    }

    private fun findHitTrumpet(x: Float,y: Float): Trumpet?{
        val bounds = RectF()
        trumpetView.visibleTrumpet().forEach { trumpet->
            bounds.set(trumpet.left(),trumpet.top(),trumpet.right(),trumpet.bottom())
            if (isHit(bounds, x, y)){
                return trumpet
            }
        }
        return null
    }

    private fun isHit(bounds: RectF,x: Float,y: Float): Boolean{
        val newBounds = boundsStrategy.delimit(bounds)
        return newBounds.intersect(x, y, x, y)
    }


}