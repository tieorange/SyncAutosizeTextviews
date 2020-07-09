package com.tieorange.syncautosizetextviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.tieorange.syncautosizetextviews.R.id
import com.tieorange.syncautosizetextviews.R.layout
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.min


class MainActivity : AppCompatActivity() {

    private val text1 = "Here is the second TextView is"
    private val text2 = "Here"

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        val textView1: SizeAwareTextView = findViewById(id.textView1)
        val textView2: SizeAwareTextView = findViewById(id.textView2)

        val textViewList: MutableList<SizeAwareTextView> = ArrayList()
        textViewList.add(textView1)
        textViewList.add(textView2)

        val onTextSizeChangedListener: (SizeAwareTextView, Float) -> Unit = { view, textSize ->
            for (textView in textViewList) {
                if (textView != view && textView.textSize != view.textSize) {
                    val textSizeInt = textSize.toInt()
                    textView.setAutoSizeTextTypeUniformWithPresetSizes(
                        intArrayOf(textSizeInt),
                        TypedValue.COMPLEX_UNIT_PX
                    )
                }

                if (textView == textView1) {
                    textView1Size.text = textView1.textSize.toInt().toString()
                } else {
                    textView2Size.text = textView2.textSize.toInt().toString()
                }
            }
        }

        for (textView in textViewList) {
            textView.onTextSizeChangedListener = onTextSizeChangedListener
        }

        textView1.text = text1
        textView2.text = text2

        button.setOnClickListener {
            textView1.text = text2
            textView2.text = text1
        }

        editText.doAfterTextChanged { editable ->
            textView1.text =
                textView1.textSize.toInt()
                    .toString() + " Hello hello hello hello hello hello: $editable"
            textView2.text = textView2.textSize.toInt().toString() + editable.toString()
        }

        editTextBottom.doAfterTextChanged { editable ->
            left.text =
                left.textSize.toString() + " Hello hello hello hello hello hello: $editable"
            right.text = right.textSize.toString() + editable.toString()
        }

        // BOTTOM
        bottom()
    }

    private var leftTextSize: Float? = null
    private var rightTextSize: Float? = null
    private fun bottom() {
        left.onDraw = { view, textSize ->
            leftTextSize = textSize
            textViewBottomSize.text = textSize.toString()
            onTextViewDraw(view, textSize)

        }

        right.onDraw = { view, textSize ->
            rightTextSize = textSize
            textViewBottom2Size.text = textSize.toString()
            onTextViewDraw(view, textSize)
        }
        left.text = "eDirham Instant"
        right.text = "Paaaaasdfsfdsfsdfsdfdsfsdfdsfsdf"
    }

    private var viewsInitialisedCount = 0
    private fun onTextViewDraw(view: AutoSizeTextView, textSize: Float) {
        viewsInitialisedCount++

        if (viewsInitialisedCount >= 2) {
            onAllViewsAreDraw()
        }
    }

    private fun onAllViewsAreDraw() {
        val minTextSize = leftTextSize?.let { rightTextSize?.let { it1 -> min(it, it1) } }
        minTextSize?.let { left.setTextSizeManually(it);  right.setTextSizeManually(it)}
    }
}