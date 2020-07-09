package com.tieorange.syncautosizetextviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.tieorange.syncautosizetextviews.R.id
import com.tieorange.syncautosizetextviews.R.layout
import kotlinx.android.synthetic.main.activity_main.*


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

        // BOTTOM
        bottom()
    }

    private var textSize1: Float? = null
    private var textSize2: Float? = null
    private fun bottom() {
        editTextBottom.doAfterTextChanged { editable ->
            textViewBottom1.text =
                textViewBottom1.textSize.toString() + " Hello hello hello hello hello hello: $editable"
            textViewBottom2.text = textViewBottom2.textSize.toString() + editable.toString()
        }
        /*1. autosize T1 and T2
        2. get textSizes
        3. apply textSizes.min to B1 and B2
        4. hide/show
        * */
        textViewBottom1.onLayedout = { view, textSize ->
            textSize1 = textSize
            textViewBottomSize.text = textSize.toString()
//            textViewBottomSize.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
//            textViewBottom2.setTextSize(TypedValue.COMPLEX_UNIT_PX, 10f)
            Log.d("TAG", "textSize1: $textSize")
        }

        textViewBottom2.onLayedout = { view, textSize ->
            textSize2 = textSize
            textViewBottom2Size.text = textSize.toString()
            Log.d("TAG", "textSize2: $textSize")
        }


        textViewBottom1.text = "eDirham Instant"
        textViewBottom2.text = "Pay in app"
    }
}