package com.sendi.simple

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sendi.danmudemo.R

class TrumpetAdapter(val context: Context,val root: ViewGroup) : com.sendi.trumpet.Adapter<TrumpetAdapter.TrumpetViewHolder>() {
    override fun bindViewHolder(viewHolder: TrumpetViewHolder, data: com.sendi.trumpet.Trumpet) {
        /*data.data?.let {
            if (it is Entity){
                viewHolder.content.text = it.content
                viewHolder.image.setImageResource(it.imageRes)
            }
        }*/
    }

    override fun onCreateViewHolder(viewType: Int): TrumpetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.test_layout,null)
        val linearLayout = LinearLayout(context)
        val textView = TextView(context)
        textView.text = "加我好友，以后一0000000000000000000000起开黑000000000000"
        val params = ViewGroup.LayoutParams(2500,ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout.layoutParams = params
        linearLayout.clipChildren = false
        linearLayout.addView(textView)
        return TrumpetViewHolder(view)
    }

    override fun getSingleLineHeight(): Int {
        return 80
    }


    class TrumpetViewHolder(view: View): ViewHolder(view){
        /*val  content: TextView = view.findViewById(R.id.content)
        val image: ImageView = view.findViewById(R.id.image)*/
    }
}