package com.sendi.trumpet

import android.os.Handler
import android.os.Looper
import android.os.Message

class TrumpetHandler(val trumpet: TrumpetSurfaceView, looper: Looper) : Handler(looper){

    companion object{
        const val START = 0
        const val UPDATE = 1
        const val QUIT = 2
    }

    private var quit = false

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what) {
            START -> {
                prepare()
                val updateMsg = obtainMessage(UPDATE)
                sendMessage(updateMsg)
            }
            UPDATE ->{
                removeMessages(UPDATE)
               trumpet.draw()
                if (!quit){
                    val message = obtainMessage(UPDATE)
                    sendMessage(message)
                }
            }
            QUIT -> {
                quit = true
            }
        }
    }



    private fun prepare(){
        //这里准备做一些准备操作，目前还没有
    }
}