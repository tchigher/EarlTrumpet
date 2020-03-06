package com.sendi.danmudemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.sendi.trumpet.Trumpet
import com.sendi.trumpet.TrumpetSurfaceView
import com.sendi.simple.DamuAdapter
import com.sendi.simple.Entity

class MainActivity : AppCompatActivity() {

    private lateinit var mDamuView: TrumpetSurfaceView

    private val myTag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mDamuView = findViewById(R.id.danmuView)
        mDamuView.setAdapter(DamuAdapter(this))
        mDamuView.openTouch(listener = object : TrumpetSurfaceView.OnTrumpetClickListener{
            override fun onClick(trumpet: Trumpet) {
                Log.i(myTag,"onClick data is ${trumpet.data}")
            }
        })

        findViewById<Button>(R.id.btn_add_one).setOnClickListener {
            val trumpet = Trumpet()
            trumpet.data = Entity("one this is content --------$index",getRes(index))
            ++index
            mDamuView.addTrumpet(trumpet)
        }

        findViewById<Button>(R.id.btn_add_more).setOnClickListener {
            mDamuView.addTrumpets(getDanmuList())
        }
        mDamuView.start()
    }

    private fun getDanmuList(): List<Trumpet>{
        val list = ArrayList<Trumpet>()
        for(i in 0 until 50){
            val danmu = Trumpet()
            danmu.index = i
            danmu.data = Entity("group this is content $i",getRes(i))
            list.add(danmu)
        }

        return  list
    }
    private var index = 0
    private fun getRes(index: Int): Int{
        val rI = index % 6
        return when(rI){
            0 -> R.drawable.icon1
            1 -> R.drawable.icon2
            2 -> R.drawable.icon3
            3 -> R.drawable.icon4
            4 -> R.drawable.icon5
            else -> R.drawable.heart
        }
    }
}
