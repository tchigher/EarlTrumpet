package com.sendi.trumpet.touch

import android.graphics.RectF

/**
 * 限定点击弹幕某个区域的策略接口
 */
interface IBoundsStrategy{
    fun delimit(bounds: RectF): RectF
}