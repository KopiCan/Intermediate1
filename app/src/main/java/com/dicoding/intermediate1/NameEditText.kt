package com.dicoding.intermediate1

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class NameEditText :AppCompatEditText, View.OnTouchListener {

    private lateinit var crossButton: Drawable

    override fun onTouch(p0: View?, p1: MotionEvent): Boolean {
        if (compoundDrawables[2]!=null){
            val buttonStart: Float
            val buttonEnd: Float
            var buttonClickState = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                buttonEnd = (crossButton.intrinsicWidth + paddingLeft).toFloat()
                when {
                    p1.x < buttonEnd -> buttonClickState = true
                }
            } else {
                buttonStart = (width - paddingRight - crossButton.intrinsicWidth).toFloat()
                when {
                    p1.x > buttonStart -> buttonClickState = true
                }
            }
            if (buttonClickState) {
                when (p1.action) {
                    MotionEvent.ACTION_DOWN -> {
                        crossButton = ContextCompat.getDrawable(
                            context, R.drawable.ic_baseline_clear_24
                        ) as Drawable
                        showButton()
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        crossButton = ContextCompat.getDrawable(
                            context, R.drawable.ic_baseline_clear_24
                        ) as Drawable
                        when {
                            text != null -> text?.clear()
                        }
                        hideButton()
                        return true
                    }
                    else -> return false
                }
            }else {
                return false
            }
        }
        return false
    }

    private fun initiate() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) showButton() else hideButton()
            }

            override fun afterTextChanged(p0: Editable?) {
                TODO("Not yet implemented")
            }
        })
    }

    constructor(context: Context) : super(context){
        initiate()
    }
    constructor(context: Context, attributes: AttributeSet) : super(context,attributes){
        initiate()
    }
    constructor(context: Context, attributes: AttributeSet, styleAttributes: Int) : super(context, attributes, styleAttributes){
        initiate()
    }

    private fun setButton(startOfText: Drawable? = null,
                          topOfText: Drawable? = null,
                          endOfText: Drawable? = null,
                          bottomOfText:Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(startOfText, topOfText, endOfText, bottomOfText)
    }

    private fun hideButton() {
        setButton()
    }

    private fun showButton() {
        setButton(endOfText = crossButton)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        context.apply {
            setTextColor(ContextCompat.getColor(this, R.color.base_green))
            setHintTextColor(ContextCompat.getColor(this, R.color.gray_dark))
            background = ContextCompat.getDrawable(this, R.drawable.field_background)
        }
        maxLines = 1
    }




}