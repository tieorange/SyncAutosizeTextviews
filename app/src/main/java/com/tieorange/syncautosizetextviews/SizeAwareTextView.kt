package com.tieorange.syncautosizetextviews

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


class SizeAwareTextView : AppCompatTextView {
    var onTextSizeChangedListener: ((SizeAwareTextView, Float) -> Unit)? = null
    private var mLastTextSize: Float

    constructor(context: Context) : super(context) {
        mLastTextSize = textSize
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        mLastTextSize = textSize
    }

    // TODO: remove if not needed
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        mLastTextSize = textSize
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mLastTextSize != textSize) {
            mLastTextSize = textSize
            onTextSizeChangedListener?.invoke(this, mLastTextSize)
        }
    }
}
