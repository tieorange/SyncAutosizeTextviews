package com.tieorange.syncautosizetextviews

import kotlin.math.min

class TextViewSizeCoordinator(
    private val leftView: AutoSizeTextView,
    private val rightView: AutoSizeTextView
) {

    private var leftTextSize: Float? = null
    private var rightTextSize: Float? = null
    private var viewsInitialisedCount = 0

    init {
        leftView.onDraw = { view, textSize ->
            leftTextSize = textSize
            onTextViewDraw(view, textSize)
        }

        rightView.onDraw = { view, textSize ->
            rightTextSize = textSize
            onTextViewDraw(view, textSize)
        }
    }

    private fun onTextViewDraw(view: AutoSizeTextView, textSize: Float) {
        viewsInitialisedCount++

        if (viewsInitialisedCount >= 2) {
            onAllViewsAreDraw()
        }
    }

    private fun onAllViewsAreDraw() {
        val minTextSize = leftTextSize?.let { rightTextSize?.let { it1 -> min(it, it1) } }
        minTextSize?.let { leftView.setTextSizeManually(it); rightView.setTextSizeManually(it) }
    }
}