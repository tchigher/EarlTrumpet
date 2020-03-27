package com.sendi.trumpet

import android.util.SparseArray
import android.view.View
import java.util.*

abstract class Adapter<VH : Adapter.ViewHolder> {

    private val mCacheViewHolders: Stack<VH> = Stack()
    private val mSaveViewHolder = SparseArray<VH>()
    //todo:每个trumpet的id对应一个view，清除的时候，也是对应id然后清除掉view，
    // 这样可以避免每次都要bindViewHolder和measure和layout
    fun getViewHolder(data: Trumpet): VH{
        if (mCacheViewHolders.isEmpty()){
            //以后考虑多种view类型
            val type = getViewType(data)
            val viewHolder = onCreateViewHolder(type)
            mCacheViewHolders.push(viewHolder)
            bindViewHolder(viewHolder, data)

            return viewHolder
        }

        val cViewHolder = mCacheViewHolders.peek()
        bindViewHolder(cViewHolder,data)
        return cViewHolder
    }


    fun getViewType(data: Trumpet): Int{
        return 0
    }

    abstract fun bindViewHolder(viewHolder: VH,data: Trumpet)

    abstract fun onCreateViewHolder(viewType: Int): VH

    abstract fun getSingleLineHeight(): Int


    abstract class ViewHolder(val itemView: View)

    private fun recycle(){
        //todo:这里进行回收操作
    }
}