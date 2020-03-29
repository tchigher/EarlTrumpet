package com.sendi.trumpet


class Trumpet {

    var x: Float = -1f
    var y: Float = -1f
    var height: Float =0f
    var width: Float =0f
    var index: Int = -1

    var isVisiable = false

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