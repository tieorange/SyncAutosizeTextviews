package com.tieorange.syncautosizetextviews

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Log
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

        Log.d("TAG", "onDraw() called with: canvas = $canvas")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(
            "TAG",
            "onMeasure() called with: widthMeasureSpec = $widthMeasureSpec, heightMeasureSpec = $heightMeasureSpec"
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(
            "TAG",
            "onLayout() called with: changed = $changed, left = $left, top = $top, right = $right, bottom = $bottom"
        )
    }
}
