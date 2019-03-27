package com.lxj.runball

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text1 = findViewById<RunningBall>(R.id.rxText1)
        text1.setAnimTime(400)
        text1.setCurrentText("?")
        text1.setNumberRange(100)
        text1.startRunning()
        text1.stopRunning(98)

        val text2 = findViewById<RunningBall>(R.id.rxText2)
        text2.setAnimTime(100)
        text2.setCurrentText("?")
        text2.setNumberRange(100)
        text2.startRunning()
        text2.stopRunning(75)

        val text3 = findViewById<LotteryView>(R.id.rxText3)
        text3.setNumberList(100)
        text3.stopScroll(99,100)
    }
}
