package com.lxj.runball

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextSwitcher
import android.widget.TextView
import android.widget.ViewSwitcher

/**
 *
 * @Author： Jason
 * @Time： 2019/3/27
 * @Description: JP1160873948@163.com
 *
 */
class RunningBall(context: Context?, attributes: AttributeSet) : TextSwitcher(context, attributes),
    ViewSwitcher.ViewFactory {
    private val FLAG_START_AUTO_SCROLL = 0
    private val FLAG_STOP_AUTO_SCROLL = 1
    private val TAG = RunningBall::class.java.simpleName
    private var mContext: Context? = null
    private var numberList: ArrayList<Int>? = null
    private var currentId = -1
    private var stopNum = -1
    private var stopDelay = 100L
    private var startDelay = 100L
    private var mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            when (msg!!.what) {
                FLAG_START_AUTO_SCROLL -> {
                    currentId++
                    setText(numberList!![currentId % numberList!!.size].apply {
                        if (this == stopNum) {
                            sendEmptyMessageDelayed(FLAG_STOP_AUTO_SCROLL, stopDelay)
                        } else {
                            sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, startDelay)
                        }
                    }.toString())
                }
                FLAG_STOP_AUTO_SCROLL -> {
                    removeMessages(FLAG_START_AUTO_SCROLL)
                }
            }
        }
    }

    init {
        mContext = context
        numberList = ArrayList()
    }


    /**
     * 设置动画时间，初始化View
     */
    fun setAnimTime(animDuration: Long) {
        setFactory(this)
        val `in` = TranslateAnimation(0f, 0f, animDuration.toFloat(), 0f)
        `in`.duration = animDuration
        `in`.interpolator = AccelerateInterpolator()
        val out = TranslateAnimation(0f, 0f, 0f, (-animDuration).toFloat())
        out.duration = animDuration
        out.interpolator = AccelerateInterpolator()
        inAnimation = `in`
        outAnimation = out
    }

    public fun setNumberRange(endNum: Int) {
        setNumberRange(0, endNum)
    }

    public fun setNumberRange(firstNum: Int, endNum: Int) {
        numberList!!.clear()
        currentId = -1
        for (num in firstNum..endNum) {
            numberList!!.add(num)
        }
    }

    public fun startRunning() {
        if (numberList != null && numberList!!.size > 0) {
            mHandler.sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, 200)
        } else {
            Log.d(TAG, "数字集合不能为空数据")
        }
    }

    public fun stopRunning(stopNum: Int) {
        stopRunningDelay(stopNum, startDelay)
    }

    public fun stopRunningDelay(stopNum: Int, delay: Long) {
        this.stopNum = stopNum
        this.stopDelay = delay
    }

    override fun makeView(): View {
        val textView = TextView(mContext)
/*        val layoutParams = FrameLayout.LayoutParams(
            dip2px(25f).toInt()
            , dip2px(25f).toInt()
        )
        textView.layoutParams = layoutParams*/
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.WHITE)
        textView.textSize = 12f
        return textView
    }

    private fun dip2px(dipValue: Float): Float {
        val scale = resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }
}