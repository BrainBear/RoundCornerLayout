package com.brainbear.roundcornerlayout

import android.R
import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.brainbear.corner.IRoundCorner
import com.brainbear.roundcornerlayout.databinding.ActivityMainBinding
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener


class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var roundCornerViews: List<IRoundCorner>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        roundCornerViews =
            listOf(viewBinding.rcLayout1, viewBinding.rcLayout2, viewBinding.rcLayout3)


        viewBinding.cbHalfSize.setOnCheckedChangeListener { buttonView, isChecked ->
            roundCornerViews.forEach { it.setHalfSizeRadius(isChecked) }
        }

        viewBinding.radius.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textRadius) { progress ->
            roundCornerViews.forEach { it.setRadius(progress.dp) }
        })
        viewBinding.radiusTopLeft.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textTopLeft) { progress ->
            roundCornerViews.forEach { it.setTopLeftRadius(progress.dp) }
        })
        viewBinding.radiusTopRight.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textTopRight) { progress ->
            roundCornerViews.forEach { it.setTopRightRadius(progress.dp) }
        })
        viewBinding.radiusBottomRight.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textBottomRight) { progress ->
            roundCornerViews.forEach { it.setBottomRightRadius(progress.dp) }
        })
        viewBinding.radiusBottomLeft.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textBottomLeft) { progress ->
            roundCornerViews.forEach { it.setBottomLeftRadius(progress.dp) }
        })


        viewBinding.stokeWidth.setOnSeekBarChangeListener(SeekBarChangedListener(viewBinding.textStrokeWidth) { progress ->
            roundCornerViews.forEach { it.setStrokeWidth(progress.dp) }
        })


        updateStrokeColor(Color.WHITE, "FFFFFF")

        viewBinding.colorView.setOnClickListener {
            showStrokeColorSelectDialog()
        }

        viewBinding.radius.progress = viewBinding.radius.max / 2
        viewBinding.radiusTopLeft.progress = -1
        viewBinding.radiusTopRight.progress = -1
        viewBinding.radiusBottomRight.progress = -1
        viewBinding.radiusBottomLeft.progress = -1
        viewBinding.stokeWidth.progress = 6
    }


    private fun showStrokeColorSelectDialog() {
        ColorPickerDialog.Builder(this)
            .setTitle("Stroke Color")
            .setPositiveButton(getString(R.string.ok),
                ColorEnvelopeListener { envelope, _ ->
                    updateStrokeColor(envelope.color, envelope.hexCode)
                })
            .setNegativeButton(
                getString(R.string.cancel)
            ) { dialogInterface, _ -> dialogInterface.dismiss() }
            .attachAlphaSlideBar(true) // the default value is true.
            .attachBrightnessSlideBar(true) // the default value is true.
            .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
            .show()
    }


    private fun updateStrokeColor(color: Int, text: String) {
        viewBinding.colorView.setBackgroundColor(color)
        viewBinding.textStrokeColor.text = "#$text"
        roundCornerViews.forEach {
            it.setStrokeColor(color)
        }

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