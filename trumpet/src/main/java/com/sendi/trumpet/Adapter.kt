package com.sendi.trumpet

import android.util.SparseArray
import android.view.View
import java.util.*

abstract class Adapter<VH : Adapter.ViewHolder> {

    private val mCacheViewHolders: Stack<VH> = Stack()
    private val mSaveViewHolder = SparseArray<VH>()

    fun getViewHolder(data: Trumpet): VH{
        var viewHolder = mSaveViewHolder[data.index]
        if (viewHolder !=null){
            return viewHolder
        }else{
            val type = getViewType(data)
            viewHolder = onCreateViewHolder(type)
            bindViewHolder(viewHolder, data)
            mSaveViewHolder.put(data.index,viewHolder)
        }
        return viewHolder
    }

    fun measure(width: Int,height: Int,trumpet: Trumpet){
        val vh = mSaveViewHolder[trumpet.index]
        if (!trumpet.isMeasured && vh!=null){
            vh.itemView.measure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST))
            trumpet.isMeasured = true
            val iw = vh.itemView.measuredWidth
            val ih = vh.itemView.measuredHeight
            trumpet.width = iw.toFloat()
            trumpet.height = ih.toFloat()
        }
    }

    fun layout(trumpet: Trumpet){
        val vh = mSaveViewHolder[trumpet.index]
        if (!trumpet.isLayout && vh!=null){
            vh.itemView.layout(0,0,trumpet.width.toInt(),trumpet.height.toInt())
            trumpet.isLayout = true
        }
    }

    fun getViewType(data: Trumpet): Int{
        return 0
    }

    abstract fun bindViewHolder(viewHolder: VH,data: Trumpet)

    abstract fun onCreateViewHolder(viewType: Int): VH

    abstract fun getSingleLineHeight(): Int


    abstract class ViewHolder(val itemView: View)

    fun recycle(trumpet: Trumpet){
        mSaveViewHolder.delete(trumpet.index)
    }

    fun quitClear(){
        mSaveViewHolder.clear()
    }
}