package com.example.doublestopovertones

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class NoteView : View{

    private var linePaint_: Paint? = null

    // コンストラクタ
    constructor(context: Context?) : super(context) {
        initialize()
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initialize()
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initialize()
    }
    override fun onDraw(canvas: Canvas?) {
        drawGuideLine(canvas)
        drawLine(canvas)
    }
    private fun drawGuideLine(canvas: Canvas?) {
        linePaint_!!.color = resources.getColor(R.color.guide)
        var lineY = 0
        for (i in 0..divisionNumber) {
            canvas?.drawLine(
                    0f, (oneLineHeight * lineY).toFloat(),
                    noteViewWidth.toFloat(), (oneLineHeight * lineY).toFloat(), linePaint_!!)
            lineY = lineY + 2
        }
    }

    private fun drawLine(canvas: Canvas?) {
        linePaint_!!.color = resources.getColor(R.color.black)
        var lineY = biginingOfStuffNotation       //五線譜を描画し始めるライン数
        for (i in 1..5) {
            canvas?.drawLine(
                0f, (oneLineHeight * lineY).toFloat(),
                noteViewWidth.toFloat(), (oneLineHeight * lineY).toFloat(), linePaint_!!)
            lineY = lineY + 2
        }
    }
    // 初期化
    private fun initialize(){
        linePaint_ = Paint()
        linePaint_!!.strokeWidth = 2f
        linePaint_!!.color = resources.getColor(R.color.black)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        // Viewの高さ，幅が取れるのでそれらに依存した計算を行う
    }
}
