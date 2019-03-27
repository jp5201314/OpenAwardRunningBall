package com.lxj.runball

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import java.lang.Math.abs
/**
 *
 * @Author： Jason
 * @Time： 2019/3/27
 * @Description: JP1160873948@163.com
 *
 */
class LotteryView(context: Context,attributeSet: AttributeSet) : View(context,attributeSet) {

    private val data = ArrayList<Int>()
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val size = dip2px(23f).toInt()
    private var maxHeight = size * (data.size - 1)
    private val mDuration = 3000L
    private var scrollAnimator = ValueAnimator()

    init {
        mPaint.color = Color.WHITE
        mPaint.textSize = dip2px(12f)
        mPaint.typeface = Typeface.DEFAULT_BOLD
        mPaint.textAlign = Paint.Align.CENTER
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val x = (width / 2).toFloat()
        val fontMetrics = mPaint.fontMetrics
        for (i in 0 until data.size) {
            val y = (height / 2 + (abs(fontMetrics.ascent) - fontMetrics.descent) / 2) - (height * i)
            canvas?.drawText(data[i].toString(), x, y, mPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(size, size)
    }

    private fun startScroll() {
        maxHeight = size * (data.size - 1)
        scrollAnimator = ValueAnimator.ofInt(0, maxHeight).apply {
            addUpdateListener {
                scrollTo(0, -(it.animatedValue as Int))
            }
            interpolator = LinearInterpolator()
            repeatCount = -1
            duration = 1000L
            start()
        }
    }

    fun stopScroll(number: Int, delay: Long) {
        stopScroll(number, delay, null)
    }

    fun stopScroll(number: Int, delay: Long, animatorStatusListener: AnimatorStatusListener?) {
        val endIndex = data.indexOf(number)
        maxHeight = size * (data.size - 1)
        if (endIndex < data.size - 1) {
            val endValue = size * endIndex
            ValueAnimator.ofInt(0, maxHeight + endValue).run {
                addUpdateListener {
                    if (it.animatedValue as Int >= maxHeight) {
                        scrollTo(0, -(it.animatedValue as Int % maxHeight))
                    } else {
                        scrollTo(0, -(it.animatedValue as Int))
                    }
                }
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        animatorStatusListener?.animEnd()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                        animatorStatusListener?.animStart()
                        scrollAnimator.cancel()
                    }
                })
                interpolator = LinearInterpolator()
                duration = mDuration
                startDelay = delay
                start()
            }
        } else {
            scrollTo(0, 0)
        }
    }

    fun setNumberList(maxNumber: Int) {
        setNumberList(0, maxNumber)
    }

    fun setNumberList(minNumber: Int, maxNumber: Int) {
        data.clear()
        if (maxNumber > minNumber) {
            for (i in minNumber..maxNumber) {
                data.add(i)
            }
            data.add(minNumber)
        }
        startScroll()
    }

    private fun dip2px(dipValue: Float): Float {
        val scale = resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }

    interface AnimatorStatusListener {
        fun animStart()
        fun animEnd()
    }
}