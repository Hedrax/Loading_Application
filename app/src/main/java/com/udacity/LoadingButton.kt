package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var currentAngle:Int  = 0
    private var widthSize = 0
    private var heightSize = 0
    private var text = context.getString(R.string.button_Download)
    private var textColor = 0
    private var textSize = 0
    private var background_Color = 0
    private var clicked = false
    private var valueAnimator:ValueAnimator? = null
    private val rect: RectF = RectF(0f, 0f, 0f, 0f)

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        if (buttonState == ButtonState.Completed){
            text = context.getString(R.string.button_Download)
            clicked = false
        }
        else if (buttonState == ButtonState.Loading){
            text = context.getString(R.string.button_loading)
            clicked = true
            animatationStart()
        }
        invalidate()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }
    init {

        with(context.obtainStyledAttributes(attrs,R.styleable.LoadingButton)){
            textSize = getDimensionPixelSize(R.styleable.LoadingButton_textSize,0)
            background_Color = getColor(R.styleable.LoadingButton_backgroundColor,0)
            textColor = getColor(R.styleable.LoadingButton_textColor,0)
        }

        isClickable = true

    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.color = background_Color
        canvas?.drawRect(0.0F, 0.0F, widthSize.toFloat(), heightSize.toFloat(), paint)
        paint.color = textColor
        paint.textSize = textSize.toFloat()
        canvas?.drawText(text, widthSize / 2f, heightSize / 2*1.2f, paint)
        if(clicked)click(canvas)
    }
    private fun click(canvas:Canvas?){
        paint.color = Color.GRAY
        rect.set(0f, heightSize / 7f, widthSize / 1f, heightSize / 1.2f)
        canvas?.drawArc(rect,200f,currentAngle.toFloat(),true,paint)

        paint.color = textColor
        paint.textSize = textSize.toFloat()
        canvas?.drawText(text, widthSize / 2f, heightSize / 2*1.2f, paint)

    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }
    private fun animatationStart(){
        valueAnimator?.cancel()
        valueAnimator = ValueAnimator.ofInt(0,360).apply{
            duration = 2500
            interpolator = LinearInterpolator()
            addUpdateListener {
                currentAngle = it.animatedValue as Int
                invalidate()
            }

        }
        valueAnimator?.start()
    }
    fun buttonState(value: ButtonState){
        buttonState = value
    }

}