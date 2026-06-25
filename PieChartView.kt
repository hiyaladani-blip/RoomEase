package com.example.roomease

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class PieChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var data: List<PieEntry> = emptyList()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rectF = RectF()

    data class PieEntry(val value: Float, val color: Int)

    fun setData(newData: List<PieEntry>) {
        data = newData
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (data.isEmpty()) return

        val width = width.toFloat()
        val height = height.toFloat()
        val radius = Math.min(width, height) / 2 * 0.8f
        rectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius)

        val total = data.sumOf { it.value.toDouble() }.toFloat()
        var startAngle = -90f

        for (entry in data) {
            val sweepAngle = (entry.value / total) * 360f
            paint.color = entry.color
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = radius * 0.3f
            paint.strokeCap = Paint.Cap.BUTT
            canvas.drawArc(rectF, startAngle, sweepAngle, false, paint)
            startAngle += sweepAngle
        }
    }
}