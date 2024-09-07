package com.example.utils.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton

class CustomButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialButton(context, attrs, defStyleAttr) {

    init {
        // Custom initialization if needed
        setupButton()
    }

    private fun setupButton() {
        // Add custom functionality if required
    }
}
