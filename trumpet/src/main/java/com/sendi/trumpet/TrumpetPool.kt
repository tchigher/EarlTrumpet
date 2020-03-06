package com.sendi.trumpet

import android.graphics.Canvas
import android.util.Log
import android.util.SparseArray
import android.util.SparseIntArray
import android.view.View
import java.util.*
import kotlin.collections.ArrayList

class TrumpetPool {

    private val myTag = "TrumpetPool"
    companion object{
        const val DEFAULT_CLEAR_THRESHOLD = 8
    }

    private val visibleTrumpetList: MutableList<Trumpet> = LinkedList<Trumpet>()//当前可见的弹幕

    private lateinit var mAdapter: Adapter<*>

    var speed = 1//速度

    var maxHeight = 0
    var maxWidth = 0

    private var clearThreshold = 0

    private var lineHeight: Int = 0
    private var lines: Int = -1
    private val lineDanmus = SparseIntArray()//记录每行的个数
    private val lineLastDanmu = SparseArray<Trumpet>(16)//记录每行最后一个
    private val newTrumpetQueue: Queue<Trumpet> = LinkedList<Trumpet>()


    fun setAdapter(adapter: Adapter<*>){
        this.mAdapter = adapter
    }

    fun updateWidthAndHeight(width: Int,height: Int){
        maxWidth = width
        maxHeight = height
        lineHeight = mAdapter.getSingleLineHeight()
        if (lines == -1){
            Log.i(myTag,"maxHeight:$maxHeight lineHeight$lineHeight")
            lines = maxHeight / lineHeight
        }
    }



    fun draw(canvas: Canvas){
        val newTrumpet = findWillVisiable()
        visibleTrumpetList.addAll(newTrumpet)
        if (visibleTrumpetList.isEmpty()){
            return
        }
        visibleTrumpetList.forEach { trumpet ->
            val viewHolder = mAdapter.getViewHolder(trumpet)
            val itemView = viewHolder.itemView
            canvas.save()
            itemView.measure(View.MeasureSpec.makeMeasureSpec(canvas.width, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(canvas.height, View.MeasureSpec.AT_MOST))
            val width = itemView.measuredWidth
            val height = itemView.measuredHeight
            trumpet.width = width.toFloat()
            trumpet.height = height.toFloat()
            canvas.translate(getX(trumpet), getY(trumpet))
            itemView.layout(0,0,width,height)
            itemView.draw(canvas)
            canvas.restore()
        }
        tryClear()
    }

    @Synchronized
    private fun findWillVisiable(): List<Trumpet>{
        val newAddTrumpets = ArrayList<Trumpet>(32)
        while (newTrumpetQueue.isNotEmpty()){
            val line = getBestLine()
            if (line == -1){
                break
            }
            val trumpet = newTrumpetQueue.poll()
            Log.i(myTag,"trumpet${trumpet.data} find best line is:$line")
            trumpet?.let {
                trumpet.updateY((line * lineHeight).toFloat())
                lineLastDanmu.put(line,it)
                lineDanmus.put(line,lineDanmus.get(line,0)+1)
                newAddTrumpets.add(it)
            }
        }
        return newAddTrumpets
    }

    private fun getBestLine(): Int{
        //寻找空行
        for (i in 0 until lines){
            if (lineDanmus.get(i,0) == 0){
                return i
            }
        }

        //找能够放进去的
        for (i in 0 until lines){
            val trumpet = lineLastDanmu.get(i)
            if (trumpet != null){
                if (trumpet.width>0 && maxWidth - trumpet.x >= trumpet.width){
                    return i
                }
            }else{
                return i
            }
        }

        return -1
    }

    //清理已经不再显示的弹幕
    private fun tryClear(){
        val iterator = visibleTrumpetList.iterator()
        while (iterator.hasNext()){
            val trumpet = iterator.next()
            if (trumpet.isOutside()){
                iterator.remove()
                updateLineRecord(trumpet)
            }
        }
    }

    private fun updateLineRecord(trumpet: Trumpet){
        val line = trumpet.y.toInt() / lineHeight
        Log.i(myTag,"updateLineRecord line $line")
        lineDanmus.put(line,lineDanmus.get(line,1)-1)
    }

    @Synchronized
    fun addTrumpet(trumpet: Trumpet){
        trumpet.x = maxWidth.toFloat()
        newTrumpetQueue.offer(trumpet)
        Log.i(myTag,"offer new trumpet ${trumpet.data}")
    }

    @Synchronized
    fun addTrumpets(trumpets:List<Trumpet>){
        trumpets.forEach { addTrumpet(it) }
    }

    private fun getX(trumpet: Trumpet): Float{
        //到时候有加类型的话，可以根据类型来决定控制x
        val x = trumpet.x - speed
        trumpet.updateX(x)
        return x
    }

    private fun getY(trumpet: Trumpet): Float{
        //到时候有加类型的话，可以根据类型来决定控制y

        return trumpet.y
    }

    fun visibleTrumpets(): List<Trumpet>{
        return ArrayList(visibleTrumpetList)
    }
}