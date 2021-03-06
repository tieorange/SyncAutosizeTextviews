package com.tieorange.syncautosizetextviews

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Build
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView

private const val NO_LINE_LIMIT = -1
class AutoSizeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {
    private var minTextSize = 0f
    private var maxTextSize = 0f
    private val availableSpaceRect = RectF()
    private val sizeTester: SizeTester
    private var spacingMult = 1.0f
    private var spacingAdd = 0.0f
    private var widthLimit: Int = 0
    private var maxLines: Int = 0
    private var initialized = false
    private var textPaint: TextPaint? = null

    private interface SizeTester {
        /**
         * @param suggestedSize  Size of text to be tested
         * @param availableSpace available space in which text must fit
         * @return an integer < 0 if after applying `suggestedSize` to
         * text, it takes less space than `availableSpace`, > 0
         * otherwise
         */
        fun onTestSize(suggestedSize: Int, availableSpace: RectF): Int
    }

    init {
        minTextSize =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
        maxTextSize = textSize
        textPaint = TextPaint(paint)
        if (maxLines == 0)
            maxLines = NO_LINE_LIMIT
        sizeTester = object : SizeTester {
            val textRect = RectF()

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onTestSize(suggestedSize: Int, availableSpace: RectF): Int {
                textPaint!!.textSize = suggestedSize.toFloat()
                val transformationMethod = transformationMethod
                val text = transformationMethod?.getTransformation(
                    getText(), this@AutoSizeTextView
                )?.toString()
                    ?: getText().toString()
                val singleLine = maxLines == 1
                if (singleLine) {
                    textRect.bottom = textPaint!!.fontSpacing
                    textRect.right = textPaint!!.measureText(text)
                }
                textRect.offsetTo(0f, 0f)
                return if (availableSpace.contains(textRect)) -1 else 1
            }
        }
        initialized = true
    }

    fun isValidWordWrap(before: Char, after: Char): Boolean {
        return before == ' ' || before == '-'
    }

    override fun setAllCaps(allCaps: Boolean) {
        super.setAllCaps(allCaps)
        adjustTextSize()
    }

    override fun setTypeface(tf: Typeface?) {
        super.setTypeface(tf)
        adjustTextSize()
    }

    override fun setMaxLines(maxLines: Int) {
        super.setMaxLines(maxLines)
        this.maxLines = maxLines
        adjustTextSize()
    }

    override fun getMaxLines(): Int {
        return maxLines
    }

    override fun setSingleLine() {
        super.setSingleLine()
        maxLines = 1
        adjustTextSize()
    }

    override fun setSingleLine(singleLine: Boolean) {
        super.setSingleLine(singleLine)
        if (singleLine)
            maxLines = 1
        else
            maxLines = NO_LINE_LIMIT
        adjustTextSize()
    }

    override fun setLines(lines: Int) {
        super.setLines(lines)
        maxLines = lines
        adjustTextSize()
    }

    fun setMinTextSize(minTextSize: Float) {
        this.minTextSize = minTextSize
        adjustTextSize()
    }

    private fun superSetTextSize(minTextSize: Int) {
        val textSize =
            binarySearch(minTextSize, maxTextSize.toInt(), sizeTester, availableSpaceRect)
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    override fun setTextSize(size: Float) {
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        maxTextSize = size
        adjustTextSize()
    }

    fun setTextSizeManually(size:Float){
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
    }

    override fun setTextSize(unit: Int, size: Float) {
        val context = context
        val resources = if (context == null)
            Resources.getSystem()
        else
            context.resources
        maxTextSize = TypedValue.applyDimension(unit, size, resources.displayMetrics)
        adjustTextSize()
    }

    fun adjustTextSize() {
        if (!initialized)
            return
        val minTextSize = minTextSize.toInt()
        val heightLimit = measuredHeight - compoundPaddingBottom - compoundPaddingTop
        widthLimit = measuredWidth - compoundPaddingLeft - compoundPaddingRight
        if (widthLimit <= 0)
            return
        refreshTextPaint()
        availableSpaceRect.right = widthLimit.toFloat()
        availableSpaceRect.bottom = heightLimit.toFloat()
        superSetTextSize(minTextSize)
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        super.setLineSpacing(add, mult)
        spacingMult = mult
        spacingAdd = add
    }

    private fun refreshTextPaint() {
        textPaint = TextPaint(paint)
    }

    private fun binarySearch(
        start: Int,
        end: Int,
        sizeTester: SizeTester,
        availableSpace: RectF
    ): Int {
        var lastBest = start
        var lo = start
        var hi = end - 1
        var mid: Int
        while (lo <= hi) {
            mid = (lo + hi).ushr(1)
            val midValCmp = sizeTester.onTestSize(mid, availableSpace)
            when {
                midValCmp < 0 -> {
                    lastBest = lo
                    lo = mid + 1
                }
                midValCmp > 0 -> {
                    hi = mid - 1
                    lastBest = hi
                }
                else -> return mid
            }
        }
        return lastBest
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

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d("TAG", "onDraw() called with: canvas = $canvas")
        onDraw?.invoke(this, textSize)
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        super.onTextChanged(text, start, before, after)
        adjustTextSize()
    }

    override fun onSizeChanged(width: Int, height: Int, oldwidth: Int, oldheight: Int) {
        super.onSizeChanged(width, height, oldwidth, oldheight)
        if (width != oldwidth || height != oldheight) {
            adjustTextSize()
        }
    }

    var onDraw: ((view: AutoSizeTextView, textSize: Float) -> Unit)? = null
}