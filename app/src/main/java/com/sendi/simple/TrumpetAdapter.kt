package com.sendi.simple

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sendi.danmudemo.R

class TrumpetAdapter(val context: Context) : com.sendi.trumpet.Adapter<TrumpetAdapter.TrumpetViewHolder>() {
    override fun bindViewHolder(viewHolder: TrumpetViewHolder, data: com.sendi.trumpet.Trumpet) {
        data.data?.let {
            if (it is Entity){
                viewHolder.content.text = it.content
                viewHolder.image.setImageResource(it.imageRes)
            }
        }
    }

    override fun onCreateViewHolder(viewType: Int): TrumpetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_danmu,null)
        return TrumpetViewHolder(view)
    }

    override fun getSingleLineHeight(): Int {
        return 80
    }


    class TrumpetViewHolder(view: View): ViewHolder(view){
        val  content: TextView = view.findViewById(R.id.content)
        val image: ImageView = view.findViewById(R.id.image)
    }
}