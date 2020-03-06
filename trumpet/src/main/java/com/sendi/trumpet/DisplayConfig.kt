package com.sendi.trumpet

import android.content.Context

class DisplayConfig(context: Context){

    private val screenWidth: Int
    private val screenHigth: Int

    init {
        screenWidth = context.resources.displayMetrics.widthPixels
        screenHigth = context.resources.displayMetrics.heightPixels
    }


}