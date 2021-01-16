package com.brainbear.corner

import android.content.Context
import android.content.res.ColorStateList
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
) : IRoundCorner {


    private val clipPath = Path()
    private val roundCornerAttrs = RoundCornerAttrs()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }
    private var radii = floatArrayOf()

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


        roundCornerAttrs.strokeColorStateList =
            typedArray.getColorStateList(R.styleable.round_corner_layout_attrs_round_corner_stroke_color)
                ?: ColorStateList.valueOf(Color.TRANSPARENT)

        typedArray.recycle()
        rebuildPath()
    }


    private fun rebuildPath() {
        if (view.width == 0 || view.height == 0) return

        radii = if (roundCornerAttrs.halfSizeRadius) {
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

        paint.strokeWidth = max(roundCornerAttrs.strokeWidth.toFloat() * 2, 0f)
        paint.color = roundCornerAttrs.strokeColorStateList.getColorForState(
            view.drawableState,
            Color.TRANSPARENT
        )
    }

    private fun valueValidOr(value: Int, other: Int) = if (value > INVALID_VALUE) value else other

    fun onPreDraw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(clipPath)
    }

    fun onAfterDraw(canvas: Canvas) {
        if (roundCornerAttrs.strokeWidth > 0 && paint.color != Color.TRANSPARENT) {
            canvas.drawPath(clipPath, paint)
        }
        canvas.restore()
    }

    fun invalidate() {
        rebuildPath()
    }

    override fun setHalfSizeRadius(halfSizeRadius: Boolean) {
        roundCornerAttrs.halfSizeRadius = halfSizeRadius
        view.invalidate()
    }

    override fun setRadius(radius: Int) {
        roundCornerAttrs.radius = radius
        view.invalidate()
    }

    override fun getRadius(): Int {
        return roundCornerAttrs.radius
    }

    override fun getRealRadius(): IntArray {
        return intArrayOf(radii[0].toInt(), radii[2].toInt(), radii[4].toInt(), radii[6].toInt())
    }

    override fun setTopLeftRadius(radius: Int) {
        roundCornerAttrs.topLeftRadius = radius
        view.invalidate()
    }

    override fun getTopLeftRadius(): Int {
        return roundCornerAttrs.topLeftRadius
    }

    override fun setTopRightRadius(radius: Int) {
        roundCornerAttrs.topRightRadius = radius
        view.invalidate()
    }

    override fun getTopRightRadius(): Int {
        return roundCornerAttrs.topRightRadius
    }

    override fun setBottomRightRadius(radius: Int) {
        roundCornerAttrs.bottomRightRadius = radius
        view.invalidate()
    }

    override fun getBottomRightRadius(): Int {
        return roundCornerAttrs.bottomRightRadius
    }

    override fun setBottomLeftRadius(radius: Int) {
        roundCornerAttrs.bottomLeftRadius = radius
        view.invalidate()
    }

    override fun getBottomLeftRadius(): Int {
        return roundCornerAttrs.bottomLeftRadius
    }

    override fun setStrokeColor(color: Int) {
        roundCornerAttrs.strokeColorStateList = ColorStateList.valueOf(color)
        view.invalidate()
    }

    override fun setStrokeColors(colors: ColorStateList) {
        roundCornerAttrs.strokeColorStateList = colors
        view.invalidate()
    }

    override fun setStrokeWidth(strokeWidth: Int) {
        roundCornerAttrs.strokeWidth = strokeWidth
        view.invalidate()
    }

    override fun drawableStateChanged() {
        view.invalidate()
    }
}