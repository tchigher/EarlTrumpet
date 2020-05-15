package com.sendi.trumpet

class TrumpetConfig {
    companion object{
        const val DEFAULT_SPEED = 1F
    }

    val speed: Float
    val lineHeight: Int

    constructor(builder: Builder){
        this.speed = builder.speed
        this.lineHeight = builder.lineHeight
    }


    inner class Builder(){

        var speed= DEFAULT_SPEED
        var lineHeight = 0
        fun speed(speed:  Float): Builder {
            this.speed = speed
            return this
        }

        fun lineHeight(lineHeight: Int): Builder {
            this.lineHeight = lineHeight
            return this
        }

        fun build(): TrumpetConfig {
            return TrumpetConfig(this)
        }


    }
}