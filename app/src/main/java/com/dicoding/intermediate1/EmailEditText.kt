package com.dicoding.intermediate1

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class EmailEditText : AppCompatEditText {

    private fun initiate() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
                error = if (p0.isNotEmpty()) {
                    if (!p0.toString().matches(Constanst.emailDefaultPattern)) {
                        context.getString(R.string.error_400)
                    } else null
                }else null
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        context.apply {
            setTextColor(ContextCompat.getColor(this, R.color.base_green))
            setHintTextColor(ContextCompat.getColor(this, R.color.gray_dark))
            background = ContextCompat.getDrawable(this, R.drawable.field_background)
        }
    }

}