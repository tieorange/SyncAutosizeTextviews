package com.tieorange.syncautosizetextviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.tieorange.syncautosizetextviews.R.id
import com.tieorange.syncautosizetextviews.R.layout
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.roundToInt


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
                    val textSizeInt = textSize.roundToInt()
                    textView.setAutoSizeTextTypeUniformWithPresetSizes(
                        intArrayOf(textSizeInt),
                        TypedValue.COMPLEX_UNIT_PX
                    )
                }

                if (textView == textView1) {
                    textView1Size.text = textView1.textSize.toString()
                } else {
                    textView2Size.text = textView2.textSize.toString()
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
                textView1.textSize.toString() + " Hello hello hello hello hello hello: $editable"
            textView2.text = textView2.textSize.toString() + editable.toString()
        }

        // BOTTOM
        bottom()
    }

    private fun bottom() {
        editTextBottom.doAfterTextChanged { editable ->
            textViewBottom1.text =
                textViewBottom1.textSize.toString() + " Hello hello hello hello hello hello: $editable"
            textViewBottom2.text = textViewBottom2.textSize.toString() + editable.toString()
        }

        textViewBottom1.onTextSizeChanged = { view, textSize ->
            textViewBottom2.textSize = textSize
        }

        textViewBottom2.onTextSizeChanged = { view, textSize ->
            textViewBottom1.textSize = textSize
        }
    }
}