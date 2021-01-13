package com.brainbear.corner

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout

class RoundCornerFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val roundCornerHelper: RoundCornerHelper =
        RoundCornerHelper(this, context, attrs, defStyleAttr)

    override fun draw(canvas: Canvas) {
        roundCornerHelper.onPreDraw(canvas)
        super.draw(canvas)
        roundCornerHelper.onAfterDraw(canvas)
    }

    override fun invalidate() {
        roundCornerHelper.invalidate()
        super.invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        roundCornerHelper.invalidate()
    }
}