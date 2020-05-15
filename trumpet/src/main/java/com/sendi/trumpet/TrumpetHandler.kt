package com.sendi.trumpet

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import com.sendi.trumpet.view.TrumpetSurfaceView

class TrumpetHandler(val trumpet: TrumpetSurfaceView, looper: Looper) : Handler(looper){

    private val myTag = "TrumpetHandler"

    companion object{
        const val START = 0
        const val UPDATE = 1
        const val QUIT = 2
        const val CLEAR = 3
        const val RESUME = 4
    }

    private var quit = true

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        when(msg.what) {
            START -> {
                Log.i(myTag,"START")
                quit = false
                prepare()
                val updateMsg = obtainMessage(UPDATE)
                sendMessage(updateMsg)
            }
            UPDATE ->{
                removeMessages(UPDATE)
                val startTime = System.currentTimeMillis()
                trumpet.draw()
                val endTime = System.currentTimeMillis()
                val drawTime = endTime - startTime
                if (drawTime < 16) {
                    Log.i(myTag,"drawTime:$drawTime")
                    try {
                        Thread.sleep(16 - drawTime)
                    }catch (e: InterruptedException){
                        Log.i(myTag,"update sleep throw InterruptedException.")
                    }
                }
                if (!quit){
                    val message = obtainMessage(UPDATE)
                    sendMessage(message)
                }
            }

            RESUME-> {
                Log.i(myTag,"RESUME")
                /*   trumpet.clear()*/
                if (!quit){
                    val message = obtainMessage(UPDATE)
                    sendMessage(message)
                }
            }

            CLEAR ->{
                Log.i(myTag,"CLEAR")
                removeMessages(UPDATE)
                trumpet.clear()
            }
            QUIT -> {
                Log.i(myTag,"QUIT")
                removeMessages(UPDATE)
                quit = true
            }
        }
    }



    private fun prepare(){
        //这里准备做一些准备操作，目前还没有
    }
}