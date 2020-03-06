package com.sendi.trumpet.touch

import android.graphics.RectF

class DefaultBoundsStrategy : IBoundsStrategy {
    override fun delimit(bounds: RectF): RectF {
        return bounds
    }
}