package com.lxj.runball

import android.animation.Animator
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.REVERSE
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.BounceInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.addListener

/**
 *
 * @Author： Jason
 * @Time： 2019/3/27
 * @Description: JP1160873948@163.com
 *
 */
class RunningLayout(context: Context,attributeSet: AttributeSet) : LinearLayout(context,attributeSet){
    private var numberList: ArrayList<Int>? = null
    private var currentId = -1
    private var mContext: Context? = null
    private var valueAnimator:ValueAnimator? = null
    init {
        this.mContext = context
        numberList = ArrayList()
        addView(makeView("?"))
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(dip2px(25f).toInt(), dip2px(25f).toInt())
    }
    public fun setNumberRange(endNum: Int) {
        setNumberRange(0, endNum)
    }

    public fun setNumberRange(firstNum: Int, endNum: Int) {
        numberList!!.clear()
        currentId = -1
        for (num in firstNum..endNum) {
            numberList!!.add(num)
            addView(makeView(num.toString()))
        }
    }



    fun startPoint(){
        valueAnimator = ValueAnimator.ofObject(object :TypeEvaluator<Int>{
            override fun evaluate(fraction: Float, startValue: Int?, endValue: Int?): Int {
                Log.d("TAG","$fraction:$startValue:$endValue")
                return (startValue!!+fraction*(endValue!! -startValue)).toInt()
            }
        },0,dip2px(25f*numberList!!.size).toInt()).apply {
            this.addUpdateListener {
                Log.d("TAG","animatedValue:${it.animatedValue as Int}")
                scrollTo(0, it.animatedValue as Int)
            }
            this.repeatCount = -1
            this.repeatMode = REVERSE
            this.duration = 10000
            //插值器决定估值器中fraction因子的值
            this.interpolator = LinearInterpolator()
            this.start()
        }
    }


    fun stopPoint(number:Int,delay:Long){
      ValueAnimator.ofInt(0,dip2px(25f*(number+1)).toInt()).apply {
           this.addUpdateListener {
               scrollTo(0,it.animatedValue as Int)
           }
           this.addListener(object :Animator.AnimatorListener{
               override fun onAnimationRepeat(animation: Animator?) {

               }

               override fun onAnimationEnd(animation: Animator?) {

               }

               override fun onAnimationCancel(animation: Animator?) {

               }

               override fun onAnimationStart(animation: Animator?) {
                  valueAnimator!!.cancel()
               }

           })
           this.repeatCount = 0
           this.interpolator = LinearInterpolator()
           this.duration = 10000
           startDelay = delay
           start()
       }
    }
    private fun makeView(text:String): View {
        val textView = TextView(mContext)
        val layoutParams = FrameLayout.LayoutParams(
            dip2px(25f).toInt()
            , dip2px(25f).toInt()
        )
        textView.text = text
        textView.layoutParams = layoutParams
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.WHITE)
        textView.setBackgroundResource(R.drawable.shape_running_ball_bg)
        textView.textSize = 12f
        return textView
    }


    private fun dip2px(dipValue: Float): Float {
        val scale = resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }
}