package com.sendi.trumpet


class Trumpet {

    var x: Float = -1f
    var y: Float = -1f

    var height: Float =0f
    var width: Float =0f
    /**
     * index不允许有重复，否则会出现弹幕异常消失的情况
     * 默认情况下不要手动去设置
     */
    var index: Int = -1

    var data: Any? = null

    var isMeasured: Boolean = false

    var isLayout: Boolean = false


    fun left(): Float{
        return x
    }

    fun top(): Float{
        return y
    }

    fun right(): Float{
        return x + width
    }

    fun bottom(): Float{
        return y + height
    }

    fun isOutside(): Boolean{
        return -x >= width
    }

    fun updateX(x: Float){
        this.x = x
    }
    fun updateY(y: Float){
        this.y = y
    }
    fun updateHight(hight: Float){
        this.height = hight
    }
    fun updateWidth(width: Float){
        this.width = width
    }

}