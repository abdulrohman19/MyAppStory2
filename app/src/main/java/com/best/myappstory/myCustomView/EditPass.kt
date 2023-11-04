package com.best.myappstory.myCustomView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.best.myappstory.R

class EditPass : AppCompatEditText {
    constructor(context: Context): super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init()
    }

    private var isPasswordVisible: Boolean = false
    private val showPasswordDrawable: Drawable? = ContextCompat.getDrawable(context,
        R.drawable.ic_eyevisibilityshow
    )
    private val hidePasswordDrawable: Drawable? = ContextCompat.getDrawable(context,
        R.drawable.ic_eyevisibilityhide
    )

    var alertMsg = ""
    var alertCode = 0

    private fun init() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                if (password.length < 8) {
                    alertCode = 1
                    alertMsg = "Password must be at least 8 characters"
                } else {
                    alertCode = 0
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val showIcon = s?.isNotEmpty() ?: false
                val endDrawable = if (showIcon) showPasswordDrawable else null
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, endDrawable, null)
            }
        })

        setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP && event.rawX >= right - compoundPaddingEnd) {
                togglePasswordVisibility()
                return@setOnTouchListener true
            }
            return@setOnTouchListener false
        }
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        val inputType = if (isPasswordVisible) {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        setInputType(inputType)

        val endDrawable = if (isPasswordVisible) hidePasswordDrawable else showPasswordDrawable
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, endDrawable, null)
    }
}