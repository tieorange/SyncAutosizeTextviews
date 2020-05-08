package com.tieorange.syncautosizetextviews

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import com.tieorange.syncautosizetextviews.R.id
import com.tieorange.syncautosizetextviews.R.layout
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val text1 = "Here is the second TextView  is the first TextView"
    val text2 = "Here is the second"

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
                    textView.setAutoSizeTextTypeUniformWithPresetSizes(
                        intArrayOf(textSize.toInt()),
                        TypedValue.COMPLEX_UNIT_PX
                    )
                }
            }
        }

        for (textView in textViewList) {
            textView.onTextSizeChangedListener = onTextSizeChangedListener
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable) {
                textView1.text =
                    textView1.textSize.toString() + " Hello hello hello hello hello hello: $editable"
                textView2.text = textView2.textSize.toString() + editable.toString()
            }

            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }
        })
    }
}