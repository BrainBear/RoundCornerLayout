package com.brainbear.corner

import android.graphics.Color

data class RoundCornerAttrs(
    var halfSizeRadius: Boolean = false,
    var radius: Int = 0,
    var topLeftRadius: Int = 0,
    var topRightRadius: Int = 0,
    var bottomRightRadius: Int = 0,
    var bottomLeftRadius: Int = 0,
    var strokeColor: Int = Color.TRANSPARENT,
    var strokeWidth: Int = 0,
)