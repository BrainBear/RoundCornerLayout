package com.brainbear.corner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.max

private const val INVALID_VALUE = -1

class RoundCornerHelper(
    private val view: View,
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int
) {


    private val clipPath = Path()
    private val roundCornerAttrs = RoundCornerAttrs()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.round_corner_layout_attrs)

        roundCornerAttrs.halfSizeRadius = typedArray.getBoolean(
            R.styleable.round_corner_layout_attrs_round_corner_half_size_radius,
            false
        )

        roundCornerAttrs.radius = typedArray.getDimensionPixelSize(
            R.styleable.round_corner_layout_attrs_round_corner_radius,
            INVALID_VALUE
        )

        roundCornerAttrs.topLeftRadius = typedArray.getDimensionPixelSize(
            R.styleable.round_corner_layout_attrs_round_corner_top_left_radius,
            INVALID_VALUE
        )

        roundCornerAttrs.topRightRadius = typedArray.getDimensionPixelSize(
            R.styleable.round_corner_layout_attrs_round_corner_top_right_radius,
            INVALID_VALUE
        )

        roundCornerAttrs.bottomRightRadius = typedArray.getDimensionPixelSize(
            R.styleable.round_corner_layout_attrs_round_corner_bottom_right_radius,
            INVALID_VALUE
        )

        roundCornerAttrs.bottomLeftRadius = typedArray.getDimensionPixelSize(
            R.styleable.round_corner_layout_attrs_round_corner_bottom_left_radius,
            INVALID_VALUE
        )

        roundCornerAttrs.strokeWidth = typedArray.getDimensionPixelSize(
            R.styleable.round_corner_layout_attrs_round_corner_stroke_width,
            0
        )
        roundCornerAttrs.strokeColor = typedArray.getColor(
            R.styleable.round_corner_layout_attrs_round_corner_stroke_color,
            Color.TRANSPARENT
        )

        typedArray.recycle()
        rebuildPath()
    }


    private fun rebuildPath() {
        if (view.width == 0 || view.height == 0) return

        val radii = if (roundCornerAttrs.halfSizeRadius) {
            val size = max(view.width, view.height).toFloat() / 2
            floatArrayOf(size, size, size, size, size, size, size, size)
        } else {
            val radius = valueValidOr(roundCornerAttrs.radius, 0)

            val topLeftRadius = valueValidOr(roundCornerAttrs.topLeftRadius, radius).toFloat()
            val topRightRadius = valueValidOr(roundCornerAttrs.topRightRadius, radius).toFloat()
            val bottomRightRadius =
                valueValidOr(roundCornerAttrs.bottomRightRadius, radius).toFloat()
            val bottomLeftRadius = valueValidOr(roundCornerAttrs.bottomLeftRadius, radius).toFloat()
            floatArrayOf(
                topLeftRadius,
                topLeftRadius,
                topRightRadius,
                topRightRadius,
                bottomRightRadius,
                bottomRightRadius,
                bottomLeftRadius,
                bottomLeftRadius
            )
        }

        clipPath.reset()

        val rectF = RectF(
            0f, 0f, view.width.toFloat(),
            view.height.toFloat()
        )
        clipPath.addRoundRect(
            rectF,
            radii,
            Path.Direction.CW
        )

        paint.style = Paint.Style.STROKE
        paint.strokeWidth = roundCornerAttrs.strokeWidth.toFloat() * 2
        paint.color = roundCornerAttrs.strokeColor
    }

    private fun valueValidOr(value: Int, other: Int) = if (value != INVALID_VALUE) value else other

    fun onPreDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(clipPath)
    }

    fun onAfterDraw(canvas: Canvas) {
        if (roundCornerAttrs.strokeWidth != 0 && roundCornerAttrs.strokeColor != Color.TRANSPARENT) {
            canvas.drawPath(clipPath, paint)
        }
        canvas.restore()
    }

    fun invalidate() {
        rebuildPath()
    }
}