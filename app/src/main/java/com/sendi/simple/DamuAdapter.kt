package com.sendi.simple

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sendi.danmudemo.R

class DamuAdapter(val context: Context) : com.sendi.trumpet.Adapter<DamuAdapter.DanmuViewHolder>() {
    override fun bindViewHolder(viewHolder: DanmuViewHolder, data: com.sendi.trumpet.Trumpet) {
        data.data?.let {
            if (it is Entity){
                viewHolder.content.text = it.content
                viewHolder.image.setImageResource(it.imageRes)
            }
        }
    }

    override fun onCreateViewHolder(viewType: Int): DanmuViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_danmu,null)
        return DanmuViewHolder(view)
    }

    override fun getSingleLineHeight(): Int {
        return 55
    }


    class DanmuViewHolder(view: View): ViewHolder(view){
        val  content: TextView = view.findViewById(R.id.content)
        val image: ImageView = view.findViewById(R.id.image)
    }
}