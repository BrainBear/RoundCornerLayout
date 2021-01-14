package com.brainbear.roundcornerlayout

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.brainbear.roundcornerlayout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val layouts = listOf(viewBinding.rcLayout1, viewBinding.rcLayout2, viewBinding.rcLayout3)


        viewBinding.cbHalfSize.setOnCheckedChangeListener { buttonView, isChecked ->
            layouts.forEach { it.setHalfSizeRadius(isChecked) }
        }

        viewBinding.radius.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textRadius) { progress ->
            layouts.forEach { it.setRadius(progress.dp) }
        })
        viewBinding.radiusTopLeft.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textTopLeft) { progress ->
            layouts.forEach { it.setTopLeftRadius(progress.dp) }
        })
        viewBinding.radiusTopRight.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textTopRight) { progress ->
            layouts.forEach { it.setTopRightRadius(progress.dp) }
        })
        viewBinding.radiusBottomRight.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textBottomRight) { progress ->
            layouts.forEach { it.setBottomRightRadius(progress.dp) }
        })
        viewBinding.radiusBottomLeft.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textBottomLeft) { progress ->
            layouts.forEach { it.setBottomLeftRadius(progress.dp) }
        })


        viewBinding.stokeWidth.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textStrokeWidth) { progress ->
            layouts.forEach { it.setStrokeWidth(progress.dp) }
        })

        layouts.forEach {
            it.setStrokeColor(Color.WHITE)
        }

        viewBinding.radius.progress = viewBinding.radius.max / 2
        viewBinding.radiusTopLeft.progress = -1
        viewBinding.radiusTopRight.progress = -1
        viewBinding.radiusBottomRight.progress = -1
        viewBinding.radiusBottomLeft.progress = -1
    }
}

class SeekBarChangedListener(
    private val textView: TextView,
    private val onProgressChanged: (Int) -> Unit
) : SeekBar.OnSeekBarChangeListener {
    @SuppressLint("SetTextI18n")
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        val current = progress - 1
        if (current < 0) {
            textView.text = "INVALID"
        } else {
            textView.text = "$current dp"

        }
        onProgressChanged(current)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

}

val Number.dp: Int
    get() {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            Resources.getSystem().displayMetrics
        ).toInt()
    }